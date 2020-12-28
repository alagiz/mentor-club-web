package com.mentor.club.service;

import com.mentor.club.model.InternalResponse;
import com.mentor.club.model.authentication.token.abstracts.JwtToken;
import com.mentor.club.model.authentication.token.concretes.PasswordResetToken;
import com.mentor.club.model.authentication.token.enums.JwtTokenLifetime;
import com.mentor.club.model.authentication.token.enums.JwtTokenType;
import com.mentor.club.model.password.ChangeForgottenPasswordRequest;
import com.mentor.club.model.password.ChangePasswordRequest;
import com.mentor.club.model.user.User;
import com.mentor.club.repository.IPasswordResetTokenRepository;
import com.mentor.club.repository.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordService.class);

    private IUserRepository userRepository;
    private AwsService awsService;
    private JwtService jwtService;
    private IPasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${backend.deployment.url}")
    private String backendDeploymentUrl;

    @Autowired
    public PasswordService(IUserRepository userRepository,
                           AwsService awsService,
                           JwtService jwtService,
                           IPasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.awsService = awsService;
        this.jwtService = jwtService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    Boolean isProvidedPasswordCorrect(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public ResponseEntity generateResetForgottenPasswordEmail(String email) {
        try {
            Optional<User> userWithGivenEmail = userRepository.findUserByEmail(email);

            if (!userWithGivenEmail.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            JwtToken passwordResetToken = createPasswordResetTokenForUser(userWithGivenEmail.get());
            String resetPasswordUrl = backendDeploymentUrl + "/user/change-password?token=" + passwordResetToken.getToken();
            HttpStatus passwordResetEmailSentStatusCode = awsService.sendPasswordResetEmail(resetPasswordUrl, userWithGivenEmail.get());

            LOGGER.debug("Status code of sending password reset email: " + passwordResetEmailSentStatusCode.toString());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.debug("Failed to generate resetForgottenPasswordEmail for user with email: " + email + ". Message: " + exception.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity changeForgottenPasswordNegativeFlow(ChangeForgottenPasswordRequest changeForgottenPasswordRequest) {
        try {
            Optional<PasswordResetToken> optionalPasswordResetToken = passwordResetTokenRepository.findByToken(changeForgottenPasswordRequest.getPasswordResetToken());

            if (!optionalPasswordResetToken.isPresent()) {
                return new ResponseEntity<>("Password reset token in not present in the db!", HttpStatus.UNAUTHORIZED);
            }

            if (optionalPasswordResetToken.get().isExpired()) {
                return new ResponseEntity<>("PasswordResetToken is expired!", HttpStatus.BAD_REQUEST);
            }

            PasswordResetToken passwordResetToken = optionalPasswordResetToken.get();
            User user = passwordResetToken.getUser();

            if (user == null) {
                return new ResponseEntity<>("User for password reset token not found!", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Could not validate password for token " + changeForgottenPasswordRequest.getPasswordResetToken() + "!Error: " + exception.getMessage());

            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity changeForgottenPasswordSuccessfulFlow(ChangeForgottenPasswordRequest changeForgottenPasswordRequest) {
        InternalResponse internalResponse = new InternalResponse();

        try {
            Optional<PasswordResetToken> optionalPasswordResetToken = passwordResetTokenRepository.findByToken(changeForgottenPasswordRequest.getPasswordResetToken());
            PasswordResetToken passwordResetToken = optionalPasswordResetToken.get();
            User user = passwordResetToken.getUser();

            user.setHashedPassword(hashPassword(changeForgottenPasswordRequest.getNewPassword()));
            userRepository.save(user);

            internalResponse.setJson("Successfully change forgotten password for user with username " + user.getUsername() + "!");
            internalResponse.setStatus(HttpStatus.OK);

            return new ResponseEntity<>(internalResponse.getJson(), internalResponse.getStatus());
        } catch (Exception exception) {
            internalResponse.setJson("Failed to change forgotten password for password token " + changeForgottenPasswordRequest.getPasswordResetToken() + ". Error: " + exception.getMessage());
            internalResponse.setStatus(HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(internalResponse.getJson(), internalResponse.getStatus());
        }
    }

    public ResponseEntity changeForgottenPassword(ChangeForgottenPasswordRequest changeForgottenPasswordRequest) {
        ResponseEntity responseEntity = changeForgottenPasswordNegativeFlow(changeForgottenPasswordRequest);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity;
        }

        return changeForgottenPasswordSuccessfulFlow(changeForgottenPasswordRequest);
    }

    private ResponseEntity changePasswordNegativeFlow(ChangePasswordRequest changePasswordRequest, String authorization) {
        try {
            // TODO check deviceId also
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
            boolean isPasswordCorrect = isProvidedPasswordCorrect(changePasswordRequest.getPassword(), userHashedPassword);

            if (!isPasswordCorrect) {
                internalResponse.setStatus(HttpStatus.UNAUTHORIZED);
                internalResponse.setJson("Incorrect password for user with username " + changePasswordRequest.getUsername() + "!");

                return new ResponseEntity<>(internalResponse.getJson(), internalResponse.getStatus());
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Could not authorize password change for user " + changePasswordRequest.getUsername() + "!Error: " + exception.getMessage());

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    private ResponseEntity changePasswordSuccessfulFlow(ChangePasswordRequest changePasswordRequest) {
        InternalResponse internalResponse = new InternalResponse();

        try {
            Optional<User> optionalUser = userRepository.findUserByUsername(changePasswordRequest.getUsername());
            User user = optionalUser.get();

            user.setHashedPassword(hashPassword(changePasswordRequest.getNewPassword()));
            userRepository.save(user);

            internalResponse.setJson("Successfully change password for user with username " + changePasswordRequest.getUsername() + "!");
            internalResponse.setStatus(HttpStatus.OK);

            return new ResponseEntity<>(internalResponse.getJson(), internalResponse.getStatus());
        } catch (Exception exception) {
            internalResponse.setJson("Failed to change password for user with username " + changePasswordRequest.getUsername() + ". Error: " + exception.getMessage());
            internalResponse.setStatus(HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(internalResponse.getJson(), internalResponse.getStatus());
        }
    }

    public ResponseEntity changePassword(ChangePasswordRequest changePasswordRequest, String authorization) {
        ResponseEntity responseEntity = changePasswordNegativeFlow(changePasswordRequest, authorization);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity;
        }

        return changePasswordSuccessfulFlow(changePasswordRequest);
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
