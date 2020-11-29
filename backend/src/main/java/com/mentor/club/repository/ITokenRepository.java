package com.mentor.club.repository;

import com.mentor.club.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUserId(@Param("userId") String userId);

    Optional<Token> findByToken(@Param("token") String token);
}