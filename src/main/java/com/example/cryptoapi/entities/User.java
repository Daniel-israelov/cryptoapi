package com.example.cryptoapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private Integer age;
    private Boolean isMale;  //0 male, 1 female
    private Long identityNumber;

    @OneToMany
    private List<Wallet> wallet;

//    @ElementCollection
//    private final HashMap<CoinType, List<Coin>> wallet = new HashMap<>();
}

