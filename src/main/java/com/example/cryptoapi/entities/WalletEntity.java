package com.example.cryptoapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This data class represents a crypto wallet, storing concrete crypto {@link CoinEntity}s (e.g. Bitcoin, Ethereum, etc.).
 */
@Entity
@NoArgsConstructor
@Data
public class WalletEntity implements Serializable {

    @Id
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private String provider;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "walletEntities")
    private final Set<UserEntity> owners = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER)
    private final Set<CoinEntity> coinEntities = new HashSet<>();

    public WalletEntity(String provider) { this.provider = provider; }

    public void addOwner(@NotNull UserEntity... ownersToAdd) { this.owners.addAll(Arrays.asList(ownersToAdd)); }

    public void removeOwner(@NotNull UserEntity... ownersToRemove) {
        for (UserEntity owner : ownersToRemove) {
            this.owners.remove(owner);
        }
    }

    public void addCoin(@NotNull CoinEntity coinEntityToAdd) {
        this.coinEntities.add(coinEntityToAdd);
        coinEntityToAdd.storeInWallet(this);
    }

    @Override
    public int hashCode() { return this.id == null ? 0 : this.id.hashCode(); }

    @Override
    public String toString() {
        return provider + "Wallet{" +
                "owners=" + owners.stream().map(owner -> owner.getFirstName() + owner.getLastName()).collect(Collectors.toList()) +
                ", coinsByType=" + coinEntities +
                '}';
    }
}
