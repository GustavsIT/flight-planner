package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.service.FlightPlannerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/admin-api")
public class AdminApiController {
    private final FlightPlannerService flightPlannerService;

    public AdminApiController(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }

    @PutMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public Flight addFlight(@RequestBody @Valid AddFlightRequest addFlightRequest) {
        return flightPlannerService.addFlight(addFlightRequest);
    }


    @GetMapping("/flights/{id}")
    public Flight getFlightById(@PathVariable long id) {
        Flight flight = flightPlannerService.searchFlightById(id);
        if(flight==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return flight;
    }


    @DeleteMapping("/flights/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlight(@PathVariable long id) {
        flightPlannerService.deleteFlightById(id);
    }


}


