package com.acmeair.booking.controller;

import com.acmeair.booking.dto.FlightDataDto;
import com.acmeair.booking.dto.FlightListResponse;
import com.acmeair.booking.dto.FlightSearchListResponse;
import com.acmeair.booking.dto.FlightSearchQueryParameters;
import com.acmeair.booking.services.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AcmeAirFlightSearchController.class)
@AutoConfigureMockMvc
public class AcmeAirFlightSearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnFlightsMatchingSearchCriterias() throws Exception {
        FlightSearchQueryParameters query = new FlightSearchQueryParameters();
        query.setOrigin("JFK");
        query.setDestination("LAX");
        query.setDepartureDate(LocalDate.of(2025, 8, 5));
        query.setReturnDate(LocalDate.of(2025, 8, 6));

        FlightSearchListResponse response = getFlightSearchListResponse();

        Mockito.when(flightService.findFlightsByCriterias(Mockito.any()))
                .thenReturn(response);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/acmeair/flights/search")
                        .param("origin", "JFK")
                        .param("destination", "LAX")
                        .param("departureDate", "2025-08-05")
                        .param("arrivalDate", "2025-08-06")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outboundFlights.totalCount").value(1))
                .andExpect(jsonPath("$.outboundFlights.flights[0].flightNumber").value("AC123"))
                .andExpect(jsonPath("$.outboundFlights.flights[0].origin").value("JFK"))
                .andExpect(jsonPath("$.outboundFlights.flights[0].destination").value("LAX"))
                .andExpect(jsonPath("$.inboundFlights.totalCount").value(1))
                .andExpect(jsonPath("$.inboundFlights.flights[0].flightNumber").value("AC124"))
                .andExpect(jsonPath("$.inboundFlights.flights[0].origin").value("LAX"))
                .andExpect(jsonPath("$.inboundFlights.flights[0].destination").value("JFK"));
    }

    @Test
    void shouldReturnValidationErrorForMissingRequiredParams() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/acmeair/flights/search")
                        .param("destination", "LAX"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.origin").value("must not be blank"))
                .andExpect(jsonPath("$.departureDate").value("Please specify a Departure Date"));
    }

    @Test
    void shouldReturnFlightById() throws Exception {
        // Given
        Long flightId = 1L;
        FlightDataDto dto = new FlightDataDto(flightId, "AC123", "JFK", "LAX",
                LocalDateTime.of(2025, 8, 5, 10, 0),
                LocalDateTime.of(2025, 8, 6, 13, 0),
                50);

        Mockito.when(flightService.findFlightById(flightId)).thenReturn(dto);

        // When + Then
        mockMvc.perform(MockMvcRequestBuilders.get("/acmeair/flights/{id}", flightId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("AC123"))
                .andExpect(jsonPath("$.origin").value("JFK"))
                .andExpect(jsonPath("$.destination").value("LAX"));
    }

    @Test
    void shouldReturn404WhenFlightNotFoundById() throws Exception {
        long flightId = 99L;

        Mockito.when(flightService.findFlightById(flightId))
                .thenThrow(new EntityNotFoundException("Flight not found with ID: "+ flightId));

        mockMvc.perform(MockMvcRequestBuilders.get("/acmeair/flights/{id}", flightId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.detail").value("Flight not found with ID: 99"))
                .andExpect(jsonPath("$.instance").value("/acmeair/flights/99"));
    }

    private static FlightSearchListResponse getFlightSearchListResponse() {
        FlightDataDto outBoundDto = new FlightDataDto(1L, "AC123", "JFK", "LAX",
                LocalDateTime.of(2025, 8, 5, 10, 0),
                LocalDateTime.of(2025, 8, 5, 13, 0),
                50);

        FlightDataDto inBoundDto = new FlightDataDto(1L, "AC124", "LAX", "JFK",
                LocalDateTime.of(2025, 8, 10, 13, 0),
                LocalDateTime.of(2025, 8, 10, 16, 0),
                50);

        FlightListResponse outBoundList = new FlightListResponse(1, List.of(outBoundDto));

        FlightListResponse inBoundList = new FlightListResponse(1, List.of(inBoundDto));

        return new FlightSearchListResponse(outBoundList, inBoundList);
    }
}
