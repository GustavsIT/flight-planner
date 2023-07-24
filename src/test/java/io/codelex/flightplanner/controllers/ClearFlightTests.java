package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.controllers.TestingApiController;
import io.codelex.flightplanner.repository.FlightPlannerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClearFlightTests {

    @Autowired
    FlightPlannerRepository flightPlannerRepository;

    @Autowired
    TestingApiController testingApiController;

    @Test
    public void clearFlights(){
        testingApiController.clearFlights();
        Assertions.assertTrue(flightPlannerRepository.getFlights().isEmpty());
    }


}
