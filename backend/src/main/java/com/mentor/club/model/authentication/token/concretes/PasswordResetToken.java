package com.mentor.club.model.authentication.token.concretes;

import com.mentor.club.model.authentication.token.abstracts.JwtToken;
import com.mentor.club.model.authentication.token.enums.JwtTokenType;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class PasswordResetToken extends JwtToken {
    public PasswordResetToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
