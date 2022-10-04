package com.example.cryptoapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * This data class represents a user with full details.
 * Including a crypto wallet and the methodology to add coins by type to the wallet.
 */
@Entity
@NoArgsConstructor
@Data
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private Long identityNumber;
    private String firstName;
    private String lastName;
    private Boolean isMale;
    private Integer age;
    @OneToOne
    private Wallet wallet;

    public User(Long identityNumber, String firstName, String lastName, Boolean isMale, Integer age) {
        this.identityNumber = identityNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isMale = isMale;
        this.age = age;
        this.wallet = new Wallet(this.id);
    }

    /**
     * This method calls the {@link Wallet#addCoin(Coin)} method in the user's wallet.
     * @param coinToAdd a concrete crypto coin
     */
    public void addCoin(Coin coinToAdd) { this.wallet.addCoin(coinToAdd); }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", identityNumber=" + identityNumber +
                ", name='" + firstName + ' ' + lastName + '\'' +
                ", gender=" + (isMale ? "male" : "female") +
                ", age=" + age +
                ", " + wallet +
                '}';
    }
}

