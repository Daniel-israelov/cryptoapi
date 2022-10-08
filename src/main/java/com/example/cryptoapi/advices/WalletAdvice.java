package com.example.cryptoapi.advices;

import com.example.cryptoapi.exceptions.WalletNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WalletAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WalletNotFoundException.class)
    String walletNotFoundHandler(WalletNotFoundException wnfe) { return wnfe.getMessage(); }
}
