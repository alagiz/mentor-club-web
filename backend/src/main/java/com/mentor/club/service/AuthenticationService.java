package com.mentor.club.service;

import com.mentor.club.model.AuthenticationRequest;
import com.mentor.club.model.InternalResponse;
import com.mentor.club.model.PublicKeyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public ResponseEntity authenticate(AuthenticationRequest authentication) {
        final InternalResponse authResponse = authenticateWithCredentials(authentication);
        if (authResponse.getStatus() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(authResponse.getJson(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(authResponse.getJson(), HttpStatus.UNAUTHORIZED);
        }
    }

    private InternalResponse authenticateWithCredentials(AuthenticationRequest authentication) {
        final InternalResponse response = new InternalResponse();
        String username = authentication.getUsername();
        String password = authentication.getPassword();

        // implement authentication with postgres

        return response;
    }

    public ResponseEntity getPublicKey() {
        return new ResponseEntity<>(getPublicKeyResponse(), HttpStatus.OK);
    }

    public PublicKeyResponse getPublicKeyResponse() {

        final PublicKeyResponse publicKeyResponse = new PublicKeyResponse();

        // get public key from docker volume
        // publicKeyResponse.setPublic_key(getProperty("PUBLIC_KEY"));

        return publicKeyResponse;
    }
}
