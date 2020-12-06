package com.mentor.club.model.authentication;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class AccessToken extends JwtToken {
    public AccessToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
