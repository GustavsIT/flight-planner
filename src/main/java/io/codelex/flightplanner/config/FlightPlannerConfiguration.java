package io.codelex.flightplanner.config;

import io.codelex.flightplanner.repository.AirportRepository;
import io.codelex.flightplanner.repository.FlightPlannerInMemoryRepository;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.service.FlightPlannerInDatabaseService;
import io.codelex.flightplanner.service.FlightPlannerInMemoryService;
import io.codelex.flightplanner.service.FlightPlannerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlightPlannerConfiguration {
    @Bean
    @ConditionalOnProperty(prefix ="flightplanner", name="service.version", havingValue = "in-memory")
    public FlightPlannerService getInMemoryVersion(FlightPlannerInMemoryRepository flightPlannerInMemoryRepository){
        return new FlightPlannerInMemoryService(flightPlannerInMemoryRepository);
    }


    @Bean
    @ConditionalOnProperty(prefix = "flightplanner", name = "service.version", havingValue = "database")
    public FlightPlannerService getDatabaseVersion(FlightRepository flightRepository, AirportRepository airportRepository){
        return new FlightPlannerInDatabaseService(flightRepository, airportRepository);
    }
}
