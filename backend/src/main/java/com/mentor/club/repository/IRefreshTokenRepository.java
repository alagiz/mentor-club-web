package com.mentor.club.repository;

import com.mentor.club.model.authentication.token.concretes.RefreshToken;
import org.springframework.stereotype.Repository;

@Repository
public interface IRefreshTokenRepository extends IJwtTokenWithDeviceIdRepository<RefreshToken> {
}