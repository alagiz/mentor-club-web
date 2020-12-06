package com.mentor.club.service;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mentor.club.exception.InternalException;
import com.mentor.club.model.ExtendableResult;
import com.mentor.club.model.InternalResponse;
import com.mentor.club.model.authentication.JwtToken;
import com.mentor.club.model.user.User;
import com.mentor.club.repository.IAccessTokenRepository;
import com.mentor.club.repository.IJwtTokenRepository;
import com.mentor.club.repository.IRefreshTokenRepository;
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
import java.util.stream.Collectors;

import static com.mentor.club.model.error.HttpCallError.FAILED_TO_FIND_TOKEN;
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
    private IAccessTokenRepository accessTokenRepository;
    private IRefreshTokenRepository refreshTokenRepository;

    @Value("${backend.deployment.url}")
    private String backendDeploymentUrl;

    @Autowired
    public JwtService(IUserRepository userRepository,
                      IAccessTokenRepository accessTokenRepository,
                      IRefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public ResponseEntity validateJWT(String authorization) {
        DecodedJWT decodedJWT = null;
        String errorMessage = "";
        String token = authorization.substring(authorization.lastIndexOf(" ") + 1);

        try {
            decodedJWT = RsaUtils.decodeToken(token);
        } catch (JWTDecodeException exception) {
            errorMessage = ERROR_MESSAGE_INVALID_TOKEN;
        } catch (SignatureVerificationException ex) {
            errorMessage = ERROR_MESSAGE_SIGNATURE_VERIFICATION;
        }

        InternalResponse authResponse = checkJwtToken(errorMessage, decodedJWT);

        if (authResponse.getStatus() == HttpStatus.OK) {
            return new ResponseEntity<>(authResponse.getJson(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(authResponse.getJson(), HttpStatus.UNAUTHORIZED);
        }
    }

    public InternalResponse checkJwtToken(String errorMessage, DecodedJWT decodedJWT) {
        final InternalResponse internalResponse = new InternalResponse();
        final ExtendableResult resultJson = new ExtendableResult();

        if (decodedJWT == null) {
            LOGGER.error(errorMessage);
            resultJson.getProperties().put(MESSAGE_CLAIM, errorMessage);
            internalResponse.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            if (isTokenWhitelisted(decodedJWT)) {
                resultJson.getProperties().put(MESSAGE_CLAIM, INFO_MESSAGE_VALID_JWT);
                resultJson.getProperties().put(USER_ID_CLAIM, decodedJWT.getClaims().get(USERNAME_CLAIM).asString());

                internalResponse.setStatus(HttpStatus.OK);
            } else {
                resultJson.getProperties().put(MESSAGE_CLAIM, INFO_MESSAGE_NON_WHITELIST_JWT);
                internalResponse.setStatus(HttpStatus.UNAUTHORIZED);
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
                User user = optionalUser.get();

                deleteAllJwtTokensForUser(user);

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
                List<JwtToken> userAccessTokens = accessTokenRepository.findByUserId(optionalUser.get().getId());

                boolean isTokenPresent = userAccessTokens
                        .stream()
                        .map(JwtToken::getToken)
                        .collect(Collectors.toList())
                        .contains(decodedJWT.getToken());
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
            Optional<JwtToken> optionalToken = accessTokenRepository.findByToken(decodedJWT.getToken());

            optionalToken.ifPresent(jwtToken -> accessTokenRepository.delete(jwtToken));
        } catch (Exception exception) {
            String username = decodedJWT.getClaims().get(USERNAME_CLAIM).asString();

            LOGGER.error("Failed to remove token for user with username " + username + ". Error: " + exception.getMessage());
        }
    }

    protected void deleteJwtTokenForUser(User user, IJwtTokenRepository repository, JwtToken jwtToken) {
        try {
            Optional<JwtToken> token = repository.findByToken(jwtToken.getToken());

            repository.delete(token.get());
        } catch (Exception exception) {
            LOGGER.error("Failed to remove token for user with username " + user.getUsername() + "!");
        }
    }

    private void deleteJwtTokensForUser(User user, IJwtTokenRepository repository) {
        try {
            List<JwtToken> tokensOfUser = repository.findByUserId(user.getId());

            tokensOfUser.forEach(jwtToken -> repository.delete(jwtToken));
        } catch (Exception exception) {
            LOGGER.error("Failed to remove all tokens for user with username " + user.getUsername() + "!");
        }
    }

    void deleteAllJwtTokensForUser(User user) {
        deleteJwtTokensForUser(user, accessTokenRepository);
        deleteJwtTokensForUser(user, refreshTokenRepository);
    }

    JwtToken getAccessTokenFromAuthorizationString(String authorization) {
        String token = authorization.substring(authorization.lastIndexOf(" ") + 1);

        try {
            Optional<JwtToken> jwtToken = accessTokenRepository.findByToken(token);

            if (!jwtToken.isPresent()) {
                throw new InternalException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, FAILED_TO_FIND_TOKEN);
            }

            return jwtToken.get();
        } catch (Exception exception) {
            LOGGER.error("Failed to get access token from authorization string " + authorization + "!");

            throw new InternalException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, FAILED_TO_FIND_TOKEN);
        }
    }
}
