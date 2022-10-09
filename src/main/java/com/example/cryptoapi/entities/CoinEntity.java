package com.example.cryptoapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.id.UUIDGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * This data class represents a concrete crypto Coin (which holds a value) of a specific {@link CoinTypeEntity}.
 */
@Entity
@Data
@NoArgsConstructor
public class CoinEntity implements Serializable {

    @Id
    @GeneratedValue(generator = UUIDGenerator.UUID_GEN_STRATEGY)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;
    @ManyToOne
    private CoinTypeEntity coinTypeEntity;
    @JsonIgnore
    @ManyToOne
    private WalletEntity storedInWalletEntity;

    public CoinEntity(@NotNull CoinTypeEntity coinTypeEntity) {
        this.coinTypeEntity = coinTypeEntity;
        storedInWalletEntity = null;
    }

    public void storeInWallet(@NotNull WalletEntity walletEntity) { this.storedInWalletEntity = walletEntity; }

    @Override
    public String toString() { return "concrete" + coinTypeEntity.getName() + "{uuid=" + uuid + '}'; }
}
