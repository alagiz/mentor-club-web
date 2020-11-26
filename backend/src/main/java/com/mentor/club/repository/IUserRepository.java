package com.mentor.club.repository;

import com.mentor.club.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    List<User> findByOriginAndDestinationAndPriceGreaterThanEqualAndPriceLessThanEqualAndDepartureGreaterThanEqualAndDepartureLessThanEqual(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice,
            @Param("departureDateStart") Timestamp departureDateStart,
            @Param("departureDateEnd") Timestamp departureDateEnd
    );

    @Query("SELECT DISTINCT flight.origin FROM Flight flight")
    List<String> findDistinctOrigin();

    @Query("SELECT DISTINCT flight.destination FROM Flight flight")
    List<String> findDistinctDestination();

    @Transactional
    @Modifying
    @Query("DELETE FROM Flight flight WHERE flight.departure < :departureDate")
    void removeByDepartureLessThanEqual(@Param("departureDate") Timestamp departureDate);