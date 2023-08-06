package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findFlightById(Long id);

    Optional<Flight> deleteFlightById(Long id);


    @Query("SELECT f FROM Flight f WHERE f.from.airport = :fromAirport " +
            "AND LOWER(f.to.airport) = LOWER(:toAirport) " +
            "AND LOWER(f.carrier) = LOWER(:carrier) " +
            "AND f.departureTime = :departureTime " +
            "AND f.arrivalTime = :arrivalTime ")
    List<Flight> findExistingFlightsByCriteria(String fromAirport, String toAirport, String carrier, LocalDateTime departureTime, LocalDateTime arrivalTime);


    @Query("SELECT f FROM Flight f WHERE f.from.airport LIKE ('%' || :from || '%') " +
            "AND f.to.airport LIKE ('%' || :to || '%') " +
            "AND Date(f.departureTime)=  :departureDate")
    List<Flight> searchFlights(@Param("from") String from, @Param("to") String to, @Param("departureDate") LocalDate departureDate);

}
