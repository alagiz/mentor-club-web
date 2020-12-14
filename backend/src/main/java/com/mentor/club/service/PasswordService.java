package com.mentor.club.service;

import com.mentor.club.model.InternalResponse;
import com.mentor.club.model.authentication.token.JwtToken;
import com.mentor.club.model.authentication.token.JwtTokenLifetime;
import com.mentor.club.model.authentication.token.JwtTokenType;
import com.mentor.club.model.authentication.token.PasswordResetToken;
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

    private ResponseEntity changePasswordNegativeFlow(ChangePasswordRequest changePasswordRequest, String authorization) {
        // TODO add deviceId check
        try {
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

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
