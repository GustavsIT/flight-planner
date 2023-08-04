package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.requests.SearchFlightRequest;
import io.codelex.flightplanner.responses.PageResult;

import java.util.List;

public interface FlightPlannerService {

    void clearAllFlights();

    Flight addFlight(AddFlightRequest addFlightRequest);

    List<Airport> searchAirport(String search);

    PageResult<Flight> searchFlights(SearchFlightRequest searchFlightRequest);

    Flight searchFlightById(long id);
    void deleteFlightById(long id);

}
