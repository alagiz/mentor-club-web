package com.mentor.club.service;

import com.google.gson.Gson;
import com.mentor.club.exception.InternalException;
import com.mentor.club.model.InternalResponse;
import com.mentor.club.model.authentication.AuthenticationRequest;
import com.mentor.club.model.authentication.AuthenticationResult;
import com.mentor.club.model.authentication.token.JwtTokenLifetime;
import com.mentor.club.model.authentication.token.JwtTokenType;
import com.mentor.club.model.authentication.token.JwtTokenWithDeviceId;
import com.mentor.club.model.user.NewUser;
import com.mentor.club.model.user.User;
import com.mentor.club.model.user.UserStatus;
import com.mentor.club.repository.IAccessTokenRepository;
import com.mentor.club.repository.IRefreshTokenRepository;
import com.mentor.club.repository.IUserRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

import static com.mentor.club.model.error.HttpCallError.INVALID_INPUT;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private IUserRepository userRepository;
    private IAccessTokenRepository accessTokenRepository;
    private IRefreshTokenRepository refreshTokenRepository;
    private AwsService awsService;
    private JwtService jwtService;
    private PasswordService passwordService;

    @Value("${backend.deployment.url}")
    private String backendDeploymentUrl;

    @Autowired
    public UserService(IUserRepository userRepository,
                       IAccessTokenRepository accessTokenRepository,
                       IRefreshTokenRepository refreshTokenRepository,
                       AwsService awsService,
                       JwtService jwtService,
                       PasswordService passwordService) {
        this.userRepository = userRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.awsService = awsService;
        this.jwtService = jwtService;
        this.passwordService = passwordService;
    }

    public ResponseEntity authenticate(AuthenticationRequest authentication, HttpServletResponse httpServletResponse) {
        InternalResponse authResponse = handleAuthenticationUnauthorizedFlow(authentication);

        if (!authResponse.getStatus().is2xxSuccessful()) {
            return new ResponseEntity<>(authResponse.getJson(), authResponse.getStatus());
        }

        authResponse = handleAuthenticateAuthorizedFlow(authentication, httpServletResponse);

        return new ResponseEntity<>(authResponse.getJson(), authResponse.getStatus());
    }

    public ResponseEntity createNewUser(NewUser newUser) {
        try {
            if (isEmailAlreadyInUse(newUser.getEmail())) {
                Optional<User> userWithGivenEmail = userRepository.findUserByEmail(newUser.getEmail());

                String username = userWithGivenEmail.get().getUsername();
                String message = "{\"error\":\"email already in use by user with username \'" + username + "\'\"}";

                return new ResponseEntity<>(new Gson().toJson(message), HttpStatus.BAD_REQUEST);
            }

            User user = new User();
            user.setEmail(newUser.getEmail());
            user.setUsername(newUser.getUsername());
            user.setHashedPassword(passwordService.hashPassword(newUser.getPassword()));
            user.setName(newUser.getName());
            user.setThumbnailBase64(newUser.getThumbnailBase64());

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

    private InternalResponse handleAuthenticationUnauthorizedFlow(AuthenticationRequest authentication) {
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

            if (!passwordService.isProvidedPasswordCorrect(password, optionalUser.get().getHashedPassword())) {
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

    private InternalResponse handleAuthenticateAuthorizedFlow(AuthenticationRequest authentication, HttpServletResponse httpServletResponse) {
        try {
            InternalResponse response = new InternalResponse();
            AuthenticationResult result = new AuthenticationResult();

            String username = authentication.getUsername();
            String password = authentication.getPassword();
            UUID deviceId = authentication.getDeviceId();

            Optional<User> optionalUser = userRepository.findUserByUsername(username);

            if (passwordService.isProvidedPasswordCorrect(password, optionalUser.get().getHashedPassword())) {
                LOGGER.debug("Correct password for user with username " + username + "!");

                User user = optionalUser.get();

                jwtService.deleteAllJwtTokensForUserForDevice(user, deviceId);

                result.setUsername(username);
                result.setThumbnailPhoto(user.getThumbnailBase64());
                JwtTokenWithDeviceId accessToken = (JwtTokenWithDeviceId) jwtService.createJwtToken(user, JwtTokenLifetime.ACCESS_TOKEN_LIFESPAN_IN_SECONDS.getLifetime(), accessTokenRepository, JwtTokenType.ACCESS_TOKEN);
                jwtService.setDeviceIdOnJwtToken(accessToken, deviceId, accessTokenRepository);

                result.setDisplayName(user.getName());
                result.setThumbnailPhoto(user.getThumbnailBase64());

                response.setJson(result);
                response.setStatus(HttpStatus.OK);

                JwtTokenWithDeviceId refreshToken = (JwtTokenWithDeviceId) jwtService.createJwtToken(user, JwtTokenLifetime.REFRESH_TOKEN_LIFESPAN_IN_SECONDS.getLifetime(), refreshTokenRepository, JwtTokenType.REFRESH_TOKEN);
                jwtService.setDeviceIdOnJwtToken(refreshToken, deviceId, refreshTokenRepository);
                Cookie cookieWithRefreshToken = jwtService.createCookieWithRefreshToken(refreshToken.getToken());

                httpServletResponse.addCookie(cookieWithRefreshToken);
                jwtService.addSameSiteCookieAttribute(httpServletResponse);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED);
            }

            return response;
        } catch (Exception exception) {
            LOGGER.error(ExceptionUtils.getStackTrace(exception));

            throw new InternalException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, INVALID_INPUT, exception.getMessage());
        }
    }

    public ResponseEntity logout(String authorization, String username) {
        return jwtService.logout(authorization, username);
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

    private boolean isEmailAlreadyInUse(String email) {
        Optional<User> userWithGivenEmail = userRepository.findUserByEmail(email);

        return userWithGivenEmail.isPresent();
    }
}
