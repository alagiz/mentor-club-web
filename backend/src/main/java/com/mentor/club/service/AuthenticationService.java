package com.mentor.club.service;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mentor.club.model.error.HttpCallError.INVALID_INPUT;

@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private IUserRepository userRepository;
    private ITokenRepository tokenRepository;

    @Autowired
    public AuthenticationService(IUserRepository userRepository, ITokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public ResponseEntity authenticate(AuthenticationRequest authentication) {
        final InternalResponse authResponse = authenticateWithCredentials(authentication);

        // convert internalResponse to authenticationResult

        return new ResponseEntity<>(authResponse.getJson(), HttpStatus.OK);
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
        List<String> userGroups =  Arrays.asList("user"); // change in the future
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
}
