package com.mentor.club.repository;

import com.mentor.club.model.authentication.token.PasswordResetToken;
import org.springframework.stereotype.Repository;

@Repository
public interface IPasswordResetTokenRepository extends IJwtTokenRepository<PasswordResetToken> {
}