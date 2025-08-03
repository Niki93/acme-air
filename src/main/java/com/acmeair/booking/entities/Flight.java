package com.acmeair.booking.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Long flightId;

    @Column(name = "flight_number")
    private String flightNumber;

    private String origin;
    private String destination;

    @Column(name = "departure_date_time")
    private LocalDateTime departureDate;

    @Column(name = "arrival_date_time")
    private LocalDateTime arrivalDate;

    @Column(name = "available_seats")
    private int availableSeats;

}
