package io.codelex.flightplanner.responses;

import io.codelex.flightplanner.domain.Flight;

import java.util.List;

public class FlightsResponse {
    List<Flight> flightList;

    public FlightsResponse(List<Flight> flightList) {
        this.flightList = flightList;
    }

    public List<Flight> getFlightList() {
        return flightList;
    }

    public void setFlightList(List<Flight> flightList) {
        this.flightList = flightList;
    }
}
