package com.example.cryptoapi.controllers;

import com.example.cryptoapi.entities.CoinTypeEntity;
import com.example.cryptoapi.services.CoinTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cointypes")
@Tag(name = "CoinType Controller", description = "CoinType CRUD controller")
public class CoinTypeController {

    private final CoinTypeService coinTypeService;

    public CoinTypeController(CoinTypeService coinTypeService) { this.coinTypeService = coinTypeService; }

    @GetMapping
    @Operation(summary = "GET all CoinTypes")
    public ResponseEntity<CollectionModel<EntityModel<CoinTypeEntity>>> getAllCoinTypes() {
        return ResponseEntity.ok(coinTypeService.getAllCoinTypes());
    }

    @GetMapping("/{name}")
    @Operation(summary = "GET CoinType by name")
    public ResponseEntity<EntityModel<CoinTypeEntity>> getByName(@PathVariable String name) {
        return ResponseEntity.ok(coinTypeService.getByName(name));
    }

    @GetMapping("/price")
    @Operation(summary = "GET CoinTypes by price range")
    public ResponseEntity<CollectionModel<EntityModel<CoinTypeEntity>>> getByPriceRange(
            @RequestParam(required = false, defaultValue = "0") Double from,
            @RequestParam(required = false, defaultValue = "1000000") Double to) {
        return ResponseEntity.ok(coinTypeService.getByPriceRange(from, to));
    }
}
