package com.mentor.club.service;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mentor.club.model.ExtendableResult;
import com.mentor.club.model.InternalResponse;
import com.mentor.club.model.Token;
import com.mentor.club.model.User;
import com.mentor.club.repository.ITokenRepository;
import com.mentor.club.repository.IUserRepository;
import com.mentor.club.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.mentor.club.utils.RsaUtils.USERNAME_CLAIM;

@Service
public class JwtService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    static final String MESSAGE_CLAIM = "message";
    static final String USER_ID_CLAIM = "user_id";

    static final String ERROR_MESSAGE_INVALID_TOKEN = "Invalid token.";
    static final String ERROR_MESSAGE_SIGNATURE_VERIFICATION = "Signature verification exception.";

    static final String INFO_MESSAGE_VALID_JWT = "JWT is valid!";
    static final String INFO_MESSAGE_INVALIDATED_JWT = "JWT has been invalidated!";
    static final String INFO_MESSAGE_NON_WHITELIST_JWT = "JWT is not whitelisted!";

    private IUserRepository userRepository;
    private ITokenRepository tokenRepository;

    @Value("${backend.deployment.url}")
    private String backendDeploymentUrl;

    @Autowired
    public JwtService(IUserRepository userRepository, ITokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public ResponseEntity invalidateJWT(String authorization) {
        final InternalResponse authResponse = validateJWT(authorization, false, false);

        if (authResponse.getStatus() == HttpStatus.OK) {
            return new ResponseEntity<>(authResponse.getJson(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(authResponse.getJson(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity validateJWT(String authorization) {
        return getReturnResponse(authorization, false);
    }

    public ResponseEntity getUserIdFromJwt(String authorization) {
        return getReturnResponse(authorization, true);
    }

    public InternalResponse validateJWT(String authorization, Boolean includeUserIdInResponse, Boolean isValidation) {
        DecodedJWT decodedJWT = null;
        String errorMessage = "";
        String[] splitAuth = authorization.split(" ");
        String token = splitAuth[splitAuth.length - 1];

        try {
            decodedJWT = RsaUtils.decodeToken(token);
        } catch (JWTDecodeException exception) {
            errorMessage = ERROR_MESSAGE_INVALID_TOKEN;
        } catch (SignatureVerificationException ex) {
            errorMessage = ERROR_MESSAGE_SIGNATURE_VERIFICATION;
        }

        return validateOrInvalidateJWT(authorization, errorMessage, decodedJWT, isValidation, includeUserIdInResponse);
    }

    public InternalResponse validateOrInvalidateJWT(String authorization, String errorMessage, DecodedJWT decodedJWT, Boolean isValidation, Boolean includeUserIdInResponse) {
        final InternalResponse internalResponse = new InternalResponse();
        final ExtendableResult resultJson = new ExtendableResult();

        if (decodedJWT == null) {
            LOGGER.error(errorMessage);
            resultJson.getProperties().put(MESSAGE_CLAIM, errorMessage);
            internalResponse.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            if (isValidation) {
                if (isTokenWhitelisted(decodedJWT)) {
                    resultJson.getProperties().put(MESSAGE_CLAIM, INFO_MESSAGE_VALID_JWT);

                    if (includeUserIdInResponse) {
                        resultJson.getProperties().put(USER_ID_CLAIM, decodedJWT.getClaims().get(USERNAME_CLAIM).asString());
                    }

                    internalResponse.setStatus(HttpStatus.OK);
                } else {
                    resultJson.getProperties().put(MESSAGE_CLAIM, INFO_MESSAGE_NON_WHITELIST_JWT);
                    internalResponse.setStatus(HttpStatus.UNAUTHORIZED);
                }
            } else {
                logout(authorization, decodedJWT.getClaim("username").asString());
                resultJson.getProperties().put(MESSAGE_CLAIM, INFO_MESSAGE_INVALIDATED_JWT);
                internalResponse.setStatus(HttpStatus.OK);
            }
        }

        internalResponse.setJson(resultJson);

        return internalResponse;
    }

    public ResponseEntity logout(String authorization, String username) {
        boolean isActionAllowed = validateJWT(authorization).getStatusCode().is2xxSuccessful();

        if (!isActionAllowed) {
            LOGGER.error("Failed to logout user with username " + username + ". JWT is invalid");

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Optional<User> optionalUser = userRepository.findUserByUsername(username);

            if (optionalUser.isPresent()) {
                String userId = optionalUser.get().getId();
                List<Token> userTokens = tokenRepository.findByUserId(userId);

                userTokens.stream().forEach(token -> tokenRepository.delete(token));

                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                LOGGER.error("Failed to logout, no user with username " + username + " found!");

                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception exception) {
            LOGGER.error("Failed to logout user with username " + username + ". Error: " + exception.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isTokenWhitelisted(DecodedJWT decodedJWT) {
        String username = decodedJWT.getClaims().get(USERNAME_CLAIM).asString();

        try {
            Optional<User> optionalUser = userRepository.findUserByUsername(username);

            if (optionalUser.isPresent()) {
                List<Token> userTokens = tokenRepository.findByUserId(optionalUser.get().getId());

                boolean isTokenPresent = userTokens.contains(decodedJWT.getToken());
                boolean isTokenExpired = decodedJWT.getExpiresAt().after(Date.from(Instant.now()));

                if (isTokenPresent && isTokenExpired) {
                    removeTokenIfExpired(decodedJWT);

                    return false;
                }

                return isTokenPresent;
            } else {
                LOGGER.error("Failed to check token validity, user with username " + username + " not found!");

                return false;
            }
        } catch (Exception exception) {
            LOGGER.error("Failed to check token validity for user with username " + username + ". Error: " + exception.getMessage());

            return false;
        }
    }

    private void removeTokenIfExpired(DecodedJWT decodedJWT) {
        try {
            Optional<Token> optionalToken = tokenRepository.findByToken(decodedJWT.getToken());

            if (optionalToken.isPresent()) {
                tokenRepository.delete(optionalToken.get());
            }
        } catch (Exception exception) {
            String username = decodedJWT.getClaims().get(USERNAME_CLAIM).asString();

            LOGGER.error("Failed to remove token for user with username " + username + ". Error: " + exception.getMessage());
        }
    }

    private ResponseEntity getReturnResponse(String authorization, boolean includeUserIdInResponse) {
        final InternalResponse authResponse = validateJWT(authorization, includeUserIdInResponse, true);

        if (authResponse.getStatus() == HttpStatus.OK) {
            return new ResponseEntity<>(authResponse.getJson(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(authResponse.getJson(), HttpStatus.UNAUTHORIZED);
        }
    }

}
