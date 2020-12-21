package com.mentor.club.model.authentication.token.factories;

import com.mentor.club.model.authentication.token.abstracts.JwtTokenWithDeviceId;
import com.mentor.club.model.authentication.token.concretes.AccessToken;
import com.mentor.club.model.authentication.token.concretes.RefreshToken;
import com.mentor.club.model.authentication.token.enums.JwtTokenType;

public final class JwtWithDeviceIdTokenFactory {
    public static JwtTokenWithDeviceId createJwtTokenWithDeviceIdOfType(JwtTokenType jwtTokenType) {
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
