package com.example.cryptoapi.initdb;

import com.example.cryptoapi.entities.Coin;
import com.example.cryptoapi.entities.CoinType;
import com.example.cryptoapi.entities.User;
import com.example.cryptoapi.entities.Wallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * This class is responsible for the initial Databate initialization.
 */
@Component
@Slf4j
public class SeedDB {

    @Bean
    CommandLineRunner seed() {
        return args -> {

            log.info("init first user");
            User idan = new User(12345L, "idan", "montekyo", true, 25);
            log.info("idan = " + idan);
            log.info("idan's wallet = " + idan.getWallet());

            log.info("creating a bitcoin");
            CoinType bitcoin = new CoinType("Bitcoin", "https://...", 25000.0, 48390432980.0,
                    25670.0, 18992.0, 855.7);
            log.info("bitcoin = " + bitcoin);

            log.info("creating concrete bitcoin");
            Coin concreteBitcoin = new Coin(bitcoin);
            log.info("concrete bitcoin = " + concreteBitcoin);

            log.info("creating an ethereum");
            CoinType ethereum = new CoinType("Ethereum", "https://...", 11000.0, 5682980.0,
                    11270.0, 10892.0, 155.7);
            log.info("ethereum = " + bitcoin);

            log.info("creating concrete ethereum");
            Coin concreteEthereum = new Coin(ethereum);
            log.info("concrete ethereum = " + concreteEthereum);

            log.info("adding concrete bitcoin to idan's wallet");
            idan.addCoin(concreteBitcoin);
            log.info("adding concrete ethereum to idan's wallet");
            idan.addCoin(concreteEthereum);
            log.info("idan's stats after adding concrete coins to wallet = \n" + idan);
        };
    }
}
