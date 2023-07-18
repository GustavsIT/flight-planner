package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.service.FlightPlannerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Flight searchFlight(@PathVariable long id) {
        return flightPlannerService.searchFlightById(id).getFlight();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFlight(@PathVariable long id) {
        flightPlannerService.deleteFlightById(id);
    }

}
