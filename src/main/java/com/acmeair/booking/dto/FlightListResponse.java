package com.acmeair.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Data
@AllArgsConstructor
public class FlightListResponse {

    private int totalCount;
    private List<FlightDataDto> flights;;
}
