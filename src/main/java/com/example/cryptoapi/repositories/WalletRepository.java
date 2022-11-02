package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.WalletEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends CrudRepository<WalletEntity, UUID> {

    @NotNull Optional<WalletEntity> findById(@NotNull UUID uuid);

    @Query(nativeQuery = true, value = "SELECT * FROM wallet_entity WHERE provider ILIKE :provider")
    List<WalletEntity> findByProvider(String provider);

    /**
     * This method retrieves all the wallets which contains a number of coins within a certain range,
     * including empty wallets.
     * The procedure:
     *  1. pair each non-empty wallet with the number of coins it holds from WALLET_ENTITY_COIN_ENTITIES table
     *  2. get the uuid of each of the wallets from section (1) which fulfills the range condition
     *  3. retrieve all the relevant wallets from WALLET_ENTITY table
     *     corresponding to the uuids received in section (2) AND all the empty wallets
     * @param from
     * @param to
     * @return list of all the wallets with number of coins between the given range (including empty wallets)
     */
    @Query(nativeQuery = true, value = """
            select *
            from wallet_entity
            where (id = any(
                select wallet_entity_id id
                from
                    (select wallet_entity_id, count(*) count
                    from wallet_entity_coin_entities
                    group by wallet_entity_id)
                where count between :from and :to))
            or (id not in (
                select wallet_entity_id
                from wallet_entity_coin_entities
                group by wallet_entity_id))
            """)
    List<WalletEntity> findAllByCoinsRangeFromEquals0(Integer from, Integer to);

    /**
     * This method retrieves all the wallets which contains a number of coins within a certain range,
     * not including empty wallets.
     * The procedure:
     *  1. pair each non-empty wallet with the number of coins it holds from WALLET_ENTITY_COIN_ENTITIES table
     *  2. get the uuid of each of the wallets from section (1) which fulfills the range condition
     *  3. retrieve all the relevant wallets from WALLET_ENTITY table
     * @param from
     * @param to
     * @return list of all the wallets with number of coins between the given range (not including empty wallets)
     */
    @Query(nativeQuery = true, value = """
            select *
            from wallet_entity
            where id = any(
                select wallet_entity_id id
                from
                    (select wallet_entity_id, count(*) count
                    from wallet_entity_coin_entities
                    group by wallet_entity_id)
                where count between :from and :to)
            """)
    List<WalletEntity> findAllByCoinsRangeFromNotEquals0(Integer from, Integer to);

    /**
     * This method retrieves all the wallets held by a specific user corresponding to a given identity number
     * The procedure:
     *  1. get the required user from USER_ENTITY table
     *  2. get the wallets linked to the user in USERS_WALLETS table
     *  3. retrieve all the wallets that returned in section (2) from WALLET_ENTITY table
     * @param ownerIdentityNumber
     * @return list of all the wallets that the desired user currently holds
     */
    @Query(nativeQuery = true, value = """
            select *
            from wallet_entity
            where id = any(
                select wallet_id
                from users_wallets
                where user_id =
                    (select id
                    from user_entity
                    where identity_number = :ownerIdentityNumber))
            """)
    List<WalletEntity> findAllByOwnerIdentityNumber(Long ownerIdentityNumber);
}
