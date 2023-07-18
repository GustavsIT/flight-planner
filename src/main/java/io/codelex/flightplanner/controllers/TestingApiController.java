package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.service.FlightPlannerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/testing-api")
public class TestingApiController {
    private final FlightPlannerService flightPlannerService;

    public TestingApiController(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }

    @PostMapping("/clear")
    public void clearFlights(){
        flightPlannerService.clearAllFlights();
    }
}