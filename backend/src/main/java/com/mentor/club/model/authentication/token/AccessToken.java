package com.mentor.club.model.authentication.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class AccessToken extends JwtToken {
    public AccessToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
