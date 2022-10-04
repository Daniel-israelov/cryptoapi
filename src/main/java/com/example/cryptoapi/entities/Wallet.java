package com.example.cryptoapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This data class represents a crypto wallet, sorting the coins by types (e.g. Bitcoin, Ethereum, etc.).
 */
@Entity
@NoArgsConstructor
@Data
public class Wallet {

    @Id
    @GeneratedValue
    private Long id;
    private final HashMap<String, List<Coin>> coinsByType = new HashMap<>();

    public Wallet(Long id) { this.id = id; }

    /**
     * This method gets a concrete crypto coin and adds it to the corresponding list in the coins hashmap.
     * @param coinToAdd a concrete crypto coin
     */
    public void addCoin(Coin coinToAdd) {

        String coinTypeName = coinToAdd.getCoinType().getName();
        if (this.coinsByType.containsKey(coinTypeName)) {
            this.coinsByType.get(coinTypeName).add(coinToAdd);
        }
        else {
            this.coinsByType.put(coinTypeName, new java.util.ArrayList<>(Collections.emptyList()));
            addCoin(coinToAdd);
        }
    }
}
