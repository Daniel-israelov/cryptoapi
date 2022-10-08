package com.example.cryptoapi.dtos;

import com.example.cryptoapi.entities.User;
import com.example.cryptoapi.entities.Wallet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

import java.util.Set;

/**
 * This class represents the fields of {@link User} class we would like to share with the clients.
 */
@Value
@JsonPropertyOrder({"Identity number", "Full Name", "Age", "Gender", "Wallets"})
public class UserDto {

    @JsonIgnore
    User user;

    @JsonProperty("Identity Number")
    public Long getIdentityNumber() { return user.getIdentityNumber(); }

    @JsonProperty("Full name")
    public String getFullName() { return String.format("%s %s", user.getFirstName(), user.getLastName()); }

    @JsonProperty("Age")
    public Integer getAge() { return user.getAge(); }

    @JsonProperty("Gender")
    public String getGender() { return user.getIsMale() ? "Male" : "Female"; }

    @JsonProperty("Wallets")
    public Set<Wallet> getWallets() { return user.getWallets(); }
}
