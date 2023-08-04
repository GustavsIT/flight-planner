package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Flight;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightPlannerInMemoryRepository {
    private List<Flight> flightList;


    public FlightPlannerInMemoryRepository() {
        this.flightList = new ArrayList<>();
    }

    public List<Flight> getFlights() {
        return flightList;
    }
    public void clear(){
        flightList.clear();
    }
    public void addFlight(Flight flight){
        flightList.add(flight);
    }


}




