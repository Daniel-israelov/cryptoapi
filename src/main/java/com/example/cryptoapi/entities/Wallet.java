package com.example.cryptoapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;

@Entity
@Data
@NoArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue
    private Long id;
    private String provider;

    @ElementCollection
    //T = List<Coin>
    private final HashMap<CoinType, Long> coins = new HashMap<>();

    public Wallet(Long id) {
        this.id = id;
    }
}
