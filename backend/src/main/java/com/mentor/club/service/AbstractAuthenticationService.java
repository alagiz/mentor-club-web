package com.mentor.club.service;

import org.springframework.stereotype.Service;

import com.mentor.club.model.InternalResponse;
import com.mentor.club.model.PublicKeyResponse;

@Service
public abstract class AbstractAuthenticationService {

    public abstract InternalResponse authenticate(String username, String password);

    public PublicKeyResponse getPublicKey() {

        final PublicKeyResponse publicKeyResponse = new PublicKeyResponse();

        // get public key from docker volume
        // publicKeyResponse.setPublic_key(getProperty("PUBLIC_KEY"));

        return publicKeyResponse;
    }
}
