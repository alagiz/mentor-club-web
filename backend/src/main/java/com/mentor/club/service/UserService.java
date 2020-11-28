package com.mentor.club.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mentor.club.exception.InternalException;
import com.mentor.club.model.*;
import com.mentor.club.repository.ITokenRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mentor.club.model.error.HttpCallError.INVALID_INPUT;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private IUserRepository userRepository;
    private ITokenRepository tokenRepository;
    private AwsService awsService;
    private JwtService jwtService;

    @Value("${backend.deployment.url}")
    private String backendDeploymentUrl;

    @Autowired
    public UserService(IUserRepository userRepository, ITokenRepository tokenRepository, AwsService awsService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.awsService = awsService;
        this.jwtService = jwtService;
    }

    public ResponseEntity authenticate(AuthenticationRequest authentication) {
        final InternalResponse authResponse = authenticateWithCredentials(authentication);

        return new ResponseEntity<>(authResponse.getJson(), HttpStatus.OK);
    }

    public ResponseEntity createNewUser(NewUser newUser) {
        User user = new User();
        user.setEmail(newUser.getEmail());
        user.setUsername(newUser.getUsername());
        user.setHashedPassword(hashPassword(newUser.getPassword()));
        user.setName(newUser.getName());
        user.setThumbnailBase64(newUser.getThumbnailBase64());

        try {
            User createdUser = userRepository.save(user);

            String confirmationUrl = backendDeploymentUrl + "/user/confirm-email/" + createdUser.getId();

            HttpStatus confirmationEmailSentStatusCode = awsService.sendConfirmationEmail(confirmationUrl, user.getEmail());

            LOGGER.error("Status code of sending confirmation email: " + confirmationEmailSentStatusCode.toString());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Failed to create user with username " + newUser.getUsername() + ". Error: " + exception.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity confirmEmail(String userId) {
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
                LOGGER.error("Correct password for user with username " + username + "!");

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

        Token token = new Token();

        token.setToken(jwtToken);
        token.setUserId(user.get().getId());

        tokenRepository.save(token);

        return token.getToken();
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
}
