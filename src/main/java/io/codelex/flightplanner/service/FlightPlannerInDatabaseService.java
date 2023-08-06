package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.AirportRepository;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.requests.SearchFlightRequest;
import io.codelex.flightplanner.responses.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        flightRepository.deleteAll();
        airportRepository.deleteAll();
    }


    @Override
    public synchronized Flight addFlight(AddFlightRequest addFlightRequest) {

        LocalDateTime departureTime = addFlightRequest.getDepartureTime();
        LocalDateTime arrivalTime = addFlightRequest.getArrivalTime();
        if(departureTime.isAfter(arrivalTime)||departureTime.isEqual(arrivalTime)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid date and time: departure time is after/equals arrival time");
        }


        String from = addFlightRequest.getFrom().getAirport().toLowerCase().trim();
        String to = addFlightRequest.getTo().getAirport().toLowerCase().trim();
        if(from.equals(to)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid, FROM and TO airports are the same!");
        }


        String carrier = addFlightRequest.getCarrier();
        Airport airport_from = addFlightRequest.getFrom();
        Airport airport_to = addFlightRequest.getTo();
        List<Flight> existingFlights = flightRepository.findExistingFlightsByCriteria(airport_from.getAirport(), airport_to.getAirport(), carrier, departureTime, arrivalTime);
        if(!existingFlights.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Flight already exists");
        }


        Flight newFlight = new Flight(airport_from, airport_to, carrier, departureTime, arrivalTime);
        airportRepository.save(newFlight.getFrom());
        airportRepository.save(newFlight.getTo());
        return flightRepository.save(newFlight);
    }


    @Override
    public List<Airport> searchAirport(String search) {
        return airportRepository.searchAirportsByCityNameOrAirportName(search.toLowerCase().trim());
    }


    @Override
    public PageResult<Flight> searchFlights(SearchFlightRequest searchFlightRequest) {
        if (searchFlightRequest.getFrom().toLowerCase().trim().equals(searchFlightRequest.getTo().toLowerCase().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FROM and TO airports cannot be the same!");
        }

        String from = searchFlightRequest.getFrom();
        String to = searchFlightRequest.getTo();
        LocalDate departureDate = searchFlightRequest.getDepartureDate();
        List<Flight> matchingFLights = flightRepository.searchFlights(from, to, departureDate);
        return new PageResult<>(0, matchingFLights.size(), matchingFLights);
    }


    @Override
    public Flight searchFlightById(long id) {
        return flightRepository.findFlightById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @Override
    public synchronized void deleteFlightById(long id) {
        flightRepository.deleteFlightById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
