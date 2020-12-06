package com.mentor.club.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mentor.club.exception.InternalException;
import com.mentor.club.model.error.HttpCallError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

public final class RsaUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsaUtils.class);

    public static final String USERNAME_CLAIM = "username";

    @Value("${pem.keys.path.public}")
    private static String publicKeyPath;

    @Value("${pem.keys.path.private}")
    private static String privateKeyPath;

    private static Key loadRSAPublicKey() {
        try {
            String publicKeyContent = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(publicKeyPath).toURI())));
            publicKeyContent = publicKeyContent.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));

            return keyFactory.generatePublic(keySpecX509);
        } catch (GeneralSecurityException | URISyntaxException | IOException exception) {
            LOGGER.error("Failed to load public key. Error: " + exception.getMessage());

            throw new InternalException(HttpStatus.INTERNAL_SERVER_ERROR, HttpCallError.READ_INPUT_STREAM);
        }
    }

    private static Key loadRSAPrivateKey() {
        try {
            String privateKeyContent = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(privateKeyPath).toURI())));
            privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));

            return keyFactory.generatePrivate(keySpecPKCS8);
        } catch (GeneralSecurityException | URISyntaxException | IOException exception) {
            LOGGER.error("Failed to load private key. Error: " + exception.getMessage());

            throw new InternalException(HttpStatus.INTERNAL_SERVER_ERROR, HttpCallError.READ_INPUT_STREAM);
        }
    }

    public static String generateToken(String username, List<String> group, Long tokenLifetime) {
        try {
            Algorithm algorithm = getRsaAlgorithm();
            Date expirationDate = Date.from(Instant.now().plusSeconds(tokenLifetime));

            return JWT.create()
                    .withIssuer("user microservice")
                    .withExpiresAt(expirationDate)
                    .withClaim(USERNAME_CLAIM, username)
                    .withClaim("group", group.toString())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            LOGGER.error("Error creating the JWT.");

            throw new InternalException(HttpStatus.INTERNAL_SERVER_ERROR, HttpCallError.FAILED_TO_CREATE_TOKEN);
        }
    }

    private static Algorithm getRsaAlgorithm() {
        RSAPublicKey publicKey = (RSAPublicKey) loadRSAPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) loadRSAPrivateKey();

        return Algorithm.RSA256(publicKey, privateKey);
    }

    public static DecodedJWT decodeToken(String token) throws JWTDecodeException, SignatureVerificationException {
        DecodedJWT jwt = JWT.decode(token);

        getRsaAlgorithm().verify(jwt);

        return jwt;
    }
}
