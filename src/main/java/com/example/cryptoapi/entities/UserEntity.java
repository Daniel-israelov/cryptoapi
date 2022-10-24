package com.example.cryptoapi.entities;

import com.example.cryptoapi.exceptions.WalletNotFoundException;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This data class represents a User with full details.
 * Including a crypto {@link WalletEntity} and the methodology to add {@link CoinEntity}s
 * by {@link CoinTypeEntity} to the {@link WalletEntity}.
 */
@Entity
@NoArgsConstructor
@Data
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Long identityNumber;
    private String firstName;
    private String lastName;
    private Boolean isMale;
    private Integer age;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Users_Wallets",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "wallet_id")})
    @Column(name = "wallets")
    private Set<WalletEntity> walletEntities = new HashSet<>();

    public UserEntity(Long identityNumber, String firstName, String lastName, Boolean isMale, Integer age) {
        this.identityNumber = identityNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isMale = isMale;
        this.age = age;
    }

    /**
     * This method adds the input {@link WalletEntity}s to the {@link UserEntity}'s {@link WalletEntity}s set,
     * while adding the {@link UserEntity} to the {@link WalletEntity}s owners set.
     *
     * @param walletsToAdd args of type {@link WalletEntity}.
     * @return the {@link UserEntity}'s full set of {@link WalletEntity}s.
     */
    public Set<WalletEntity> connectWallets(@NotNull WalletEntity... walletsToAdd) {
        for (WalletEntity walletEntity : walletsToAdd) {
            this.walletEntities.add(walletEntity);
            walletEntity.addOwner(this);
        }
        return this.walletEntities;
    }

    /**
     * This method removes the input {@link WalletEntity}s from the {@link UserEntity}'s {@link WalletEntity}s set,
     * while removing the {@link UserEntity} from the {@link WalletEntity}s owners set.
     *
     * @param walletsToAdd args of type {@link WalletEntity}.
     * @return the {@link UserEntity}'s full set of {@link WalletEntity}s.
     */
    public Set<WalletEntity> disconnectWallets(@NotNull WalletEntity... walletsToAdd) {
        for (WalletEntity walletEntity : walletsToAdd) {
            this.walletEntities.remove(walletEntity);
            walletEntity.removeOwner(this);
        }
        return this.walletEntities;
    }

    /**
     * This method gets a concrete {@link CoinEntity} and adds it to a random {@link WalletEntity} from the {@link UserEntity}'s
     * set of {@link WalletEntity}s.
     *
     * @param coinEntityToAdd object of type concrete {@link CoinEntity}.
     * @return the random {@link WalletEntity} chosen.
     */
    public WalletEntity addCoinToWallet(CoinEntity coinEntityToAdd) {

        WalletEntity addedToWalletEntity = null;

        if (this.walletEntities.size() < 1) {
            throw new WalletNotFoundException();
        } else {
            int randomIndex = new Random().nextInt(this.walletEntities.size());
            int currentIndex = 0;
            for (WalletEntity walletEntity : this.walletEntities) {
                if (currentIndex == randomIndex) {
                    addedToWalletEntity = walletEntity;
                    walletEntity.addCoin(coinEntityToAdd);
                }
                currentIndex++;
            }
        }

        return addedToWalletEntity;
    }

    /**
     * This method gets a concrete {@link CoinEntity} and a {@link WalletEntity}.
     * In case the {@link UserEntity} owns the {@link WalletEntity}, adds the {@link CoinEntity} to it.
     *
     * @param coinEntityToAdd object of type concrete {@link CoinEntity}.
     * @param walletEntity    object of type {@link WalletEntity}.
     */
    public void addCoinToWallet(CoinEntity coinEntityToAdd, WalletEntity walletEntity) {

        if (this.walletEntities.size() < 1 || !this.walletEntities.contains(walletEntity)) {
            throw new WalletNotFoundException();
        } else {
            walletEntity.addCoin(coinEntityToAdd);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", identityNumber=" + identityNumber +
                ", name='" + firstName + ' ' + lastName + '\'' +
                ", gender=" + (isMale == null ? null : (isMale ? "male" : "female")) +
                ", age=" + age +
                ", wallets=" + walletEntities +
                '}';
    }

    @Override
    public int hashCode() {
        return this.id == null ? 0 : this.id.hashCode();
    }
}

