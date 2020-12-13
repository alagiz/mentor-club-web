package com.mentor.club.repository;

import com.mentor.club.model.authentication.token.RefreshToken;
import com.mentor.club.model.user.User;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface IJwtTokenWithDeviceIdRepository<T> extends IJwtTokenRepository<T> {
    Optional<RefreshToken> findByTokenAndDeviceId(@Param("token") String token, @Param("deviceId") UUID deviceId);

    List<RefreshToken> findByUserAndDeviceId(@Param("user") User user, @Param("deviceId") UUID deviceId);
}