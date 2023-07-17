package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.exceptions.FlightNotFoundByIdException;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.requests.SearchFlightRequest;
import io.codelex.flightplanner.responses.PageResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


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

    public void getAllFlights(){
        flightRepository.getFlightList();
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

    public PageResultResponse<Airport> searchAirports(String request) {
        String searchAirport = request.toLowerCase();
        List<Airport> matchingAirports = flightRepository.getFlightList().stream()
                .map(Flight::getFrom)
                .filter(airport -> airport.getCountry().toLowerCase().contains(searchAirport) ||
                        airport.getCity().toLowerCase().contains(searchAirport) ||
                        airport.getAirport().toLowerCase().contains(searchAirport))
                .collect(Collectors.toList());

        return new PageResultResponse<>(1, matchingAirports.size(), matchingAirports);
    }

    public PageResultResponse<Flight> searchFlights(SearchFlightRequest request) {
        String from = request.getFrom().toLowerCase();
        String to = request.getTo().toLowerCase();
        LocalDateTime departureTime = request.getDepartureTime();

        List<Flight> matchingFlights = flightRepository.getFlightList().stream()
                .filter(flight -> flight.getFrom().getCountry().toLowerCase().contains(from) ||
                        flight.getFrom().getCity().toLowerCase().contains(from) ||
                        flight.getFrom().getAirport().toLowerCase().contains(from) ||
                        flight.getTo().getCountry().toLowerCase().contains(to) ||
                        flight.getTo().getCity().toLowerCase().contains(to) ||
                        flight.getTo().getAirport().toLowerCase().contains(to) ||
                        flight.getDepartureTime().equals(departureTime))
                .collect(Collectors.toList());

        return new PageResultResponse<>(1, matchingFlights.size(), matchingFlights);
    }

}
