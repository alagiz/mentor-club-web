package com.mentor.club.model.authentication.token;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class AccessToken extends JwtTokenWithDeviceId {
    public AccessToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
