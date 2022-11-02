package com.example.cryptoapi.services;

import com.example.cryptoapi.assemblers.WalletDtoAssembler;
import com.example.cryptoapi.dtos.WalletDto;
import com.example.cryptoapi.entities.UserEntity;
import com.example.cryptoapi.entities.WalletEntity;
import com.example.cryptoapi.exceptions.WalletIllegalCoinRangeException;
import com.example.cryptoapi.exceptions.WalletNotFoundException;
import com.example.cryptoapi.repositories.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.*;
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

    /**
     * This method is responsible for creating a new {@link WalletEntity} and connect to a {@link UserEntity} as an owner
     * @param identityNumber
     * @param optionalWalletEntity
     * @return {@link EntityModel<WalletDto>} representing the created {@link WalletEntity}
     */
    public EntityModel<WalletDto> createWalletAndConnectToUser(Long identityNumber,
                                                               Optional<WalletEntity> optionalWalletEntity) {
        log.info("Sending information from WalletService to UserService to link between user(identityNumber="
                + identityNumber + ") and the newly created wallet = " + optionalWalletEntity);
        if (optionalWalletEntity.isEmpty()) {
            log.info("RequestBody was empty, thus could not create a wallet. Insert a 'provider' field.");
            return null;
        }
        walletRepository.save(optionalWalletEntity.get());
        userService.linkWalletToUserByIdentityNumber(identityNumber, optionalWalletEntity.get());
        log.info("Wallet created and linked to user(identityNumber=" + identityNumber
                + "). wallet = " + optionalWalletEntity);
        return walletDtoAssembler.toModel(new WalletDto(optionalWalletEntity.get()));
    }

    public EntityModel<WalletDto> updateWallet(UUID uuid, Optional<WalletEntity> optionalWalletEntity) {
        log.info("WalletEntity (wrapped by Optional<>) received in PUT request body = " + optionalWalletEntity);
        WalletEntity walletToUpdate = walletRepository.findById(uuid)
                .orElseThrow(() -> new WalletNotFoundException(uuid));
        if (optionalWalletEntity.isPresent()) {
            walletToUpdate.setProvider(optionalWalletEntity.get().getProvider() == null ?
                    walletToUpdate.getProvider() : optionalWalletEntity.get().getProvider());
            walletRepository.save(walletToUpdate);
        }
        log.info("Updated Wallet info after PUT request = " + walletToUpdate);
        return walletDtoAssembler.toModel(new WalletDto(walletToUpdate));
    }

    /**
     * This method updates the list of {@link UserEntity} owners in a desired {@link WalletEntity}
     * by removing existing {@link UserEntity}s or adding new {@link UserEntity}s as owners
     * @param uuid
     * @param attach
     * @param detach
     */
    public EntityModel<WalletDto> updateWalletOwners(UUID uuid, Set<Long> attach, Set<Long> detach) {
        log.info("Updating the owners of wallet with uuid = " + uuid + " . . .");
        WalletEntity walletToUpdate = walletRepository.findById(uuid)
                .orElseThrow(() -> new WalletNotFoundException(uuid));
        log.info("Updating the owners of wallet = " + walletToUpdate);

        Set<Long> identityNumbersInBothSetsToIgnore = new HashSet<>();
        for (Long identityNumber : attach) {
            if (detach.contains(identityNumber)) {
                identityNumbersInBothSetsToIgnore.add(identityNumber);
            }
        }
        attach.removeAll(identityNumbersInBothSetsToIgnore);
        detach.removeAll(identityNumbersInBothSetsToIgnore);
        log.info("identityNumbers of users to attach as owners = " + attach);
        log.info("identityNumbers of users to detach from owners = " + detach);
        userService.updateWalletOwnersByIdentityNumbers(walletToUpdate, attach, detach);
        walletRepository.save(walletToUpdate);

        return walletDtoAssembler.toModel(new WalletDto(walletToUpdate));
    }

    /**
     * This method throws a {@link WalletNotFoundException} in case there is no {@link WalletEntity}
     * corresponding to the given uuid
     * @param uuid
     */
    public void confirmWalletExistenceByUUID(UUID uuid) {
        Optional<WalletEntity> optionalWalletEntity = walletRepository.findById(uuid);
        if (optionalWalletEntity.isEmpty()) {
            throw new WalletNotFoundException(uuid);
        }
    }
}
