package com.mentor.club.repository;

import com.mentor.club.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IAccessTokenRepository extends JpaRepository<AccessToken, Long> {
    List<AccessToken> findByUserId(@Param("userId") UUID userId);

    Optional<AccessToken> findByToken(@Param("token") String token);
}