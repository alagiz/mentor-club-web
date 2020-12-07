package com.mentor.club.model.authentication.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken extends JwtToken {
    public RefreshToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
