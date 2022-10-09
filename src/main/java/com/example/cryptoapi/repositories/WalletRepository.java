package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.WalletEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends CrudRepository<WalletEntity, UUID> {

    WalletEntity findByProvider(String provider);
}
