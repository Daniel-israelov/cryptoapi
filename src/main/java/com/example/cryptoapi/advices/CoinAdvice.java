package com.example.cryptoapi.advices;

import com.example.cryptoapi.exceptions.CoinNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CoinAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CoinNotFoundException.class)
    String CoinNotFoundHandler(CoinNotFoundException cnfe) { return cnfe.getMessage(); }
}
