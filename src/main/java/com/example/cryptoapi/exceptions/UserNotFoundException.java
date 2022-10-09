package com.example.cryptoapi.exceptions;

public class UserNotFoundException extends RuntimeException {

    private static final String baseMessage = "There is no User corresponding to ";

    public UserNotFoundException(Long identityNumber) {
        super(baseMessage + "identityNumber = " + identityNumber);
    }

    public UserNotFoundException(String firstName, String lastName) {
        super(baseMessage + "name = " + firstName + " " + lastName);
    }
}
