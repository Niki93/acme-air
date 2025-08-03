package com.acmeair.booking.configs;

import com.acmeair.booking.repositories.FlightDataRepository;
import com.acmeair.booking.services.FlightService;
import com.acmeair.booking.services.impl.FlightServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.acmeair.booking.*"})
public class FlightBookingConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public FlightService flightService(FlightDataRepository flightDataRepository, ModelMapper modelMapper){
        return new FlightServiceImpl(flightDataRepository, modelMapper);
    }
}
