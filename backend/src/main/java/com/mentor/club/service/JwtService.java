package com.mentor.club.service;

import com.mentor.club.model.ExtendableResult;
import com.mentor.club.model.InternalResponse;
import com.mentor.club.utils.RsaUtils;
import com.mentor.club.utils.WhitelistManager;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;

import static com.mentor.club.utils.RsaUtils.USERNAME_CLAIM;

@Service
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    static final String MESSAGE_CLAIM = "message";
    static final String USER_ID_CLAIM = "user_id";

    static final String ERROR_MESSAGE_INVALID_TOKEN = "Invalid token.";
    static final String ERROR_MESSAGE_CRYPTO_ALGORITHM = "Could not create the crypto algorithm related needed data.";
    static final String ERROR_MESSAGE_SIGNATURE_VERIFICATION = "Signature verification exception.";

    static final String INFO_MESSAGE_VALID_JWT = "JWT is valid!";
    static final String INFO_MESSAGE_INVALIDATED_JWT = "JWT has been invalidated!";
    static final String INFO_MESSAGE_NON_WHITELIST_JWT = "JWT is not whitelisted!";

    public InternalResponse validateJWT(String token, boolean includeUserIdInResponse) {
        final InternalResponse internalResponse = new InternalResponse();
        DecodedJWT decodedJWT = null;
        String errorMessage = "";
        try {
            decodedJWT = RsaUtils.decodeToken(token);
        } catch (JWTDecodeException exception) {
            errorMessage = ERROR_MESSAGE_INVALID_TOKEN;
        } catch (GeneralSecurityException e) {
            errorMessage = ERROR_MESSAGE_CRYPTO_ALGORITHM;
        } catch (SignatureVerificationException ex) {
            errorMessage = ERROR_MESSAGE_SIGNATURE_VERIFICATION;
        }

        final ExtendableResult resultJson = new ExtendableResult();
        if (decodedJWT == null) {
            LOGGER.error(errorMessage);
            resultJson.getProperties().put(MESSAGE_CLAIM, errorMessage);

            internalResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            if (WhitelistManager.isTokenWhitelisted(decodedJWT)) {
                resultJson.getProperties().put(MESSAGE_CLAIM, INFO_MESSAGE_VALID_JWT);

                if (includeUserIdInResponse) {
                    resultJson.getProperties().put(USER_ID_CLAIM, decodedJWT.getClaims().get(USERNAME_CLAIM).asString());
                }

                internalResponse.setStatus(HttpStatus.OK.value());
            } else {
                resultJson.getProperties().put(MESSAGE_CLAIM, INFO_MESSAGE_NON_WHITELIST_JWT);
                internalResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }

        internalResponse.setJson(resultJson);

        return internalResponse;
    }

    public InternalResponse invalidateJWT(String token) {
        final InternalResponse internalResponse = new InternalResponse();
        DecodedJWT decodedJWT = null;
        String errorMessage = "";

        try {
            decodedJWT = RsaUtils.decodeToken(token);
        } catch (JWTDecodeException exception) {
            errorMessage = ERROR_MESSAGE_INVALID_TOKEN;
        } catch (GeneralSecurityException e) {
            errorMessage = ERROR_MESSAGE_CRYPTO_ALGORITHM;
        } catch (SignatureVerificationException ex) {
            errorMessage = ERROR_MESSAGE_SIGNATURE_VERIFICATION;
        }
        final ExtendableResult resultJson = new ExtendableResult();

        if (decodedJWT == null) {
            LOGGER.error(errorMessage);
            resultJson.getProperties().put(MESSAGE_CLAIM, errorMessage);
            internalResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        } else {
            WhitelistManager.logout(decodedJWT);
            resultJson.getProperties().put(MESSAGE_CLAIM, INFO_MESSAGE_INVALIDATED_JWT);
            internalResponse.setStatus(HttpStatus.OK.value());
        }

        internalResponse.setJson(resultJson);

        return internalResponse;
    }

}
