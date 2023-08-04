package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.service.FlightPlannerInMemoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/testing-api")
public class TestingApiController {
    private final FlightPlannerInMemoryService flightPlannerInMemoryService;

    public TestingApiController(FlightPlannerInMemoryService flightPlannerInMemoryService) {
        this.flightPlannerInMemoryService = flightPlannerInMemoryService;
    }

    @PostMapping("/clear")
    public void clearFlights(){
        flightPlannerInMemoryService.clearAllFlights();
    }
}