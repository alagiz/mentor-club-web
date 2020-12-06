package com.mentor.club.service;

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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

@Service
public class RsaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsaService.class);

    public static final String USERNAME_CLAIM = "username";

    @Value("${pem.keys.path.public}")
    private String publicKeyPath;

    @Value("${pem.keys.path.private}")
    private String privateKeyPath;

    private Key loadRSAPublicKey() {
        try {
            String publicKeyContent = new String(Files.readAllBytes(new File(publicKeyPath).toPath()));
            publicKeyContent = publicKeyContent
                    .replaceAll("\\n", "")
                    .replaceAll("\\r", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));

            return keyFactory.generatePublic(keySpecX509);
        } catch (GeneralSecurityException | IOException exception) {
            LOGGER.error("Failed to load public key. Error: " + exception.getMessage());

            throw new InternalException(HttpStatus.INTERNAL_SERVER_ERROR, HttpCallError.READ_INPUT_STREAM);
        }
    }

    private Key loadRSAPrivateKey() {
        try {
            String privateKeyContent = new String(Files.readAllBytes(new File(privateKeyPath).toPath()));
            privateKeyContent = privateKeyContent
                    .replaceAll("\\n", "")
                    .replaceAll("\\r", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));

            return keyFactory.generatePrivate(keySpecPKCS8);
        } catch (GeneralSecurityException | IOException exception) {
            LOGGER.error("Failed to load private key. Error: " + exception.getMessage());

            throw new InternalException(HttpStatus.INTERNAL_SERVER_ERROR, HttpCallError.READ_INPUT_STREAM);
        }
    }

    public String generateToken(String username, List<String> group, Long tokenLifetime) {
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

    private Algorithm getRsaAlgorithm() {
        RSAPublicKey publicKey = (RSAPublicKey) loadRSAPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) loadRSAPrivateKey();

        return Algorithm.RSA256(publicKey, privateKey);
    }

    public DecodedJWT decodeToken(String token) throws JWTDecodeException, SignatureVerificationException {
        DecodedJWT jwt = JWT.decode(token);

        getRsaAlgorithm().verify(jwt);

        return jwt;
    }
}
