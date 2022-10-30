package com.example.cryptoapi.dtos;

import com.example.cryptoapi.assemblers.CoinDtoAssembler;
import com.example.cryptoapi.entities.WalletEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents the fields of {@link WalletEntity} class we would like to share with the clients.
 */
@Value
@JsonPropertyOrder({"provider", "owners", "coins"})
public class WalletDto {

    @JsonIgnore
    WalletEntity walletEntity;

    @JsonProperty("provider")
    public String getProvider() { return walletEntity.getProvider(); }

    @JsonProperty("owners")
    public List<String> getOwners() {
        return walletEntity
                .getOwners()
                .stream()
                .map(owner -> owner.getFirstName() + " " + owner.getLastName() + " " + owner.getIdentityNumber())
                .collect(Collectors.toList());
    }

    @JsonProperty("coins")
    public Set<EntityModel<CoinDto>> getCoins() {
        CoinDtoAssembler coinDtoAssembler = new CoinDtoAssembler();
        return walletEntity.getCoinEntities()
                .stream().map(CoinDto::new).map(coinDtoAssembler::toModel).collect(Collectors.toSet());
    }
}
