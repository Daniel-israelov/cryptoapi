package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.WalletEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WalletRepository extends CrudRepository<WalletEntity, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM wallet_entity WHERE provider ILIKE :provider")
    List<WalletEntity> findByProvider(String provider);

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
