package com.acmeair.booking.services.impl;

import com.acmeair.booking.dto.FlightDataDto;
import com.acmeair.booking.dto.FlightSearchListResponse;
import com.acmeair.booking.dto.FlightSearchQueryParameters;
import com.acmeair.booking.entities.Flight;
import com.acmeair.booking.repositories.FlightDataRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlightServiceImplTest {

    private FlightDataRepository flightDataRepository;
    private ModelMapper modelMapper;
    private FlightServiceImpl flightService;

    @BeforeEach
    void setUp() {
        flightDataRepository = mock(FlightDataRepository.class);
        modelMapper = mock(ModelMapper.class);
        flightService = new FlightServiceImpl(flightDataRepository, modelMapper);
    }

    @Test
    void testFindFlightById_Found() {
        Flight flight = new Flight();
        flight.setFlightId(1L);
        flight.setOrigin("JFK");
        flight.setDestination("LAX");

        FlightDataDto flightDataDto = new FlightDataDto();
        flightDataDto.setFlightId(1L);

        when(flightDataRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(modelMapper.map(flight, FlightDataDto.class)).thenReturn(flightDataDto);

        FlightDataDto result = flightService.findFlightById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getFlightId());
        verify(flightDataRepository).findById(1L);
    }

    @Test
    void testFindFlightById_NotFound() {
        when(flightDataRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                flightService.findFlightById(99L));

        assertEquals("Flight not found with ID: 99", thrown.getMessage());
        verify(flightDataRepository).findById(99L);
    }

    @Test
    void testFindFlightsByCriterias_WithoutReturnDate() {
        FlightSearchQueryParameters params = new FlightSearchQueryParameters();
        params.setOrigin("JFK");
        params.setDestination("LAX");
        params.setDepartureDate(LocalDate.of(2025, 8, 5));

        Flight flight = new Flight();
        flight.setFlightId(1L);
        List<Flight> outboundFlights = List.of(flight);

        FlightDataDto flightDto = new FlightDataDto();
        flightDto.setFlightId(1L);

        when(flightDataRepository.findByOriginAndDestinationAndExactDepartureDate("JFK", "LAX", LocalDate.of(2025, 8, 5)))
                .thenReturn(outboundFlights);
        when(modelMapper.map(flight, FlightDataDto.class)).thenReturn(flightDto);

        FlightSearchListResponse response = flightService.findFlightsByCriterias(params);

        assertNotNull(response);
        assertEquals(1, response.getOutboundFlights().getTotalCount());
        assertNull(response.getInboundFlights());
    }

    @Test
    void testFindFlightsByCriterias_WithReturnDate() {
        FlightSearchQueryParameters params = new FlightSearchQueryParameters();
        params.setOrigin("JFK");
        params.setDestination("LAX");
        params.setDepartureDate(LocalDate.of(2025, 8, 5));
        params.setReturnDate(LocalDate.of(2025, 8, 10));

        Flight outboundFlight = new Flight();
        outboundFlight.setFlightId(1L);
        Flight returnFlight = new Flight();
        returnFlight.setFlightId(2L);

        FlightDataDto outboundDto = new FlightDataDto();
        outboundDto.setFlightId(1L);
        FlightDataDto returnDto = new FlightDataDto();
        returnDto.setFlightId(2L);

        when(flightDataRepository.findByOriginAndDestinationAndExactDepartureDate("JFK", "LAX", LocalDate.of(2025, 8, 5)))
                .thenReturn(List.of(outboundFlight));
        when(flightDataRepository.findByOriginAndDestinationAndExactDepartureDate("LAX", "JFK", LocalDate.of(2025, 8, 10)))
                .thenReturn(List.of(returnFlight));
        when(modelMapper.map(outboundFlight, FlightDataDto.class)).thenReturn(outboundDto);
        when(modelMapper.map(returnFlight, FlightDataDto.class)).thenReturn(returnDto);

        FlightSearchListResponse response = flightService.findFlightsByCriterias(params);

        assertNotNull(response);
        assertEquals(1, response.getOutboundFlights().getTotalCount());
        assertEquals(1, response.getInboundFlights().getTotalCount());
    }

}
