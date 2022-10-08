package com.example.cryptoapi.entities;

import com.example.cryptoapi.exceptions.WalletNotFoundException;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This data class represents a User with full details.
 * Including a crypto {@link Wallet} and the methodology to add {@link Coin}s
 * by {@link CoinType} to the {@link Wallet}.
 */
@Entity
@NoArgsConstructor
@Data
@Table(name = "USERS")
public class User implements Serializable {

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
    @Column(name="wallets")
    private Set<Wallet> wallets = new HashSet<>();

    public User(Long identityNumber, String firstName, String lastName, Boolean isMale, Integer age) {
        this.identityNumber = identityNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isMale = isMale;
        this.age = age;
    }

    /**
     * This method adds the input {@link Wallet}s to the {@link User}'s {@link Wallet}s set,
     * while adding the {@link User} to the {@link Wallet}s owners set.
     * @param walletsToAdd args of type {@link Wallet}.
     * @return the {@link User}'s full set of {@link Wallet}s.
     */
    public Set<Wallet> connectWallets(@NotNull Wallet... walletsToAdd) {
        for (Wallet wallet : walletsToAdd) {
            this.wallets.add(wallet);
            wallet.addOwner(this);
        }
        return this.wallets;
    }

    /**
     * This method removes the input {@link Wallet}s from the {@link User}'s {@link Wallet}s set,
     * while removing the {@link User} from the {@link Wallet}s owners set.
     * @param walletsToAdd args of type {@link Wallet}.
     * @return the {@link User}'s full set of {@link Wallet}s.
     */
    public Set<Wallet> disconnectWallets(@NotNull Wallet... walletsToAdd) {
        for (Wallet wallet : walletsToAdd) {
            this.wallets.remove(wallet);
            wallet.removeOwner(this);
        }
        return this.wallets;
    }

    /**
     * This method gets a concrete {@link Coin} and adds it to a random {@link Wallet} from the {@link User}'s
     * set of {@link Wallet}s.
     * @param coinToAdd object of type concrete {@link Coin}.
     * @return the random {@link Wallet} chosen.
     */
    public Wallet addCoinToWallet(Coin coinToAdd) {

        Wallet addedToWallet = null;

        if (this.wallets.size() < 1) {
            throw new WalletNotFoundException();
        }
        else {
            int randomIndex = new Random().nextInt(this.wallets.size());
            int currentIndex = 0;
            for (Wallet wallet : this.wallets) {
                if (currentIndex == randomIndex) {
                    addedToWallet = wallet;
                    wallet.addCoin(coinToAdd);
                }
                currentIndex++;
            }
        }

        return addedToWallet;
    }

    /**
     * This method gets a concrete {@link Coin} and a {@link Wallet}.
     * In case the {@link User} owns the {@link Wallet}, adds the {@link Coin} to it.
     * @param coinToAdd object of type concrete {@link Coin}.
     * @param wallet object of type {@link Wallet}.
     */
    public void addCoinToWallet(Coin coinToAdd, Wallet wallet) {

        if (this.wallets.size() < 1 || !this.wallets.contains(wallet)) {
            throw new WalletNotFoundException();
        }
        else {
            wallet.addCoin(coinToAdd);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", identityNumber=" + identityNumber +
                ", name='" + firstName + ' ' + lastName + '\'' +
                ", gender=" + (isMale ? "male" : "female") +
                ", age=" + age +
                ", wallets=" + wallets +
                '}';
    }

    @Override
    public int hashCode() { return this.id == null ? 0 : this.id.hashCode(); }
}

