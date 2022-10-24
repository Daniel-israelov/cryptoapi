package com.example.cryptoapi.services;

import com.example.cryptoapi.entities.CoinEntity;
import com.example.cryptoapi.entities.WalletEntity;
import com.example.cryptoapi.repositories.CoinRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class CoinService {

    private final CoinRepository coinRepository;
//    private final CoinDtoAssembler coinDtoAssembler;

    public CoinService(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public void removeCoinsAttributionFromWallet(WalletEntity wallet) {
        log.info("Removing connections between all relevant coins and wallet = " + wallet);
        Set<CoinEntity> coins = wallet.getCoinEntities();
        for (CoinEntity coin : coins) {
            coin.setStoredInWalletEntity(null);
            coinRepository.save(coin);
        }
    }

    public void deleteAllUnattributedCoins() {
        log.info("Deleting all the coins that are not linked to a wallet . . .");
        coinRepository.deleteAllUnattributedCoins();
    }
}
