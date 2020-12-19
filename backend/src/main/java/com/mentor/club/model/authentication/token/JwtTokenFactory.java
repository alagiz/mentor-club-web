package com.mentor.club.model.authentication.token;

import com.mentor.club.model.authentication.token.abstracts.JwtToken;
import com.mentor.club.model.authentication.token.concretes.AccessToken;
import com.mentor.club.model.authentication.token.concretes.PasswordResetToken;
import com.mentor.club.model.authentication.token.concretes.RefreshToken;
import com.mentor.club.model.authentication.token.enums.JwtTokenType;

public final class JwtTokenFactory {
    public static JwtToken createJwtTokenOfType(JwtTokenType jwtTokenType) {
        JwtToken jwtToken;

        switch (jwtTokenType) {
            case REFRESH_TOKEN:
                jwtToken = new RefreshToken(jwtTokenType);
                break;
            case ACCESS_TOKEN:
                jwtToken = new AccessToken(jwtTokenType);
                break;
            case PASSWORD_RESET_TOKEN:
                jwtToken = new PasswordResetToken(jwtTokenType);
                break;
            default:
                throw new IllegalStateException("Unexpected value for jwtTokenType: " + jwtTokenType);
        }

        return jwtToken;
    }
}
