package com.example.cryptoapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.id.UUIDGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * This data class represents a concrete crypto coin (which holds a value) of a specific type.
 */
@Entity
@Data
@NoArgsConstructor
public class Coin {

    @Id
    @GeneratedValue(generator = UUIDGenerator.UUID_GEN_STRATEGY)
    private UUID uuid;
    @ManyToOne
    private CoinType coinType;

    public Coin(CoinType coinType) { this.coinType = coinType; }

    @Override
    public String toString() { return "Coin{type=" + coinType.getName() + ", uuid=" + uuid + '}'; }
}
