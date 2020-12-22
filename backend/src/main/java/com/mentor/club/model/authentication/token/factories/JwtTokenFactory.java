package com.mentor.club.model.authentication.token.factories;

import com.mentor.club.model.authentication.token.abstracts.AbstractJwtTokenFactory;
import com.mentor.club.model.authentication.token.abstracts.JwtToken;
import com.mentor.club.model.authentication.token.abstracts.JwtTokenWithDeviceId;
import com.mentor.club.model.authentication.token.concretes.EmailConfirmToken;
import com.mentor.club.model.authentication.token.concretes.PasswordResetToken;
import com.mentor.club.model.authentication.token.enums.JwtTokenType;
import org.springframework.stereotype.Component;

@Component
public final class JwtTokenFactory extends AbstractJwtTokenFactory {
    @Override
    public JwtToken getJwtToken(JwtTokenType jwtTokenType) {
        JwtToken jwtToken;

        switch (jwtTokenType) {
            case PASSWORD_RESET_TOKEN:
                jwtToken = new PasswordResetToken(jwtTokenType);
                break;
            case EMAIL_CONFIRM_TOKEN:
                jwtToken = new EmailConfirmToken(jwtTokenType);
                break;
            default:
                throw new IllegalStateException("Unexpected value for jwtTokenType: " + jwtTokenType);
        }

        return jwtToken;
    }

    @Override
    public JwtTokenWithDeviceId getJwtTokenWithDeviceId(JwtTokenType jwtTokenType) {
        return null;
    }
}
