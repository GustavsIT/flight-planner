package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.FlightPlannerInMemoryRepository;
import io.codelex.flightplanner.requests.AddFlightRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class AddFlightTest {
    @Autowired
    FlightPlannerInMemoryRepository flightPlannerInMemoryRepository;

    @Autowired
    AdminApiController adminApiController;



    @Test
    public void addFlight(){
        AddFlightRequest request = new AddFlightRequest();
        Airport from = new Airport("Latvia", "Riga", "RIX");
        Airport to = new Airport("Estonia", "Tallinn", "TLN");
        request.setFrom(from);
        request.setTo(to);
        request.setCarrier("AirBaltic");
        LocalDateTime departureTime = LocalDateTime.of(2023, 7, 23, 12, 30);
        LocalDateTime arrivalTime = LocalDateTime.of(2023, 7, 23, 14, 45);
        request.setDepartureTime(departureTime);
        request.setArrivalTime(arrivalTime);

        Flight flight = adminApiController.addFlight(request);

        Assertions.assertEquals(request.getFrom(), flight.getFrom());
        Assertions.assertEquals(request.getTo(), flight.getTo());
        Assertions.assertEquals(request.getCarrier(), flight.getCarrier());
        Assertions.assertEquals(request.getDepartureTime(), flight.getDepartureTime());
        Assertions.assertEquals(request.getArrivalTime(), flight.getArrivalTime());

    }
}
