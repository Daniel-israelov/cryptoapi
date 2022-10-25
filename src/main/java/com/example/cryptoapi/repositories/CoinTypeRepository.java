package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.CoinTypeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoinTypeRepository extends CrudRepository<CoinTypeEntity, Long> {

    @Query(nativeQuery = true, value = "select * from coin_type_entity where name ilike :name")
    Optional<CoinTypeEntity> findByName(String name);

    @Query(nativeQuery = true, value = """
            select *
            from coin_type_entity
            where current_price between :from and :to
            """)
    List<CoinTypeEntity> findByPriceRange(Double from, Double to);
}
