package com.mentor.club.model.authentication.token.concretes;

import com.mentor.club.model.authentication.token.enums.JwtTokenType;
import com.mentor.club.model.authentication.token.abstracts.JwtTokenWithDeviceId;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class AccessToken extends JwtTokenWithDeviceId {
    public AccessToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
