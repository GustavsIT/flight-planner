package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.requests.SearchFlightRequest;
import io.codelex.flightplanner.responses.PageResult;
import io.codelex.flightplanner.service.FlightPlannerService;
import org.springframework.web.bind.annotation.*;
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
    public PageResult<Flight> searchFlights(SearchFlightRequest searchFlightRequest) {
        return flightPlannerService.searchFlights(searchFlightRequest);
    }


    @GetMapping("/flights/{id}")
    public Flight getFlightById(@PathVariable long id) {
        return flightPlannerService.searchFlightById(id).getFlight();
    }

}

