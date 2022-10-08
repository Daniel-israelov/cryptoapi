package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.Coin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CoinRepository extends CrudRepository<Coin, UUID> {
}
