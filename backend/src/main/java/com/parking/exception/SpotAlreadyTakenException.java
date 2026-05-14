package com.parking.exception;

public class SpotAlreadyTakenException extends RuntimeException {

    public SpotAlreadyTakenException(String spotCode) {
        super("Parking spot '" + spotCode + "' already has an active or pending request.");
    }
}
