package com.example.cryptoapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.id.UUIDGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * This data class represents a concrete crypto Coin (which holds a value) of a specific {@link CoinType}.
 */
@Entity
@Data
@NoArgsConstructor
public class Coin implements Serializable {

    @Id
    @GeneratedValue(generator = UUIDGenerator.UUID_GEN_STRATEGY)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;
    @ManyToOne
    private CoinType coinType;
    @ManyToOne
    private Wallet storedInWallet;

    public Coin(@NotNull CoinType coinType) {
        this.coinType = coinType;
        storedInWallet = null;
    }

    public void storeInWallet(@NotNull Wallet wallet) { this.storedInWallet = wallet; }

    @Override
    public String toString() { return "concrete" + coinType.getName() + "{uuid=" + uuid + '}'; }
}
