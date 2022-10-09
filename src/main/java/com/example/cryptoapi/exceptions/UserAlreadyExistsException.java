package com.example.cryptoapi.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(Long identityNumber) {
        super("User with identity number=" + identityNumber + " already exists");
    }
}
