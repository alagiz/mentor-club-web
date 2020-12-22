package com.mentor.club.model.authentication.token.factories;

import com.mentor.club.model.authentication.token.abstracts.AbstractJwtTokenFactory;
import com.mentor.club.model.authentication.token.abstracts.JwtToken;
import com.mentor.club.model.authentication.token.abstracts.JwtTokenWithDeviceId;
import com.mentor.club.model.authentication.token.concretes.AccessToken;
import com.mentor.club.model.authentication.token.concretes.RefreshToken;
import com.mentor.club.model.authentication.token.enums.JwtTokenType;
import org.springframework.stereotype.Component;

@Component
public final class JwtWithDeviceIdTokenFactory extends AbstractJwtTokenFactory {
    @Override
    public JwtToken getJwtToken(JwtTokenType jwtTokenType) {
        return null;
    }

    @Override
    public JwtTokenWithDeviceId getJwtTokenWithDeviceId(JwtTokenType jwtTokenType) {
        JwtTokenWithDeviceId jwtToken;

        switch (jwtTokenType) {
            case REFRESH_TOKEN:
                jwtToken = new RefreshToken(jwtTokenType);
                break;
            case ACCESS_TOKEN:
                jwtToken = new AccessToken(jwtTokenType);
                break;
            default:
                throw new IllegalStateException("Unexpected value for jwtTokenType: " + jwtTokenType);
        }

        return jwtToken;
    }
}
