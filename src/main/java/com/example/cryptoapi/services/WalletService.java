package com.example.cryptoapi.services;

import com.example.cryptoapi.assemblers.WalletDtoAssembler;
import com.example.cryptoapi.dtos.WalletDto;
import com.example.cryptoapi.entities.WalletEntity;
import com.example.cryptoapi.exceptions.WalletIllegalCoinRangeException;
import com.example.cryptoapi.exceptions.WalletNotFoundException;
import com.example.cryptoapi.repositories.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletDtoAssembler walletDtoAssembler;
    private final UserService userService;
    private final CoinService coinService;

    public WalletService(WalletRepository walletRepository, WalletDtoAssembler walletDtoAssembler,
                         UserService userService, CoinService coinService) {
        this.walletRepository = walletRepository;
        this.walletDtoAssembler = walletDtoAssembler;
        this.userService = userService;
        this.coinService = coinService;
    }

    public CollectionModel<EntityModel<WalletDto>> findAllWallets() {
        List<WalletDto> allWallets = StreamSupport.stream(walletRepository.findAll().spliterator(), false)
                .map(WalletDto::new).collect(Collectors.toList());
        log.info("Retrieved all WalletDtos from service");
        return walletDtoAssembler.toCollectionModel(allWallets);
    }

    public EntityModel<WalletDto> getByUUID(UUID uuid) {
        WalletDto walletDto = walletRepository.findById(uuid).map(WalletDto::new)
                .orElseThrow(() -> new WalletNotFoundException(uuid));
        log.info("WalletDto with uuid = " + uuid + " returned in service = " + walletDto);
        return walletDtoAssembler.toModel(walletDto);
    }

    public CollectionModel<EntityModel<WalletDto>> getWalletsByProvider(String provider) {
        log.info("Retrieved all WalletDtos with provider = '" + provider + "' from service");
        return walletDtoAssembler.toCollectionModel(
                walletRepository.findByProvider(provider)
                        .stream().map(WalletDto::new).collect(Collectors.toList()));
    }

    public CollectionModel<EntityModel<WalletDto>> getWalletsByCoinsRange(Integer from, Integer to) {
        if (from < 0 && to < 0) {
            throw new WalletIllegalCoinRangeException(from, to);
        } else if (from < 0) {
            throw new WalletIllegalCoinRangeException(from);
        } else if (to < 0) {
            throw new WalletIllegalCoinRangeException(to);
        }

        List<WalletEntity> wallets = (0 == from) ?
                walletRepository.findAllByCoinsRangeFromEquals0(from, to)
                : walletRepository.findAllByCoinsRangeFromNotEquals0(from, to);
        log.info("Retrieved all Wallets with " + from + "-" + to + " concrete coins in service = " + wallets);
        return walletDtoAssembler.toCollectionModel(
                wallets.stream().map(WalletDto::new).collect(Collectors.toList()));
    }

    public CollectionModel<EntityModel<WalletDto>> getWalletsByOwnerIdentityNumber(Long identityNumber) {
        log.info("Retrieved all walletDtos owned by owner with identityNumber = " + identityNumber);
        return walletDtoAssembler.toCollectionModel(
                walletRepository.findAllByOwnerIdentityNumber(identityNumber)
                        .stream().map(WalletDto::new).collect(Collectors.toList()));
    }

    public void deleteWalletByUUID(UUID uuid) {
        WalletEntity wallet = walletRepository.findById(uuid)
                .orElseThrow(() -> new WalletNotFoundException(uuid));
        coinService.removeCoinsAttributionFromWallet(wallet);
        userService.removeUsersAttributionToWallet(wallet);
        walletRepository.delete(wallet);
        coinService.deleteAllUnattributedCoins();
        log.info("Wallet with uuid = " + uuid + " deleted in service = " + wallet);
    }
}
