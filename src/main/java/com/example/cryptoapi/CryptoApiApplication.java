package com.example.cryptoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CryptoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoApiApplication.class, args);
    }

}
