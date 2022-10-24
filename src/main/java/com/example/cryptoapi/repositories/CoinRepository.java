package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.CoinEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface CoinRepository extends CrudRepository<CoinEntity, UUID> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from COIN_ENTITY where STORED_IN_WALLET_ENTITY_ID is null")
    void deleteAllUnattributedCoins();
}
