package com.example.cryptoapi.dtos;

import com.example.cryptoapi.entities.UserEntity;
import com.example.cryptoapi.entities.WalletEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

import java.util.Set;

/**
 * This class represents the fields of {@link UserEntity} class we would like to share with the clients.
 */
@Value
@JsonPropertyOrder({"Identity Number", "Full Name", "Age", "Gender", "Wallets"})
public class UserDto {

    @JsonIgnore
    UserEntity userEntity;

    @JsonProperty("Identity Number")
    public Long getIdentityNumber() { return userEntity.getIdentityNumber(); }

    @JsonProperty("Full Name")
    public String getFullName() { return String.format("%s %s", userEntity.getFirstName(), userEntity.getLastName()); }

    @JsonProperty("Age")
    public Integer getAge() { return userEntity.getAge(); }

    @JsonProperty("Gender")
    public String getGender() { return userEntity.getIsMale() ? "Male" : "Female"; }

    @JsonProperty("Wallets")
    public Set<WalletEntity> getWallets() { return userEntity.getWalletEntities(); }
}
