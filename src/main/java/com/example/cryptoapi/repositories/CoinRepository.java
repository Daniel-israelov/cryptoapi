package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.CoinEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoinRepository extends CrudRepository<CoinEntity, UUID> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from COIN_ENTITY where STORED_IN_WALLET_ENTITY_ID is null")
    void deleteAllUnattributedCoins();

    @NotNull
    Optional<CoinEntity> findById(@NotNull UUID uuid);

    @Query(nativeQuery = true, value = """
            select *
            from coin_entity
            where coin_type_entity_id = (select id from coin_type_entity where name ilike :coinTypeName)
            """)
    List<CoinEntity> getByCoinType(String coinTypeName);

    @Query(nativeQuery = true, value = """
            select *
            from coin_entity
            where stored_in_wallet_entity_id = :walletUUIDAsString
            """)
    List<CoinEntity> getByWalletUUID(String walletUUIDAsString);

    @Query(nativeQuery = true, value = """
            select *
            from coin_entity
            where stored_in_wallet_entity_id in (
                select wallet_id
                from users_wallets
                where user_id = (
                    select id
                    from user_entity
                    where identity_number = :userIdentityNumber))
            """)
    List<CoinEntity> getByUserIdentityNumber(Long userIdentityNumber);
}
