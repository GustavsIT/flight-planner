package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.exceptions.FlightNotFoundByIdException;
import io.codelex.flightplanner.responses.PageResultResponse;
import io.codelex.flightplanner.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api")
public class CustomerApiController {
    private final FlightService flightService;

    public CustomerApiController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/flights/search")
    public ResponseEntity<PageResultResponse<Flight>> searchFlights() {
        PageResultResponse<Flight> flights = flightService.searchFlights();
        return ResponseEntity.ok(flights);
    }


    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable long id) {
        try {
            Flight flight = flightService.searchFlightById(id);
            return ResponseEntity.ok(flight);
        } catch (FlightNotFoundByIdException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/airports")
    public ResponseEntity<PageResultResponse<Airport>> searchAirports() {
        PageResultResponse<Airport> airports = flightService.searchAirports();
        return ResponseEntity.ok(airports);
    }



}

