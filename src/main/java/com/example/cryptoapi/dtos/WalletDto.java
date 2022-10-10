package com.example.cryptoapi.dtos;

import com.example.cryptoapi.entities.CoinEntity;
import com.example.cryptoapi.entities.WalletEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents the fields of {@link WalletEntity} class we would like to share with the clients.
 */
@Value
@JsonPropertyOrder({"Provider", "Owners", "Coins"})
public class WalletDto {

    @JsonIgnore
    WalletEntity walletEntity;

    @JsonProperty("Provider")
    public String getProvider() { return walletEntity.getProvider(); }

    @JsonProperty("Owners")
    public List<String> getOwners() {
        return walletEntity.getOwners().stream()
                .map(owner -> owner.getFirstName() + " " + owner.getLastName())
                .collect(Collectors.toList());
    }

    // TODO: change to Set<EntityModel<CoinDto>>
    @JsonProperty("Coins")
    public Set<CoinEntity> getCoins() { return walletEntity.getCoinEntities(); }
}
