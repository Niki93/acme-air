package com.acmeair.booking.services.impl;

import com.acmeair.booking.dto.FlightDataDto;
import com.acmeair.booking.dto.FlightListResponse;
import com.acmeair.booking.dto.FlightSearchListResponse;
import com.acmeair.booking.dto.FlightSearchQueryParameters;
import com.acmeair.booking.entities.Flight;
import com.acmeair.booking.repositories.FlightDataRepository;
import com.acmeair.booking.services.FlightService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for flight search related operations.
 */
@Transactional(readOnly = true)
@Slf4j
@Service
public class FlightServiceImpl implements FlightService {

    private final FlightDataRepository flightDataRepository;
    private final ModelMapper modelMapper;

    public FlightServiceImpl(FlightDataRepository flightDataRepository, ModelMapper modelMapper) {
        this.flightDataRepository = flightDataRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Finds a flight by its ID.
     *
     * @param flightId the ID of the flight to find
     * @return the flight information as a DTO
     * @throws EntityNotFoundException if the flight with the given ID is not found
     */
    @Override
    public FlightDataDto findFlightById(Long flightId) {
        log.info("Looking for flight with ID {}", flightId);
        Flight flight = flightDataRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found with ID: " + flightId));
        log.debug("Found flight: {}", flight);
        return this.modelMapper.map(flight, FlightDataDto.class);
    }

    /**
     * Finds available flights based on search criteria such as origin, destination, departure, and optional return dates.
     *
     * @param flightSearchQueryParameters the parameters used to search for flights
     * @return a response object containing both outbound and (optional) inbound flight data
     */
    @Override
    public FlightSearchListResponse findFlightsByCriterias(FlightSearchQueryParameters flightSearchQueryParameters){
        List<Flight> flightListOut = flightDataRepository.findByOriginAndDestinationAndExactDepartureDate
                (flightSearchQueryParameters.getOrigin(),flightSearchQueryParameters.getDestination(),
                        flightSearchQueryParameters.getDepartureDate());

        List<FlightDataDto> flightDataDtoListOut = flightListOut
                .stream()
                .map(flight -> this.modelMapper.map(flight, FlightDataDto.class))
                .toList();

        FlightListResponse outBoundResponse = new FlightListResponse(flightDataDtoListOut.size(), flightDataDtoListOut);

        FlightListResponse inBoundResponse = null;

        if(flightSearchQueryParameters.getReturnDate()!=null){
            List<Flight> flightListIn = flightDataRepository.findByOriginAndDestinationAndExactDepartureDate
                    (flightSearchQueryParameters.getDestination(),flightSearchQueryParameters.getOrigin(),
                            flightSearchQueryParameters.getReturnDate());

            List<FlightDataDto> flightDataDtoListIn = flightListIn
                    .stream()
                    .map(flight -> this.modelMapper.map(flight, FlightDataDto.class))
                    .toList();
            inBoundResponse = new FlightListResponse(flightDataDtoListIn.size(), flightDataDtoListIn);
        }

        return new FlightSearchListResponse(outBoundResponse, inBoundResponse);
    }

}
