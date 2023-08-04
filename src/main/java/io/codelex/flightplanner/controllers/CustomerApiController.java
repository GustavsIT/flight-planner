package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.requests.SearchFlightRequest;
import io.codelex.flightplanner.responses.PageResult;
import io.codelex.flightplanner.service.FlightPlannerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api")
public class CustomerApiController {
    private final FlightPlannerService flightPlannerService;

    public CustomerApiController(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }

    @GetMapping("/airports")
    public List<Airport> searchAirports(@RequestParam("search") String search) {
        return flightPlannerService.searchAirport(search);
    }

    @PostMapping("/flights/search")
    public PageResult<Flight> searchFlights(@RequestBody @Valid SearchFlightRequest searchFlightRequest) {
        return flightPlannerService.searchFlights(searchFlightRequest);
    }


    @GetMapping("/flights/{id}")
    public Flight getFlightById(@PathVariable long id) {
        Flight flight = flightPlannerService.searchFlightById(id);
        if(flight==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return flight;
    }

}

