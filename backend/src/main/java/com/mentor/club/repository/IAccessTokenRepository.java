package com.mentor.club.repository;

import com.mentor.club.model.authentication.token.AccessToken;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccessTokenRepository extends IJwtTokenRepository<AccessToken> {
}