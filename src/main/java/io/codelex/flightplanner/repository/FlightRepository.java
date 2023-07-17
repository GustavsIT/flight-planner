package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Flight;
import org.springframework.stereotype.Repository;
import java.util.List;



@Repository
public class FlightRepository {
    private List<Flight> flightList;

    public FlightRepository(List<Flight> allFlights) {
        this.flightList = allFlights;
    }

    public List<Flight> getFlightList() {
        return flightList;
    }

}



