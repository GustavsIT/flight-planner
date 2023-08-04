package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.AirportRepository;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.requests.SearchFlightRequest;
import io.codelex.flightplanner.responses.PageResult;

import java.util.List;


public class FlightPlannerInDatabaseService implements FlightPlannerService {
    private FlightRepository flightRepository;

    private AirportRepository airportRepository;


    public FlightPlannerInDatabaseService(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }


    @Override
    public void clearAllFlights() {

    }

    @Override
    public Flight addFlight(AddFlightRequest addFlightRequest) {
        return null;
    }

    @Override
    public List<Airport> searchAirport(String search) {
        return airportRepository.searchAirportsByCityNameOrAirportName(search);
    }

    @Override
    public PageResult<Flight> searchFlights(SearchFlightRequest searchFlightRequest) {
        return null;
    }

    @Override
    public Flight searchFlightById(long id) {
        return null;
    }

    @Override
    public void deleteFlightById(long id) {

    }
}
