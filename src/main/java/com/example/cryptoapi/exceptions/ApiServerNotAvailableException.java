package com.example.cryptoapi.exceptions;

public class ApiServerNotAvailableException extends RuntimeException {
    public ApiServerNotAvailableException(int statusCode) {
        super("Endpoint server currently Unavailable! (Response code " + statusCode + ")");
    }
}
