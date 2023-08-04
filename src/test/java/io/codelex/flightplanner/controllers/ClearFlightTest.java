package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.repository.FlightPlannerInMemoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class ClearFlightTest {
    @Autowired
    FlightPlannerInMemoryRepository flightPlannerInMemoryRepository;

    @Autowired
    TestingApiController testingApiController;

    @Test
    public void clearFlights(){
        testingApiController.clearFlights();
        Assertions.assertTrue(flightPlannerInMemoryRepository.getFlights().isEmpty());
    }
}
