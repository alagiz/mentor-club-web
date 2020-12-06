package com.mentor.club.repository;

import com.mentor.club.model.authentication.RefreshToken;
import org.springframework.stereotype.Repository;

@Repository
public interface IRefreshTokenRepository extends IJwtTokenRepository<RefreshToken> {
}