package com.example.cryptoapi.dtos;

import com.example.cryptoapi.assemblers.WalletDtoAssembler;
import com.example.cryptoapi.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;
import org.springframework.hateoas.EntityModel;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents the fields of {@link UserEntity} class we would like to share with the clients.
 */
@Value
@JsonPropertyOrder({"identityNumber", "fullName", "age", "gender", "wallets"})
public class UserDto {

    @JsonIgnore
    UserEntity userEntity;

    @JsonProperty("identityNumber")
    public Long getIdentityNumber() { return userEntity.getIdentityNumber(); }

    @JsonProperty("fullName")
    public String getFullName() { return String.format("%s %s", userEntity.getFirstName(), userEntity.getLastName()); }

    @JsonProperty("age")
    public Integer getAge() { return userEntity.getAge(); }

    @JsonProperty("gender")
    public String getGender() { return (userEntity.getIsMale() == null ? null : (userEntity.getIsMale() ? "Male" : "Female")); }

    @JsonProperty("wallets")
    public Set<EntityModel<WalletDto>> getWallets() {
        WalletDtoAssembler walletDtoAssembler = new WalletDtoAssembler();
        return userEntity.getWalletEntities()
            .stream().map(WalletDto::new).map(walletDtoAssembler::toModel).collect(Collectors.toSet());
    }
}
