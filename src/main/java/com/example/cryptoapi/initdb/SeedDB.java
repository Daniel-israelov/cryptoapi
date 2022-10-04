package com.example.cryptoapi.initdb;

import com.example.cryptoapi.entities.Coin;
import com.example.cryptoapi.entities.Wallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SeedDB {

    @Bean
    CommandLineRunner seed() {
        return args -> {
            log.info("init db");
            Wallet<List<Coin>> wallet = new Wallet<>();
            log.info(wallet+"");
        };
    }
}
