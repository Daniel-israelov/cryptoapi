package com.example.cryptoapi.exceptions;

public class WalletIllegalCoinRangeException extends RuntimeException {

    public WalletIllegalCoinRangeException(Integer illegalCoinsInput) {
        super("Illegal coins input = " + illegalCoinsInput + ". Coins range must be a positive integer.");
    }

    public WalletIllegalCoinRangeException(Integer illegalMinCoinsInput, Integer illegalMaxCoinsInput) {
        super("Illegal coins input = " + illegalMinCoinsInput
                + ", " + illegalMaxCoinsInput + ". Coins range must be a positive integer.");
    }
}
