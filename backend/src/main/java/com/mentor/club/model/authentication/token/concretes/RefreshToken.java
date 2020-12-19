package com.mentor.club.model.authentication.token.concretes;

import com.mentor.club.model.authentication.token.enums.JwtTokenType;
import com.mentor.club.model.authentication.token.abstracts.JwtTokenWithDeviceId;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class RefreshToken extends JwtTokenWithDeviceId {
    public RefreshToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
