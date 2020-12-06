package com.mentor.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface IJwtTokenRepository<T> extends JpaRepository<T, Long> {
    List<T> findByUserId(@Param("userId") UUID userId);

    Optional<T> findByToken(@Param("token") String token);
}