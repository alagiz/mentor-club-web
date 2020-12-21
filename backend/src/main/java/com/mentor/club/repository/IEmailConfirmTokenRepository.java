package com.mentor.club.repository;

import com.mentor.club.model.authentication.token.concretes.EmailConfirmToken;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmailConfirmTokenRepository extends IJwtTokenRepository<EmailConfirmToken> {
}