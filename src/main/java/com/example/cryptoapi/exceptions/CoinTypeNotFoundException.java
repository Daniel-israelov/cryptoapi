package com.example.cryptoapi.exceptions;

public class CoinTypeNotFoundException extends RuntimeException {

    public CoinTypeNotFoundException(String name) { super("There is no CoinType corresponding to name = " + name); }
}
