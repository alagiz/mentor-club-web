package com.mentor.club.model.authentication.token.abstracts;

import com.mentor.club.model.authentication.token.enums.JwtTokenType;

public abstract class AbstractJwtTokenFactory {
    public abstract JwtToken getJwtToken(JwtTokenType jwtTokenType);
    public abstract JwtTokenWithDeviceId getJwtTokenWithDeviceId(JwtTokenType jwtTokenType);
}