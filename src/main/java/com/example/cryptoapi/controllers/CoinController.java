package com.example.cryptoapi.controllers;

import com.example.cryptoapi.dtos.CoinDto;
import com.example.cryptoapi.services.CoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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

    @DeleteMapping("/{uuid}")
    @Operation(summary = "DELETE Coin by UUID and all references to the coin")
    public ResponseEntity<String> deleteCoinByUUID(@PathVariable UUID uuid) {
        coinService.deleteCoinByUUID(uuid);
        return ResponseEntity.ok("Coin with uuid = " + uuid + " deleted");
    }

    @PostMapping("/{coinType}/{walletUUID}")
    @Operation(summary = "POST request to create a new Coin of a specific CoinType and insert to a specific Wallet")
    public ResponseEntity<?> createCoin(@PathVariable @NotNull String coinType,
                                        @PathVariable @NotNull UUID walletUUID) {
        EntityModel<CoinDto> coin = coinService.createCoin(coinType, walletUUID);
        try {
            return ResponseEntity
                    .created(new URI(coin.getRequiredLink(IanaLinkRelations.SELF).getHref())).body(coin);
        } catch (URISyntaxException uriSyntaxException) {
            return ResponseEntity
                    .badRequest().body("Failed to add Coin of type = " + coinType + " to Wallet with uuid = " + walletUUID);
        }
    }

    @PutMapping("/{coinUUID}")
    @Operation(summary = "PUT request to move concrete Coins between Wallets")
    public ResponseEntity<EntityModel<CoinDto>> changeStoringWalletOfCoin(
            @PathVariable @NotNull UUID coinUUID,
            @RequestParam(required = true, defaultValue = "") @NotNull UUID fromWallet,
            @RequestParam(required = true, defaultValue = "") @NotNull UUID toWallet) {
        return ResponseEntity.ok(coinService.changeStoringWalletOfCoin(coinUUID, fromWallet, toWallet));
    }
}
