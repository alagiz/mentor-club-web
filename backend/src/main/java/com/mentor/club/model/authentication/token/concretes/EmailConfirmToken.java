package com.mentor.club.model.authentication.token.concretes;

import com.mentor.club.model.authentication.token.abstracts.JwtToken;
import com.mentor.club.model.authentication.token.enums.JwtTokenType;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class EmailConfirmToken extends JwtToken {
    public EmailConfirmToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
