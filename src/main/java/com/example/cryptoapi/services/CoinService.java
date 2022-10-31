package com.example.cryptoapi.services;

import com.example.cryptoapi.assemblers.CoinDtoAssembler;
import com.example.cryptoapi.dtos.CoinDto;
import com.example.cryptoapi.entities.CoinEntity;
import com.example.cryptoapi.entities.WalletEntity;
import com.example.cryptoapi.exceptions.CoinNotFoundException;
import com.example.cryptoapi.repositories.CoinRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class CoinService {

    private final CoinRepository coinRepository;
    private final CoinDtoAssembler coinDtoAssembler;
    private final UserService userService;
    private final WalletService walletService;
    private final CoinTypeService coinTypeService;

    public CoinService(CoinRepository coinRepository, CoinDtoAssembler coinDtoAssembler,
                       UserService userService, @Lazy WalletService walletService, CoinTypeService coinTypeService) {
        this.coinRepository = coinRepository;
        this.coinDtoAssembler = coinDtoAssembler;
        this.userService = userService;
        this.walletService = walletService;
        this.coinTypeService = coinTypeService;
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

    public CollectionModel<EntityModel<CoinDto>> getAllCoins() {
        List<CoinDto> allCoins = StreamSupport.stream(coinRepository.findAll().spliterator(), false)
                .map(CoinDto::new).collect(Collectors.toList());
        log.info("Retrieved all CoinDtos from service");
        return coinDtoAssembler.toCollectionModel(allCoins);
    }

    public EntityModel<CoinDto> getByUUID(UUID uuid) {
        CoinDto coinDto = coinRepository.findById(uuid).map(CoinDto::new)
                .orElseThrow(() -> new CoinNotFoundException(uuid));
        log.info("CoinDto with uuid = " + uuid + " returned in service = " + coinDto);
        return coinDtoAssembler.toModel(coinDto);
    }

    public CollectionModel<EntityModel<CoinDto>> getByCoinType(String coinTypeName) {
        coinTypeService.confirmCoinTypeExistenceByName(coinTypeName);
        log.info("Retrieved all CoinDtos with CoinType = '" + coinTypeName + "' from service");
        return coinDtoAssembler.toCollectionModel(
                coinRepository.getByCoinType(coinTypeName)
                        .stream().map(CoinDto::new).collect(Collectors.toList()));
    }

    public CollectionModel<EntityModel<CoinDto>> getByWalletUUID(UUID walletUUID) {
        walletService.confirmWalletExistenceByUUID(walletUUID);
        log.info("Retrieved all CoinDtos stored in Wallet corresponding to uuid = '" + walletUUID + "' from service");
        return coinDtoAssembler.toCollectionModel(
                coinRepository.getByWalletUUID(walletUUID.toString())
                        .stream().map(CoinDto::new).collect(Collectors.toList()));
    }

    public CollectionModel<EntityModel<CoinDto>> getByUserIdentityNumber(Long userIdentityNumber) {
        userService.confirmUserExistenceByIdentityNumber(userIdentityNumber);
        log.info("Retrieved all CoinDtos which belongs to User corresponding to identityNumber = '"
                + userIdentityNumber + "' from service");
        return coinDtoAssembler.toCollectionModel(
                coinRepository.getByUserIdentityNumber(userIdentityNumber)
                        .stream().map(CoinDto::new).collect(Collectors.toList()));
    }

    public void deleteCoinByUUID(UUID uuid) {
        CoinEntity coin = coinRepository.findById(uuid)
                .orElseThrow(() -> new CoinNotFoundException(uuid));
        log.info("trying to delete coin with uuid = " + uuid + " in service . . .");
        // TODO: both queries are sent without failing, but the rows still exists in the database.
        //  note - when using the SAME queries in the browser, the rows are deleted.
        coinRepository.deleteForeignKeyById(uuid);
        coinRepository.deleteById(uuid);
        log.info("coin with uuid = " + uuid + " deleted in service = " + coin);
    }
}
