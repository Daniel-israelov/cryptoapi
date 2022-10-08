package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.CoinType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinTypeRepository extends CrudRepository<CoinType, Long> {

    CoinType findByName(String name);
}
