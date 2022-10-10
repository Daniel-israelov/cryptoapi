package com.example.cryptoapi.exceptions;

import java.util.UUID;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException() { super("There is no corresponding Wallet for the desired User."); }

    public WalletNotFoundException(UUID uuid) {
        super("There is no Wallet corresponding to UUID = " + uuid);
    }
}
