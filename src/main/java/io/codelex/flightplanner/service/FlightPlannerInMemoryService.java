package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.FlightPlannerInMemoryRepository;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.requests.SearchFlightRequest;
import io.codelex.flightplanner.responses.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FlightPlannerInMemoryService implements FlightPlannerService {
    private FlightPlannerInMemoryRepository flightPlannerInMemoryRepository;
    private long flightIdCounter =0;


    public FlightPlannerInMemoryService(FlightPlannerInMemoryRepository flightPlannerInMemoryRepository) {
        this.flightPlannerInMemoryRepository = flightPlannerInMemoryRepository;
    }

    public void clearAllFlights(){
        flightPlannerInMemoryRepository.clear();
    }

    public synchronized Flight addFlight(AddFlightRequest addFlightRequest) {
            if (flightAlreadyExists(addFlightRequest)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Flight already exists");
            }
            if(!validateAirports(addFlightRequest)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid, FROM and TO airports are the same!");
            }
            if(!validateDateTime(addFlightRequest)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid date and time: departure time is after/equals arrival time");
            }
            Flight newFlight = createFlight(addFlightRequest);
            flightPlannerInMemoryRepository.addFlight(newFlight);
            return newFlight;
    }

    private Flight createFlight(AddFlightRequest addFlightRequest) {
        Airport from = addFlightRequest.getFrom();
        Airport to = addFlightRequest.getTo();
        String carrier = addFlightRequest.getCarrier();
        LocalDateTime departureTime = addFlightRequest.getDepartureTime();
        LocalDateTime arrivalTime = addFlightRequest.getArrivalTime();

        long newFlightId = generateFlightId();
        return new Flight(newFlightId, from, to, carrier, departureTime, arrivalTime);
    }

    private long generateFlightId() {
        return flightIdCounter++;
    }


    private boolean flightAlreadyExists(AddFlightRequest addFlightRequest) {
        Airport from = addFlightRequest.getFrom();
        Airport to = addFlightRequest.getTo();
        String carrier = addFlightRequest.getCarrier();
        LocalDateTime departureTime = addFlightRequest.getDepartureTime();
        LocalDateTime arrivalTime = addFlightRequest.getArrivalTime();

        return flightPlannerInMemoryRepository.getFlights().stream()
                .anyMatch(flight ->
                        flight.getFrom().equals(from) &&
                                flight.getTo().equals(to) &&
                                flight.getCarrier().equals(carrier) &&
                                flight.getDepartureTime().equals(departureTime) &&
                                flight.getArrivalTime().equals(arrivalTime)
                );
    }

    private boolean validateDateTime(AddFlightRequest addFlightRequest) {
        LocalDateTime departureTime = addFlightRequest.getDepartureTime();
        LocalDateTime arrivalTime = addFlightRequest.getArrivalTime();
        if (departureTime.isAfter(arrivalTime)||departureTime.isEqual(arrivalTime)) {
            return false;
        }
        return true;
    }

    private boolean validateAirports(AddFlightRequest addFlightRequest) {
        String from = addFlightRequest.getFrom().getAirport().toLowerCase().trim();
        String to = addFlightRequest.getTo().getAirport().toLowerCase().trim();
        if (from.equals(to)) {
            return false;
        }
        return true;
    }



    public List<Airport> searchAirport(String search) {
        String searchString = search.toLowerCase().trim();
        return flightPlannerInMemoryRepository.getFlights().stream()
                .flatMap(flight -> Stream.of(flight.getFrom(), flight.getTo()))
                .filter(airport -> airport.getAirport().toLowerCase().contains(searchString)
                        || airport.getCity().toLowerCase().contains(searchString)
                        || airport.getCountry().toLowerCase().contains(searchString))
                .distinct()
                .collect(Collectors.toList());
    }

    public PageResult<Flight> searchFlights(SearchFlightRequest searchFlightRequest) {
        if (searchFlightRequest.getFrom().toLowerCase().trim().equals(searchFlightRequest.getTo().toLowerCase().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FROM and TO airports cannot be the same!");
        }
        List<Flight> flights = flightPlannerInMemoryRepository.getFlights().stream()
                .filter(flight -> flight.getFrom().getAirport().equalsIgnoreCase(searchFlightRequest.getFrom()))
                .filter(flight -> flight.getTo().getAirport().equalsIgnoreCase(searchFlightRequest.getTo()))
                .filter(flight -> flight.getDepartureTime().toLocalDate().equals(searchFlightRequest.getDepartureDate()))
                .collect(Collectors.toList());

        return new PageResult<>(0, flights.size(), flights);
    }

    public Flight searchFlightById(long id) {
        return flightPlannerInMemoryRepository.getFlights().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public synchronized void deleteFlightById(long id) {
        List<Flight> flights = flightPlannerInMemoryRepository.getFlights();
        flights.removeIf(flight -> flight.getId()==id);
    }


}