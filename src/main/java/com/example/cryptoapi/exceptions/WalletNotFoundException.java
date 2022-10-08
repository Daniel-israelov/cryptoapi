package com.example.cryptoapi.exceptions;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException() { super("There is no corresponding Wallet for the desired User."); }
}
