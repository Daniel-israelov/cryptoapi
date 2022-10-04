package com.example.cryptoapi.dtos;

import com.example.cryptoapi.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

@Value
@JsonPropertyOrder({"Full Name", "Age", "Gender", "Identity number"})
public class UserDto {
    @JsonIgnore
    User user;

    @JsonProperty("Full name")
    public String getFullName() {
        return user.getFirstName() + user.getLastName();
    }

    @JsonProperty("Age")
    public Integer getAge() {
        return user.getAge();
    }

    @JsonProperty("Gender")
    public String getGender() {
        return user.getIsMale() ? "Male" : "Female";
    }

    @JsonProperty("Identity Number")
    public Long getIdentityNumber() {
        return user.getIdentityNumber();
    }
}
