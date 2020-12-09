package com.mentor.club.model.authentication.token;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class PasswordResetToken extends JwtToken {
    public PasswordResetToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
