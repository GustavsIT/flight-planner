package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.service.FlightPlannerInMemoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/admin-api")
public class AdminApiController {
    private final FlightPlannerInMemoryService flightPlannerInMemoryService;

    public AdminApiController(FlightPlannerInMemoryService flightPlannerInMemoryService) {
        this.flightPlannerInMemoryService = flightPlannerInMemoryService;
    }

    @PutMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public Flight addFlight(@RequestBody @Valid AddFlightRequest addFlightRequest) {
        return flightPlannerInMemoryService.addFlight(addFlightRequest);
    }


    @GetMapping("/flights/{id}")
    public Flight getFlightById(@PathVariable long id) {
        Flight flight = flightPlannerInMemoryService.searchFlightById(id);
        if(flight==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return flight;
    }


    @DeleteMapping("/flights/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlight(@PathVariable long id) {
        flightPlannerInMemoryService.deleteFlightById(id);
    }


}


