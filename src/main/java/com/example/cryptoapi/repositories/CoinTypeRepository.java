package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.CoinTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinTypeRepository extends CrudRepository<CoinTypeEntity, Long> {

    CoinTypeEntity findByName(String name);
}
