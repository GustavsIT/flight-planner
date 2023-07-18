package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Flight;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightPlannerRepository {
    private List<Flight> flightList;

    public FlightPlannerRepository() {
        this.flightList = new ArrayList<>();
    }

    public List<Flight> getFlights() {
        return flightList;
    }
    public void clear(){
        flightList.clear();
    }
}




