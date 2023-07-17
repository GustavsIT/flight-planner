package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.exceptions.FlightNotFoundByIdException;
import io.codelex.flightplanner.requests.SearchFlightRequest;
import io.codelex.flightplanner.responses.PageResultResponse;
import io.codelex.flightplanner.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api")
public class CustomerApiController {
    private final FlightService flightService;

    public CustomerApiController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/flights/search")
    public ResponseEntity<PageResultResponse<Flight>> searchFlights(@RequestParam String from,
                                                                    @RequestParam String to,
                                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime departureTime) {
        SearchFlightRequest request = new SearchFlightRequest(from, to, departureTime);
        PageResultResponse<Flight> result = flightService.searchFlights(request);
        return ResponseEntity.ok(result);
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
    public ResponseEntity<PageResultResponse<Airport>> searchAirports (@RequestParam String request){
        PageResultResponse<Airport> result = flightService.searchAirports(request);
        return ResponseEntity.ok(result);
    }



}

