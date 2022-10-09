package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.CoinEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CoinRepository extends CrudRepository<CoinEntity, UUID> {
}
