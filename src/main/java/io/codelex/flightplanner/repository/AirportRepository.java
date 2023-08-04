package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {

    //pagaidu variants
    @Query("SELECT a FROM Airport a " +
            "WHERE LOWER(a.airport) LIKE LOWER(concat('%', :search, '%')) OR " +
            "LOWER(a.city) LIKE LOWER(concat('%', :search, '%')) OR " +
            "LOWER(a.country) LIKE LOWER(concat('%', :search, '%'))")
    List<Airport> searchAirportsByCityNameOrAirportName(@Param("search") String search);




}
