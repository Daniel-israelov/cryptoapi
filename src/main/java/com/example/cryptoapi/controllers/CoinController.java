package com.example.cryptoapi.controllers;

import com.example.cryptoapi.dtos.CoinDto;
import com.example.cryptoapi.services.CoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/coins")
@Tag(name = "Coin Controller", description = "Coin CRUD controller")
public class CoinController {

    private final CoinService coinService;

    public CoinController(CoinService coinService) { this.coinService = coinService; }

    @GetMapping
    @Operation(summary = "GET all Coins")
    public ResponseEntity<CollectionModel<EntityModel<CoinDto>>> getAllCoins() {
        return ResponseEntity.ok(coinService.getAllCoins());
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "GET Coin by UUID")
    public ResponseEntity<EntityModel<CoinDto>> getByUUID(@PathVariable UUID uuid) {
        return ResponseEntity.ok(coinService.getByUUID(uuid));
    }

    @GetMapping("/bytype/{coinTypeName}")
    @Operation(summary = "GET Coins by CoinType name")
    public ResponseEntity<CollectionModel<EntityModel<CoinDto>>> getByCoinType(@PathVariable String coinTypeName) {
        return ResponseEntity.ok(coinService.getByCoinType(coinTypeName));
    }

    @GetMapping("/bywallet/{walletUUID}")
    @Operation(summary = "GET Coin by Wallet uuid")
    public ResponseEntity<CollectionModel<EntityModel<CoinDto>>> getByWalletUUID(@PathVariable UUID walletUUID) {
        return ResponseEntity.ok(coinService.getByWalletUUID(walletUUID));
    }

    @GetMapping("/byuser/{userIdentityNumber}")
    @Operation(summary = "GET Coin by User identityNumber")
    public ResponseEntity<CollectionModel<EntityModel<CoinDto>>> getByUserIdentityNumber(@PathVariable Long userIdentityNumber) {
        return ResponseEntity.ok(coinService.getByUserIdentityNumber(userIdentityNumber));
    }
}
