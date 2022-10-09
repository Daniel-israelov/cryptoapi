package com.example.cryptoapi.services;

import com.example.cryptoapi.entities.CoinTypeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This Service pulls {@link CoinTypeEntity} data from an external endpoint.
 */
@Slf4j
@Service
public class ExternalEndpointService {

    private final String externalEndpointUrl = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd";
    private final RestTemplate template;

    public ExternalEndpointService(RestTemplateBuilder templateBuilder) { this.template = templateBuilder.build(); }

    /**
     * This method returns {@link CoinTypeEntity}s from an external endpoint asynchronously
     * as a {@link CompletableFuture<List>} of {@link CoinTypeEntity}s.
     * @return all {@link CoinTypeEntity}s from the external endpoint.
     */
    @Async
    public CompletableFuture<List<CoinTypeEntity>> pullExternalData() {

        List coinTypesAsJson = this.template.getForObject(this.externalEndpointUrl, List.class);
        List<CoinTypeEntity> coinTypeEntities = new ArrayList<>();
        assert coinTypesAsJson != null;
        int numberOfCoinTypes = coinTypesAsJson.size();
        CoinTypeEntity tempCoinTypeEntity;

        for (int i = 0; i < numberOfCoinTypes; i++) {

            tempCoinTypeEntity = Parse((LinkedHashMap) coinTypesAsJson.get(i));
            coinTypeEntities.add(tempCoinTypeEntity);
            log.info("Parsed CoinType {}/{} = {}", (i + 1), numberOfCoinTypes, tempCoinTypeEntity);
        }
        return CompletableFuture.completedFuture(coinTypeEntities);
    }

    private CoinTypeEntity Parse(LinkedHashMap dataToParse) {
        return new CoinTypeEntity(dataToParse.get("name").toString(),
                            dataToParse.get("image").toString(),
                            Double.parseDouble(dataToParse.get("current_price").toString()),
                            Double.parseDouble(dataToParse.get("market_cap").toString()),
                            Double.parseDouble(dataToParse.get("high_24h").toString()),
                            Double.parseDouble(dataToParse.get("low_24h").toString()),
                            Double.parseDouble(dataToParse.get("price_change_24h").toString()));
    }
}
