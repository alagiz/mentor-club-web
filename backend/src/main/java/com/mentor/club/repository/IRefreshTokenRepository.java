package com.mentor.club.repository;

import com.mentor.club.model.authentication.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRefreshTokenRepository extends JpaRepository<JwtToken, Long> {
    List<JwtToken> findByUserId(@Param("userId") UUID userId);

    Optional<JwtToken> findByToken(@Param("token") String token);
}