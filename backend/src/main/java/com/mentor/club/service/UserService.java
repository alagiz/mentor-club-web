package com.mentor.club.service;

import com.google.gson.Gson;
import com.mentor.club.exception.InternalException;
import com.mentor.club.model.*;
import com.mentor.club.repository.IAccessTokenRepository;
import com.mentor.club.repository.IPasswordResetTokenRepository;
import com.mentor.club.repository.IUserRepository;
import com.mentor.club.utils.RsaUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static com.mentor.club.model.PasswordResetToken.PASSWORD_RESET_TOKEN_LIFESPAN_IN_SECONDS;
import static com.mentor.club.model.error.HttpCallError.INVALID_INPUT;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private IUserRepository userRepository;
    private IAccessTokenRepository tokenRepository;
    private AwsService awsService;
    private JwtService jwtService;
    private IPasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${backend.deployment.url}")
    private String backendDeploymentUrl;

    @Autowired
    public UserService(IUserRepository userRepository, IAccessTokenRepository tokenRepository, AwsService awsService, JwtService jwtService, IPasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.awsService = awsService;
        this.jwtService = jwtService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public ResponseEntity authenticate(AuthenticationRequest authentication) {
        final InternalResponse authResponse = authenticateWithCredentials(authentication);

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

    private InternalResponse authenticateWithCredentials(AuthenticationRequest authentication) {
        InternalResponse response = new InternalResponse();
        AuthenticationResult result = new AuthenticationResult();

        String username = authentication.getUsername();
        String password = authentication.getPassword();

        try {
            Optional<User> user = userRepository.findUserByUsername(username);

            if (!user.isPresent()) {
                LOGGER.error("User with username " + username + " not found!");

                response.setStatus(HttpStatus.NOT_FOUND);
                response.setJson("User with username " + username + " not found!");

                return response;
            }

            if (checkPassword(password, user.get().getHashedPassword())) {
                LOGGER.debug("Correct password for user with username " + username + "!");

                result.setUsername(username);
                result.setThumbnailPhoto(user.get().getThumbnailBase64());
                result.setToken(createToken(user));
                result.setDisplayName(user.get().getName());
                result.setThumbnailPhoto(user.get().getThumbnailBase64());

                response.setJson(result);
                response.setStatus(HttpStatus.OK);
            } else {
                LOGGER.error("Incorrect password for user with username " + username + "!");

                response.setJson("Incorrect password for user with username " + username + "!");
                response.setStatus(HttpStatus.UNAUTHORIZED);
            }

            return response;
        } catch (Exception exception) {
            LOGGER.error(ExceptionUtils.getStackTrace(exception));
            throw new InternalException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, INVALID_INPUT);
        }
    }

    public ResponseEntity getPublicKey() {
        return new ResponseEntity<>(getPublicKeyResponse(), HttpStatus.OK);
    }

    private String createToken(Optional<User> user) {
        List<String> userGroups = Arrays.asList("user"); // change in the future
        String jwtToken = RsaUtils.generateToken(user.get().getUsername(), userGroups);

        AccessToken accessToken = new AccessToken();

        accessToken.setToken(jwtToken);
        accessToken.setUserId(user.get().getId());

        tokenRepository.save(accessToken);

        return accessToken.getToken();
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

    public ResponseEntity resetPassword(String email) {
        Optional<User> userWithGivenEmail = userRepository.findUserByEmail(email);

        if (!userWithGivenEmail.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PasswordResetToken passwordResetToken = createPasswordResetTokenForUser(userWithGivenEmail.get());
        String resetPasswordUrl = backendDeploymentUrl + "/user/change-password?token=" + passwordResetToken.getToken();
        HttpStatus passwordResetEmailSentStatusCode = awsService.sendPasswordResetEmail(resetPasswordUrl, userWithGivenEmail.get());

        LOGGER.debug("Status code of sending password reset email: " + passwordResetEmailSentStatusCode.toString());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity changeForgottenPassword(ChangeForgottenPasswordRequest changeForgottenPasswordRequest) {
        ResponseEntity responseEntity = validatePasswordResetToken(changeForgottenPasswordRequest.getPasswordResetToken());

        return responseEntity;
    }

    public ResponseEntity changePassword(ChangePasswordRequest changePasswordRequest, String authorization) {
        ResponseEntity jwtValidationResult = jwtService.validateJWT(authorization);
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

        if (isTokenExpired(passwordResetToken.get())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isTokenExpired(PasswordResetToken passwordResetToken) {
        Calendar cal = Calendar.getInstance();

        return passwordResetToken.getExpirationDate().before(cal.getTime());
    }

    private PasswordResetToken createPasswordResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = new PasswordResetToken();

        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpirationDate(Date.from(Instant.now().plusSeconds(PASSWORD_RESET_TOKEN_LIFESPAN_IN_SECONDS)));

        return passwordResetTokenRepository.save(passwordResetToken);
    }
}
