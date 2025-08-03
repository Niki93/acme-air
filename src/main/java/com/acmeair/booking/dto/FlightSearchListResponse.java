package com.acmeair.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightSearchListResponse {

    private FlightListResponse outboundFlights;

    private FlightListResponse inboundFlights;


}
