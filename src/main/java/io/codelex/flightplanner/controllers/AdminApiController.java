package io.codelex.flightplanner.controllers;


import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.exceptions.FlightNotFoundByIdException;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin-api")
public class AdminApiController {
    private final FlightService flightService;

    public AdminApiController(FlightService flightService) {
        this.flightService = flightService;
    }


    @PostMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFlight(@RequestBody AddFlightRequest request) {
        flightService.addFlight(request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFlight(@PathVariable long id) {
        try {
            flightService.deleteFlightById(id);
        } catch (FlightNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/flights/{id}")
    public Flight searchFlight(@PathVariable long id){
        return flightService.searchFlightById(id);
    }

}
