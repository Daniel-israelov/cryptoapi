package com.example.cryptoapi.advices;

import com.example.cryptoapi.exceptions.CoinTypeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CoinTypeAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CoinTypeNotFoundException.class)
    String CoinTypeNotFoundHandler(CoinTypeNotFoundException ctnfe) { return ctnfe.getMessage(); }
}
