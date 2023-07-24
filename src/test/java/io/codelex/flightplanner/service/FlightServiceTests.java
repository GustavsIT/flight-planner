package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.FlightPlannerRepository;
import io.codelex.flightplanner.requests.SearchFlightRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTests {

    @Mock
    FlightPlannerRepository flightPlannerRepository;
    @InjectMocks
    FlightPlannerService flightPlannerService;

    @Test
    public void searchFlight(){
        Airport airport1 = new Airport("Latvia", "Riga", "RIX");
        Airport airport2 = new Airport("Estonia", "Tallinn", "TLN");
        List<Flight> flights = List.of(
                new Flight(1, airport1, airport2, "AirBaltic", LocalDateTime.now(), LocalDateTime.now().plusHours(2)),
                new Flight(2, airport2, airport1, "RyanAir", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2))
        );
        Mockito.when(flightPlannerRepository.getFlights()).thenReturn(flights);

        SearchFlightRequest searchFlightRequest = new SearchFlightRequest(airport1.getAirport(), airport2.getAirport(), LocalDate.now());
        List<Flight> searchResult = flightPlannerService.searchFlights(searchFlightRequest).getItems();
        assertEquals(1, searchResult.size());

        Flight flightResult1 = searchResult.get(0);
        assertEquals("Latvia", flightResult1.getFrom().getCountry());
        assertEquals("Riga", flightResult1.getFrom().getCity());
        assertEquals("RIX", flightResult1.getFrom().getAirport());
        assertEquals("Estonia", flightResult1.getTo().getCountry());
        assertEquals("Tallinn", flightResult1.getTo().getCity());
        assertEquals("TLN", flightResult1.getTo().getAirport());

    }
    
}
