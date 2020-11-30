package com.mentor.club.repository;

import com.mentor.club.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByEmail(@Param("email") String email);

    Optional<User> findUserByUsername(@Param("username") String email);

    Optional<User> findById(UUID userId);
}