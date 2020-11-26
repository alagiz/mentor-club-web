package com.mentor.club.utils;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class RsaUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsaUtils.class);

    public static final Long TOKEN_LIFESPAN = 4L * 3600L;
    public static final String USERNAME_CLAIM = "username";

    private RsaUtils() {
    }

    private static Key loadRSAPublicKey(String stored) throws GeneralSecurityException {
        final byte[] data = Base64.getDecoder().decode(stored.getBytes());
        final X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        final KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);
    }

    private static Key loadRSAPrivateKey(String stored) throws GeneralSecurityException {
        final byte[] data = Base64.getDecoder().decode(stored.getBytes());
        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(data);
        final KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePrivate(spec);
    }

    public static String generateToken(String username, List<String> group) {
        try {
            final Algorithm algorithm = getRsaAlgorithm();

            final Date expirationDate = Date.from(Instant.now().plusSeconds(TOKEN_LIFESPAN));

            return JWT.create()
                    .withIssuer("auth microservice")
                    .withExpiresAt(expirationDate)
                    .withClaim(USERNAME_CLAIM, username)
                    .withClaim("group", group.toString())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            LOGGER.error("Error creating the JWT.");
        } catch (GeneralSecurityException gse) {
            LOGGER.error("Could not create the crypto algorithm related needed data.");
        }
        return null;
    }

    private static Algorithm getRsaAlgorithm() throws GeneralSecurityException {
        // get PUBLIC_KEY and PRIVATE_KEY from docker volume

        // final RSAPublicKey publicKey = (RSAPublicKey) loadRSAPublicKey(PropertiesManager.getProperty("PUBLIC_KEY"));
        // final RSAPrivateKey privateKey = (RSAPrivateKey) loadRSAPrivateKey(PropertiesManager.getProperty("PRIVATE_KEY"));

        // return Algorithm.RSA256(publicKey, privateKey);
    }

    public static DecodedJWT decodeToken(String token) throws JWTDecodeException, GeneralSecurityException, SignatureVerificationException {
        final DecodedJWT jwt = JWT.decode(token);
        getRsaAlgorithm().verify(jwt);
        return jwt;
    }
}
