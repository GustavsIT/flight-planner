package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.exceptions.FlightNotFoundByIdException;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.responses.PageResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private long lastFlightId;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
        this.lastFlightId = 0;
    }


    public void clearAllFlights(){
        flightRepository.getFlightList().clear();
    }

    public void deleteFlightById(long id) {
        boolean flightFound = false;
        for (Flight flight : flightRepository.getFlightList()) {
            if (flight.getId() == id) {
                flightFound = true;
                flightRepository.getFlightList().remove(flight);
                break;
            }
        }
        if (!flightFound) {
            throw new FlightNotFoundByIdException("Flight not found for ID: " + id);
        }
    }

    public synchronized void addFlight(AddFlightRequest addFlightRequest){
        long newFlightId = lastFlightId + 1;
        lastFlightId = newFlightId;
        AddFlightRequest addFlightRequest1 = new AddFlightRequest();
        Airport from = addFlightRequest1.getFrom();
        Airport to = addFlightRequest.getTo();
        String carrier = addFlightRequest.getCarrier();
        LocalDateTime departureTime = addFlightRequest.getDepartureTime();
        LocalDateTime arrivalTime = addFlightRequest.getArrivalTime();
        if (flightAlreadyExists(addFlightRequest)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Flight already exists!");
        }
        if (from == null || to == null || carrier.isBlank() || departureTime == null || arrivalTime == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid flight data! Empty values!");
        }
        if (from.equals(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departure and arrival airports cannot be the same!");
        }
        if (departureTime.isAfter(arrivalTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departure time cannot be after arrival time!");
        }
        Flight flight= new Flight(newFlightId,from, to, carrier, departureTime, arrivalTime);
        flightRepository.getFlightList().add(flight);
    }

    private boolean flightAlreadyExists(AddFlightRequest addFlightRequest) {
        Airport from = addFlightRequest.getFrom();
        Airport to = addFlightRequest.getTo();
        String carrier = addFlightRequest.getCarrier();
        LocalDateTime departureTime = addFlightRequest.getDepartureTime();
        LocalDateTime arrivalTime = addFlightRequest.getArrivalTime();
        for (Flight flight : flightRepository.getFlightList()) {
            if (flight.getFrom().equals(from) &&
                    flight.getTo().equals(to) &&
                    flight.getCarrier().equals(carrier) &&
                    flight.getDepartureTime().equals(departureTime) &&
                    flight.getArrivalTime().equals(arrivalTime)) {
                return true;
            }
        }
        return false;
    }

    public Flight searchFlightById(long id) {
        for (Flight flight : flightRepository.getFlightList()) {
            if (flight.getId() == id) {
                return flight;
            }
        }
        throw new FlightNotFoundByIdException("Flight not found for ID: " + id);
    }

    public PageResultResponse<Airport> searchAirports() {
        List<Flight> flightList = flightRepository.getFlightList();
        Set<Airport> airportSet = new HashSet<>();

        for (Flight flight : flightList) {
            airportSet.add(flight.getFrom());
            airportSet.add(flight.getTo());
        }

        List<Airport> airports = new ArrayList<>(airportSet);
        return new PageResultResponse<>(1, airports.size(), airports);
    }

    public PageResultResponse<Flight> searchFlights() {
        List<Flight> allFlights = flightRepository.getFlightList();
        return new PageResultResponse<>(1, allFlights.size(), allFlights);
    }

}
