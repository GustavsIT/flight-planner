package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.service.FlightPlannerService;
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
    public Flight addFlight(@RequestBody AddFlightRequest addFlightRequest) {
        return flightPlannerService.addFlight(addFlightRequest);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable long id) {
        Flight flight = flightPlannerService.searchFlightById(id);
        if (flight == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Flight> deleteFlight(@PathVariable long id) {
        Flight flightToDelete = flightPlannerService.searchFlightById(id);
        if (flightToDelete == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        flightPlannerService.deleteFlightById(flightToDelete.getId());
        return new ResponseEntity<>(flightToDelete, HttpStatus.NO_CONTENT);
    }


}


