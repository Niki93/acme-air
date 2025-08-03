package com.acmeair.booking.repositories;

import com.acmeair.booking.entities.Flight;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightDataRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByOriginAndDestinationAndDepartureDateBetween(
            String origin, String destination, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT f FROM Flight f " +
            "WHERE f.origin = ?1 AND f.destination = ?2 " +
            "AND CAST(f.departureDate AS date) = ?3 ")
    List<Flight> findByOriginAndDestinationAndExactDepartureDate(@NotBlank String origin, @NotBlank String destination, LocalDate departureDate);
}
