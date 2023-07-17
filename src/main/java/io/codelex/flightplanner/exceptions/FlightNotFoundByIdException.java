package io.codelex.flightplanner.exceptions;

public class FlightNotFoundByIdException extends RuntimeException{
    public FlightNotFoundByIdException(String message){
        super(message);
    }
}
