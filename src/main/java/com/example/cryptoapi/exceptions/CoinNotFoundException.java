package com.example.cryptoapi.exceptions;

import java.util.UUID;

public class CoinNotFoundException extends RuntimeException {

    public CoinNotFoundException(UUID uuid) { super("There is no Coin corresponding to uuid = " + uuid); }
}
