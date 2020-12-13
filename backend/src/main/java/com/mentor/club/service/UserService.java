package com.mentor.club.service;

import com.google.gson.Gson;
import com.mentor.club.exception.InternalException;
import com.mentor.club.model.InternalResponse;
import com.mentor.club.model.PublicKeyResponse;
import com.mentor.club.model.authentication.AuthenticationRequest;
import com.mentor.club.model.authentication.AuthenticationResult;
import com.mentor.club.model.authentication.token.*;
import com.mentor.club.model.password.ChangeForgottenPasswordRequest;
import com.mentor.club.model.password.ChangePasswordRequest;
import com.mentor.club.model.user.NewUser;
import com.mentor.club.model.user.User;
import com.mentor.club.model.user.UserStatus;
import com.mentor.club.repository.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.*;

import static com.mentor.club.model.error.HttpCallError.FAILED_TO_SAVE_TO_DB;
import static com.mentor.club.model.error.HttpCallError.INVALID_INPUT;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private IUserRepository userRepository;
    private IAccessTokenRepository accessTokenRepository;
    private IRefreshTokenRepository refreshTokenRepository;
    private AwsService awsService;
    private JwtService jwtService;
    private IPasswordResetTokenRepository passwordResetTokenRepository;
    private RsaService rsaService;

    @Value("${backend.deployment.url}")
    private String backendDeploymentUrl;

    @Autowired
    public UserService(IUserRepository userRepository,
                       IAccessTokenRepository accessTokenRepository,
                       IRefreshTokenRepository refreshTokenRepository,
                       AwsService awsService,
                       JwtService jwtService,
                       IPasswordResetTokenRepository passwordResetTokenRepository,
                       RsaService rsaService) {
        this.userRepository = userRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.awsService = awsService;
        this.jwtService = jwtService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.rsaService = rsaService;
    }

    public ResponseEntity authenticate(AuthenticationRequest authentication, HttpServletResponse response) {
        final InternalResponse authResponse = authenticateWithCredentials(authentication, response);

        return new ResponseEntity<>(authResponse.getJson(), authResponse.getStatus());
    }

    public ResponseEntity createNewUser(NewUser newUser) {
        if (isEmailAlreadyInUse(newUser.getEmail())) {
            Optional<User> userWithGivenEmail = userRepository.findUserByEmail(newUser.getEmail());

            String username = userWithGivenEmail.get().getUsername();
            String message = "{\"error\":\"email already in use by user with username \'" + username + "\'\"}";

            return new ResponseEntity<>(new Gson().toJson(message), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEmail(newUser.getEmail());
        user.setUsername(newUser.getUsername());
        user.setHashedPassword(hashPassword(newUser.getPassword()));
        user.setName(newUser.getName());
        user.setThumbnailBase64(newUser.getThumbnailBase64());

        try {
            User createdUser = userRepository.save(user);

            String confirmationUrl = backendDeploymentUrl + "/user/confirm-email/" + createdUser.getId();

            HttpStatus confirmationEmailSentStatusCode = awsService.sendConfirmationEmail(confirmationUrl, user);

            LOGGER.debug("Status code of sending confirmation email: " + confirmationEmailSentStatusCode.toString());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Failed to create user with username " + newUser.getUsername() + ". Error: " + exception.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity confirmEmail(UUID userId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setUserStatus(UserStatus.CREATED_CONFIRMED_EMAIL);

                userRepository.save(user);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Failed to confirm email for user with userId " + userId);

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private InternalResponse handleUnauthorizedFlow(AuthenticationRequest authentication) {
        try {
            InternalResponse response = new InternalResponse();
            response.setStatus(HttpStatus.OK);

            String username = authentication.getUsername();
            String password = authentication.getPassword();
            UUID deviceId = authentication.getDeviceId();

            Optional<User> optionalUser = userRepository.findUserByUsername(username);

            if (!optionalUser.isPresent()) {
                LOGGER.error("User with username " + username + " not found!");

                response.setStatus(HttpStatus.NOT_FOUND);
                response.setJson("User with username " + username + " not found!");

                return response;
            }

            if (deviceId == null) {
                LOGGER.error("Device id is not set for user " + username + "!");

                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setJson("User with username " + username + " not found!");

                return response;
            }

            if (!checkPassword(password, optionalUser.get().getHashedPassword())) {
                LOGGER.error("Incorrect password for user with username " + username + "!");

                response.setJson("Incorrect password for user with username " + username + "!");
                response.setStatus(HttpStatus.UNAUTHORIZED);
            }

            return response;
        } catch (Exception exception) {
            LOGGER.error(ExceptionUtils.getStackTrace(exception));

            throw new InternalException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, INVALID_INPUT, exception.getMessage());
        }
    }

    private InternalResponse handleAuthorizedFlow(AuthenticationRequest authentication, HttpServletResponse httpServletResponse) {
        try {
            InternalResponse response = new InternalResponse();
            AuthenticationResult result = new AuthenticationResult();

            response.setStatus(HttpStatus.OK);

            String username = authentication.getUsername();
            String password = authentication.getPassword();
            UUID deviceId = authentication.getDeviceId();

            Optional<User> optionalUser = userRepository.findUserByUsername(username);

            if (checkPassword(password, optionalUser.get().getHashedPassword())) {
                LOGGER.debug("Correct password for user with username " + username + "!");

                User user = optionalUser.get();

                jwtService.deleteAllJwtTokensForUserForDevice(user, deviceId);

                result.setUsername(username);
                result.setThumbnailPhoto(user.getThumbnailBase64());
                JwtTokenWithDeviceId accessToken = (JwtTokenWithDeviceId) createJwtToken(user, JwtTokenLifetime.ACCESS_TOKEN_LIFESPAN_IN_SECONDS.getLifetime(), accessTokenRepository, JwtTokenType.ACCESS_TOKEN);
                setDeviceIdOnJwtToken(accessToken, deviceId, accessTokenRepository);

                result.setDisplayName(user.getName());
                result.setThumbnailPhoto(user.getThumbnailBase64());

                response.setJson(result);
                response.setStatus(HttpStatus.OK);

                JwtTokenWithDeviceId refreshToken = (JwtTokenWithDeviceId) createJwtToken(user, JwtTokenLifetime.REFRESH_TOKEN_LIFESPAN_IN_SECONDS.getLifetime(), refreshTokenRepository, JwtTokenType.REFRESH_TOKEN);
                setDeviceIdOnJwtToken(refreshToken, deviceId, refreshTokenRepository);
                Cookie cookieWithRefreshToken = createCookieWithRefreshToken(refreshToken.getToken());

                httpServletResponse.addCookie(cookieWithRefreshToken);
                addSameSiteCookieAttribute(httpServletResponse);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED);
            }

            return response;
        } catch (Exception exception) {
            LOGGER.error(ExceptionUtils.getStackTrace(exception));

            throw new InternalException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, INVALID_INPUT, exception.getMessage());
        }
    }

    private InternalResponse authenticateWithCredentials(AuthenticationRequest authentication, HttpServletResponse httpServletResponse) {
        InternalResponse internalResponse = handleUnauthorizedFlow(authentication);

        if (internalResponse.getStatus().is2xxSuccessful()) {
            return handleAuthorizedFlow(authentication, httpServletResponse);
        }

        return internalResponse;
    }

    private void setDeviceIdOnJwtToken(JwtTokenWithDeviceId jwtTokenWithDeviceId, UUID deviceId, IJwtTokenWithDeviceIdRepository jwtTokenWithDeviceIdRepository) {
        try {
            jwtTokenWithDeviceId.setDeviceId(deviceId);

            jwtTokenWithDeviceIdRepository.save(jwtTokenWithDeviceId);
        } catch (Exception exception) {
            LOGGER.error("Could not update refresh token with deviceId " + deviceId + " for refreshToken " + jwtTokenWithDeviceId.getToken() + "!");

            throw new InternalException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, FAILED_TO_SAVE_TO_DB, exception.getMessage());
        }
    }

    private ResponseEntity handleUnauthorizedTokenRefreshFlow(String refreshTokenCookie, Optional<String> authorization, UUID deviceId) {
        try {
            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByTokenAndDeviceId(refreshTokenCookie, deviceId);

            if (!optionalRefreshToken.isPresent()) {
                Optional<RefreshToken> optionalRefreshTokenWithoutDeviceId = refreshTokenRepository.findByToken(refreshTokenCookie);

                if (optionalRefreshTokenWithoutDeviceId.isPresent()) {
                    jwtService.deleteJwtToken(refreshTokenRepository, optionalRefreshTokenWithoutDeviceId.get());
                }

                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            JwtToken refreshToken = optionalRefreshToken.get();

            if (refreshToken.isExpired()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Optional<User> optionalUser = userRepository.findById(refreshToken.getUser().getId());

            if (!optionalUser.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception exception) {
            LOGGER.error("Could not authorize with refresh token " + refreshTokenCookie + " and deviceId " + deviceId + "! Error: " + exception.getMessage());

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    private ResponseEntity handleAuthorizedTokenRefreshFlow(String refreshTokenCookie, Optional<String> authorization, UUID deviceId, HttpServletResponse httpServletResponse) {
        try {
            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByTokenAndDeviceId(refreshTokenCookie, deviceId);
            Optional<User> optionalUser = userRepository.findById(optionalRefreshToken.get().getUser().getId());

            jwtService.deleteJwtToken(refreshTokenRepository, optionalRefreshToken.get());

            JwtTokenWithDeviceId newRefreshToken = (JwtTokenWithDeviceId) createJwtToken(optionalUser.get(), JwtTokenLifetime.REFRESH_TOKEN_LIFESPAN_IN_SECONDS.getLifetime(), refreshTokenRepository, JwtTokenType.REFRESH_TOKEN);
            setDeviceIdOnJwtToken(newRefreshToken, deviceId, refreshTokenRepository);

            Cookie cookieWithRefreshToken = createCookieWithRefreshToken(newRefreshToken.getToken());

            httpServletResponse.addCookie(cookieWithRefreshToken);
            addSameSiteCookieAttribute(httpServletResponse);

            WrappedJwtToken accessWrappedJwtToken = new WrappedJwtToken();

            accessWrappedJwtToken.setToken(createJwtToken(optionalUser.get(), JwtTokenLifetime.ACCESS_TOKEN_LIFESPAN_IN_SECONDS.getLifetime(), accessTokenRepository, JwtTokenType.ACCESS_TOKEN).getToken());

            if (authorization.isPresent()) {
                try {
                    AccessToken accessToken = jwtService.getAccessTokenFromAuthorizationString(authorization.get());

                    jwtService.deleteJwtToken(accessTokenRepository, accessToken);
                } catch (Exception exception) {
                    LOGGER.error("Could not delete access token for refresh token with deviceId " + deviceId + " and refreshToken " + refreshTokenCookie + "!");
                }
            }

            return new ResponseEntity<>(accessWrappedJwtToken, HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Could not authorize with refresh token " + refreshTokenCookie + " and deviceId " + deviceId + "!Error: " + exception.getMessage());

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity getRefreshAndAccessToken(String refreshTokenCookie, Optional<String> authorization, UUID deviceId, HttpServletResponse httpServletResponse) {
        ResponseEntity responseEntity = handleUnauthorizedTokenRefreshFlow(refreshTokenCookie, authorization, deviceId);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity;
        }

        return handleAuthorizedTokenRefreshFlow(refreshTokenCookie, authorization, deviceId, httpServletResponse);
    }

    private Cookie createCookieWithRefreshToken(String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);

        cookie.setHttpOnly(true);
        cookie.setPath("/refresh-token");
        cookie.setMaxAge(Math.toIntExact(JwtTokenLifetime.REFRESH_TOKEN_LIFESPAN_IN_SECONDS.getLifetime()));
        cookie.setSecure(true); // TODO check if needed with httpd

        return cookie;
    }

    private void addSameSiteCookieAttribute(HttpServletResponse response) {
        Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
        boolean firstHeader = true;

        for (String header : headers) {
            if (firstHeader) {
                response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=Strict"));

                firstHeader = false;

                continue;
            }

            response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=Strict"));
        }
    }

    public ResponseEntity getPublicKey() {
        return new ResponseEntity<>(getPublicKeyResponse(), HttpStatus.OK);
    }

    private JwtToken createJwtToken(User user, Long tokenLifetime, IJwtTokenRepository repository, JwtTokenType jwtTokenType) {
        List<String> userGroups = Arrays.asList("user"); // change in the future
        String jwtTokenString = rsaService.generateToken(user.getUsername(), userGroups, tokenLifetime);

        try {
            JwtToken jwtToken = JwtTokenFactory.createJwtTokenOfType(jwtTokenType);

            jwtToken.setToken(jwtTokenString);
            jwtToken.setUser(user);
            jwtToken.setExpirationDate(Date.from(Instant.now().plusSeconds(tokenLifetime)));

            repository.save(jwtToken);

            return jwtToken;
        } catch (Exception exception) {
            LOGGER.error("Could not create token of type " + jwtTokenType.name() + " for user " + user.getUsername() + "!");

            throw new InternalException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, FAILED_TO_SAVE_TO_DB, exception.getMessage());
        }
    }

    public PublicKeyResponse getPublicKeyResponse() {

        final PublicKeyResponse publicKeyResponse = new PublicKeyResponse();

        // get public key from docker volume
        // publicKeyResponse.setPublic_key(getProperty("PUBLIC_KEY"));

        return publicKeyResponse;
    }

    private String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    private Boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public ResponseEntity logout(String authorization, String username) {
        return jwtService.logout(authorization, username);
    }

    private boolean isEmailAlreadyInUse(String email) {
        Optional<User> userWithGivenEmail = userRepository.findUserByEmail(email);

        return userWithGivenEmail.isPresent();
    }

    public ResponseEntity generateResetForgottenPasswordEmail(String email) {
        Optional<User> userWithGivenEmail = userRepository.findUserByEmail(email);

        if (!userWithGivenEmail.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        JwtToken passwordResetToken = createPasswordResetTokenForUser(userWithGivenEmail.get());
        String resetPasswordUrl = backendDeploymentUrl + "/user/change-password?token=" + passwordResetToken.getToken();
        HttpStatus passwordResetEmailSentStatusCode = awsService.sendPasswordResetEmail(resetPasswordUrl, userWithGivenEmail.get());

        LOGGER.debug("Status code of sending password reset email: " + passwordResetEmailSentStatusCode.toString());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity changeForgottenPassword(ChangeForgottenPasswordRequest changeForgottenPasswordRequest) {
        ResponseEntity responseEntity = validatePasswordResetToken(changeForgottenPasswordRequest.getPasswordResetToken());

        // TODO actually change password

        return responseEntity;
    }

    public ResponseEntity changePassword(ChangePasswordRequest changePasswordRequest, String authorization) {
        ResponseEntity jwtValidationResult = jwtService.validateAccessToken(authorization);
        InternalResponse internalResponse = new InternalResponse();

        if (!jwtValidationResult.getStatusCode().is2xxSuccessful()) {
            return jwtValidationResult;
        }

        Optional<User> user = userRepository.findUserByUsername(changePasswordRequest.getUsername());

        if (!user.isPresent()) {
            internalResponse.setStatus(HttpStatus.NOT_FOUND);
            internalResponse.setJson("User with username " + changePasswordRequest.getUsername() + " not found!");

            return new ResponseEntity<>(internalResponse.getJson(), internalResponse.getStatus());
        }

        String userHashedPassword = user.get().getHashedPassword();
        boolean isPasswordCorrect = checkPassword(changePasswordRequest.getPassword(), userHashedPassword);

        if (!isPasswordCorrect) {
            internalResponse.setStatus(HttpStatus.UNAUTHORIZED);
            internalResponse.setJson("Incorrect password for user with username " + changePasswordRequest.getUsername() + "!");

            return new ResponseEntity<>(internalResponse.getJson(), internalResponse.getStatus());
        }

        user.get().setHashedPassword(hashPassword(changePasswordRequest.getNewPassword()));

        try {
            userRepository.save(user.get());

            internalResponse.setJson("Successfully change password for user with username " + changePasswordRequest.getUsername() + "!");
            internalResponse.setStatus(HttpStatus.OK);

            return new ResponseEntity<>(internalResponse.getJson(), internalResponse.getStatus());
        } catch (Exception exception) {
            internalResponse.setJson("Failed to change password for user with username " + changePasswordRequest.getUsername() + ". Error: " + exception.getMessage());
            internalResponse.setStatus(HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(internalResponse.getJson(), internalResponse.getStatus());
        }
    }

    private ResponseEntity validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (!passwordResetToken.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (passwordResetToken.get().isExpired()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private PasswordResetToken createPasswordResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = new PasswordResetToken(JwtTokenType.PASSWORD_RESET_TOKEN);

        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpirationDate(Date.from(Instant.now().plusSeconds(JwtTokenLifetime.PASSWORD_RESET_TOKEN_LIFESPAN_IN_SECONDS.getLifetime())));

        return passwordResetTokenRepository.save(passwordResetToken);
    }
}
