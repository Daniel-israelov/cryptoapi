package com.example.cryptoapi.initdb;

import com.example.cryptoapi.entities.CoinEntity;
import com.example.cryptoapi.entities.CoinTypeEntity;
import com.example.cryptoapi.entities.UserEntity;
import com.example.cryptoapi.entities.WalletEntity;
import com.example.cryptoapi.repositories.CoinRepository;
import com.example.cryptoapi.repositories.CoinTypeRepository;
import com.example.cryptoapi.repositories.UserRepository;
import com.example.cryptoapi.repositories.WalletRepository;
import com.example.cryptoapi.services.ExternalEndpointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class is responsible for Database initialization.
 */
@Configuration
@Slf4j
public class SeedDB {

    /**
     * This method calls the {@link org.springframework.scheduling.annotation.Async} method
     * in the {@link ExternalEndpointService} to initialize all {@link CoinTypeEntity} objects
     * from an external endpoint.
     */
    @Bean
    public CommandLineRunner initCoinTypes(ExternalEndpointService externalEndpointService,
                                           CoinTypeRepository coinTypeRepository) {
        return args -> {
            log.info("\n\t\t\tinit CoinTypes . . .");
            CompletableFuture<List<CoinTypeEntity>> completableFutureCoinTypes = externalEndpointService.pullExternalData();
            completableFutureCoinTypes.join();
            List<CoinTypeEntity> coinTypeEntityList = completableFutureCoinTypes.get();
            int iterationNumber = 1;
            for (CoinTypeEntity coinTypeEntity : coinTypeEntityList) {
                log.info("Saved to repo CoinType {}/{} = {}",
                        iterationNumber++, coinTypeEntityList.size(), coinTypeRepository.save(coinTypeEntity));
            }
        };
    }

    @Bean
    CommandLineRunner initUsersAndWallets(UserRepository userRepository, WalletRepository walletRepository) {
        return args -> {
            log.info("\n\t\t\tinit Users . . .");
            log.info("idan = " + userRepository.save(
                    new UserEntity(123L, "idan", "montekyo", true, 22)));
            log.info("daniel = " + userRepository.save(
                    new UserEntity(1L, "daniel", "israelov", true, 33)));

            log.info("\n\t\t\tinit Wallets . . .");
            log.info("coinbase = " + walletRepository.save(new WalletEntity("Coinbase")));
            log.info("trezor = " + walletRepository.save(new WalletEntity("Trezor")));
            log.info("ledger = " + walletRepository.save(new WalletEntity("Ledger")));
            log.info("exodus = " + walletRepository.save(new WalletEntity("Exodus")));
            log.info("mycelium = " + walletRepository.save(new WalletEntity("Mycelium")));
        };
    }

    @Bean
    CommandLineRunner connectBetweenUsersAndWallets(UserRepository userRepository, WalletRepository walletRepository) {
        return args -> {
            log.info("\n\t\t\tconnect between users and wallets . . .");
            UserEntity idan = userRepository.findByIdentityNumber(123L).get();
            WalletEntity coinbase = walletRepository.findByProvider("Coinbase").get(0);
            WalletEntity exodus = walletRepository.findByProvider("Exodus").get(0);
            idan.connectWallets(coinbase, exodus);
            log.info("connecting 'coinbase' and 'exodus' wallets to idan = " + idan.connectWallets(coinbase, exodus));
            log.info("saved 'coinbase' with idan as owner = " + walletRepository.save(coinbase));
            log.info("saved 'exodus' with idan as owner = " + walletRepository.save(exodus));
            userRepository.save(idan);
            log.info("saved idan with new wallets in repo = " + idan);

            UserEntity daniel = userRepository.findByIdentityNumber(1L).get();
            WalletEntity ledger = walletRepository.findByProvider("Ledger").get(0);
            log.info("connecting 'coinbase' and 'ledger' wallets to daniel = " + daniel.connectWallets(coinbase, ledger));
            log.info("saved 'coinbase' with daniel as owner = " + walletRepository.save(coinbase));
            log.info("saved 'ledger' with daniel as owner = " + walletRepository.save(ledger));
            userRepository.save(daniel);
            log.info("saved daniel in repo = " + daniel);
        };
    }

    @Bean
    CommandLineRunner initAndDistributeConcreteCryptoCoins(CoinTypeRepository coinTypeRepository,
                                                           CoinRepository coinRepository,
                                                           UserRepository userRepository,
                                                           WalletRepository walletRepository) {
        return args -> {
            log.info("\n\t\t\tinit concrete Coins . . .");
            CoinTypeEntity bitcoin = coinTypeRepository.findByName("Bitcoin");
            CoinTypeEntity ethereum = coinTypeRepository.findByName("Ethereum");
            CoinTypeEntity cardano = coinTypeRepository.findByName("Cardano");
            CoinTypeEntity polkadot = coinTypeRepository.findByName("Polkadot");

            CoinEntity concreteBitcoin = new CoinEntity(bitcoin);
            log.info("Creating concrete Bitcoin = " + coinRepository.save(concreteBitcoin));
            CoinEntity concreteEthereum = new CoinEntity(ethereum);
            log.info("Creating concrete Ethereum = " + coinRepository.save(concreteEthereum));
            CoinEntity concreteCardano = new CoinEntity(cardano);
            log.info("Creating concrete Cardano = " + coinRepository.save(concreteCardano));
            CoinEntity concretePolkadot = new CoinEntity(polkadot);
            log.info("Creating concrete Polkadot = " + coinRepository.save(concretePolkadot));

            log.info("\n\t\t\tdistributing concrete coins to users . . .");
            UserEntity idan = userRepository.findByIdentityNumber(123L).get();
            WalletEntity w1 = idan.addCoinToWallet(concreteBitcoin);
            WalletEntity w2 = idan.addCoinToWallet(concreteEthereum);
            UserEntity daniel = userRepository.findByIdentityNumber(1L).get();
            WalletEntity w3 = daniel.addCoinToWallet(concreteCardano);
            WalletEntity w4 = daniel.addCoinToWallet(concretePolkadot);
            log.info("saving all data to User, Wallet, Coin repositories . . .");
            coinRepository.save(concreteBitcoin);
            coinRepository.save(concreteEthereum);
            coinRepository.save(concreteCardano);
            coinRepository.save(concretePolkadot);
            log.info("w1 = " + w1.getId() + " -> " + w1);
            log.info("w2 = " + w2.getId() + " -> " + w2);
            log.info("w3 = " + w3.getId() + " -> " + w3);
            log.info("w4 = " + w4.getId() + " -> " + w4);
            walletRepository.save(w1);
            if (w1.getId() != w2.getId()) {
                walletRepository.save(w2);
            }
            if (w1.getId() != w3.getId() && w2.getId() != w3.getId()) {
                walletRepository.save(w3);
            }
            if (w1.getId() != w4.getId() && w2.getId() != w4.getId() && w3.getId() != w4.getId()) {
                walletRepository.save(w4);
            }
            log.info("idan = " + userRepository.save(idan));
            log.info("daniel = " + userRepository.save(daniel));
        };
    }
}
