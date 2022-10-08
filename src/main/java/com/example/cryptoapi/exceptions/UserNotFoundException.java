package com.example.cryptoapi.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long identityNumber) {
        super("There is no User corresponding to identityNumber = " + identityNumber);
    }
}
