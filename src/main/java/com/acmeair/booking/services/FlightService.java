package com.acmeair.booking.services;

import com.acmeair.booking.dto.FlightDataDto;
import com.acmeair.booking.dto.FlightSearchListResponse;
import com.acmeair.booking.dto.FlightSearchQueryParameters;

import java.util.List;
import java.util.Optional;

public interface FlightService {

   FlightDataDto findFlightById(Long flightId);

   FlightSearchListResponse findFlightsByCriterias(FlightSearchQueryParameters flightSearchQueryParameters);
}
