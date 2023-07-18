package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.FlightPlannerRepository;
import io.codelex.flightplanner.requests.AddFlightRequest;
import io.codelex.flightplanner.requests.SearchFlightRequest;
import io.codelex.flightplanner.responses.FlightResponse;
import io.codelex.flightplanner.responses.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlightPlannerService {
    private FlightPlannerRepository flightPlannerRepository;
    private long flightIdCounter =1;

    public FlightPlannerService(FlightPlannerRepository flightPlannerRepository) {
        this.flightPlannerRepository = flightPlannerRepository;
    }

    public void clearAllFlights(){
        flightPlannerRepository.getFlights().clear();
    }

    public synchronized Flight addFlight(AddFlightRequest addFlightRequest) {
        if (validateAddFlightRequest(addFlightRequest)) {
            if (flightAlreadyExists(addFlightRequest)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Flight already exists");
            }
            Flight newFlight = createFlight(addFlightRequest);
            flightPlannerRepository.getFlights().add(newFlight);
            return newFlight;
        }
        return null;
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


    private boolean validateAddFlightRequest(AddFlightRequest addFlightRequest) {
        if (validateValues(addFlightRequest)
                && validateAirports(addFlightRequest)
                && validateDateTime(addFlightRequest)
                && flightAlreadyExists(addFlightRequest))
            return true;
        else return false;
    }

    private boolean flightAlreadyExists(AddFlightRequest addFlightRequest) {
        Airport from = addFlightRequest.getFrom();
        Airport to = addFlightRequest.getTo();
        String carrier = addFlightRequest.getCarrier();
        LocalDateTime departureTime = addFlightRequest.getDepartureTime();
        LocalDateTime arrivalTime = addFlightRequest.getArrivalTime();

        return flightPlannerRepository.getFlights().stream()
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
        if (departureTime.isAfter(arrivalTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid date and time: departure time is after arrival time");
        }
        return true;
    }

    private boolean validateAirports(AddFlightRequest addFlightRequest) {
        String from = addFlightRequest.getFrom().getAirport().toLowerCase().trim();
        String to = addFlightRequest.getTo().getAirport().toLowerCase().trim();;

        if (from.equals(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid, FROM and TO airports are the same!");
        }
        return true;
    }

    private boolean validateValues(AddFlightRequest addFlightRequest) {
        if (addFlightRequest.getTo() == null
                || addFlightRequest.getFrom() == null
                || addFlightRequest.getCarrier() == null
                || addFlightRequest.getDepartureTime() == null
                || addFlightRequest.getArrivalTime() == null
                || addFlightRequest.getFrom().getAirport() == null
                || addFlightRequest.getFrom().getCity() == null
                || addFlightRequest.getFrom().getCountry() == null
                || addFlightRequest.getTo().getAirport() == null
                || addFlightRequest.getTo().getCity() == null
                || addFlightRequest.getTo().getCountry() == null
        ) {
            throw new NullPointerException("Null values cannot be in the flight request!");
        }
        return true;
    }


    public List<Airport> searchAirport(String search) {
        String searchString = search.toLowerCase().trim();
        return flightPlannerRepository.getFlights().stream()
                .flatMap(flight -> Stream.of(flight.getFrom(), flight.getTo()))
                .filter(airport -> airport.getAirport().toLowerCase().contains(searchString))
                .collect(Collectors.toList());
    }

    public PageResult<Flight> searchFlights(SearchFlightRequest searchFlightRequest) {
        if (searchFlightRequest.getFrom().equals(searchFlightRequest.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        List<Flight> flights = flightPlannerRepository.getFlights().stream()
                .filter(flight -> flight.getFrom().getAirport().equalsIgnoreCase(searchFlightRequest.getFrom()))
                .filter(flight -> flight.getTo().getAirport().equalsIgnoreCase(searchFlightRequest.getTo()))
                .filter(flight -> flight.getDepartureTime().toLocalDate().equals(LocalDate.parse(searchFlightRequest.getDepartureTime())))
                .collect(Collectors.toList());

        return new PageResult<>(1, flights.size(), flights);
    }

    public FlightResponse searchFlightById(long id) {
        Flight flight = flightPlannerRepository.getFlights().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found!"));

        return new FlightResponse(flight);
    }

    public synchronized void deleteFlightById(long id) {
        flightPlannerRepository.getFlights().removeIf(flight -> flight.getId() == id);
    }


}