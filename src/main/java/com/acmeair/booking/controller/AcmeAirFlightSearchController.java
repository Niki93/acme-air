package com.acmeair.booking.controller;

import com.acmeair.booking.dto.FlightDataDto;
import com.acmeair.booking.dto.FlightSearchListResponse;
import com.acmeair.booking.dto.FlightSearchQueryParameters;
import com.acmeair.booking.services.FlightService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.util.List;


@RestController
@RequestMapping("/acmeair")
@Slf4j
@Validated
public class AcmeAirFlightSearchController {

    private final FlightService flightService;

    public AcmeAirFlightSearchController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * Search for flights based on origin, destination, departure date and arrival date
     * */
    @GetMapping(value = "/flights/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlightSearchListResponse> searchFlights(@Valid FlightSearchQueryParameters flightSearchQueryParameters) {
        log.info(flightSearchQueryParameters.getOrigin());
        FlightSearchListResponse fullFlightList = flightService.findFlightsByCriterias(flightSearchQueryParameters);
        return ResponseEntity.ok(fullFlightList);
    }

    /**
     * Search for a flight by Id
     * */
    @GetMapping("/flights/{id}")
    public ResponseEntity<FlightDataDto> getFlightInformation(@PathVariable Long id) {
        FlightDataDto flightDataDto = flightService.findFlightById(id);
        return ResponseEntity.ok(flightDataDto);
    }
}
