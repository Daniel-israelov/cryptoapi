package com.example.cryptoapi.exceptions;

public class UserIllegalAgeRangeException extends RuntimeException {

    public UserIllegalAgeRangeException(Integer illegalAgeInput) {
        super("Illegal age input = " + illegalAgeInput + ". Age range may vary between 0 to 120.");
    }

    public UserIllegalAgeRangeException(Integer illegalMinAgeInput, Integer illegalMaxAgeInput) {
        super("Illegal age input = " + illegalMinAgeInput
                + ", " + illegalMaxAgeInput + ". Age range may vary between 0 to 120.");
    }
}
