package com.mentor.club.model.authentication;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class PasswordResetToken extends JwtToken {
    public PasswordResetToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
