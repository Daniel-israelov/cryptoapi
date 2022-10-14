package com.example.cryptoapi.controllers;

import com.example.cryptoapi.dtos.WalletDto;
import com.example.cryptoapi.services.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wallets")
@Tag(name = "Wallet Controller", description = "Wallet CRUD controller")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) { this.walletService = walletService; }

    @GetMapping
    @Operation(summary = "GET all wallets")
    public ResponseEntity<CollectionModel<EntityModel<WalletDto>>> getAllWallets() {
        return ResponseEntity.ok(walletService.findAllWallets());
    }

    @GetMapping("/id/{uuid}")
    @Operation(summary = "GET Wallet by UUID")
    public ResponseEntity<EntityModel<WalletDto>> getByUUID(@PathVariable UUID uuid) {
        return ResponseEntity.ok(walletService.getByUUID(uuid));
    }

    @GetMapping("/{provider}")
    @Operation(summary = "GET Wallet by Provider")
    public ResponseEntity<CollectionModel<EntityModel<WalletDto>>> getWalletsByProvider(@PathVariable String provider) {
        return ResponseEntity.ok(walletService.getWalletsByProvider(provider));
    }

    @GetMapping("/coins")
    @Operation(summary = "GET all wallets with number of coins in a certain range")
    public ResponseEntity<CollectionModel<EntityModel<WalletDto>>> getWalletsByCoinsRange(
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "100") Integer to) {
        return ResponseEntity.ok(walletService.getWalletsByCoinsRange(from, to));
    }

    @GetMapping("/owner/{identityNumber}")
    @Operation(summary = "GET all wallets owned by owner with {identityNumber}")
    public ResponseEntity<CollectionModel<EntityModel<WalletDto>>> getWalletsByOwnerIdentityNumber(
            @PathVariable Long identityNumber) {
        return ResponseEntity.ok(walletService.getWalletsByOwnerIdentityNumber(identityNumber));
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete wallet by uuid")
    public ResponseEntity<String> deleteWalletByUUID(@PathVariable UUID uuid) {
        walletService.deleteWalletByUUID(uuid);
        return ResponseEntity.ok("Wallet with uuid = " + uuid + " deleted");
    }

    // TODO: POST, PUT methods. (fix DELETE in service).
}