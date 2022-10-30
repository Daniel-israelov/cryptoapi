package com.example.cryptoapi.dtos;

import com.example.cryptoapi.entities.CoinEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

/**
 * This class represents the fields of {@link CoinEntity} class we would like to share with the clients.
 */
@Value
@JsonPropertyOrder({"coinType", "currentPrice"})
public class CoinDto {

    @JsonIgnore
    CoinEntity coinEntity;

    @JsonProperty("coinType")
    public String getCoinType() { return coinEntity.getCoinTypeEntity().getName(); }

    @JsonProperty("currentPrice")
    public Double getCurrentPrice() { return coinEntity.getCoinTypeEntity().getCurrentPrice(); }
}
