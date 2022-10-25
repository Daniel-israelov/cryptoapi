package com.example.cryptoapi.services;

import com.example.cryptoapi.assemblers.CoinTypeAssembler;
import com.example.cryptoapi.entities.CoinTypeEntity;
import com.example.cryptoapi.exceptions.CoinTypeNotFoundException;
import com.example.cryptoapi.repositories.CoinTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CoinTypeService {

    private final CoinTypeRepository coinTypeRepository;
    private final CoinTypeAssembler coinTypeAssembler;

    public CoinTypeService(CoinTypeRepository coinTypeRepository, CoinTypeAssembler coinTypeAssembler) {
        this.coinTypeRepository = coinTypeRepository;
        this.coinTypeAssembler = coinTypeAssembler;
    }

    public CollectionModel<EntityModel<CoinTypeEntity>> getAllCoinTypes() {
        log.info("Retreived all CoinTypeEntities from service");
        return coinTypeAssembler.toCollectionModel(coinTypeRepository.findAll());
    }

    public EntityModel<CoinTypeEntity> getByName(String name) {
        log.info("Service trying to get CoinType with name = " + name + " . . .");
        CoinTypeEntity coinType = coinTypeRepository.findByName(name)
                .orElseThrow(() -> new CoinTypeNotFoundException(name));
        log.info("CoinType with name = " + name + " returned in service = " + coinType);
        return coinTypeAssembler.toModel(coinType);
    }

    public CollectionModel<EntityModel<CoinTypeEntity>> getByPriceRange(Double from, Double to) {
        log.info("Service trying to get CoinTypes by price range = " + from + " -> " + to + " . . .");
        List<CoinTypeEntity> coinTypes = coinTypeRepository.findByPriceRange(from ,to);
        log.info("CoinTypes with price range between " + from + " -> " + to + " retrieved = " + coinTypes);
        return coinTypeAssembler.toCollectionModel(coinTypes);
    }
}
