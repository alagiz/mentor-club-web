package com.mentor.club.model.authentication.token;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class RefreshToken extends JwtTokenWithDeviceId {
    public RefreshToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
