package com.mentor.club.repository;

import com.mentor.club.model.authentication.token.concretes.AccessToken;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccessTokenRepository extends IJwtTokenWithDeviceIdRepository<AccessToken> {
}