package com.mentor.club.model.authentication.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken extends JwtToken {
    public PasswordResetToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
