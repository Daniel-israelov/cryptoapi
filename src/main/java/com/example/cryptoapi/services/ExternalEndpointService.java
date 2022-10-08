package com.example.cryptoapi.services;

import com.example.cryptoapi.entities.CoinType;
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
 * This Service pulls {@link CoinType} data from an external endpoint.
 */
@Slf4j
@Service
public class ExternalEndpointService {

    private final String externalEndpointUrl = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd";
    private final RestTemplate template;

    public ExternalEndpointService(RestTemplateBuilder templateBuilder) { this.template = templateBuilder.build(); }

    /**
     * This method returns {@link CoinType}s from an external endpoint asynchronously
     * as a {@link CompletableFuture<List>} of {@link CoinType}s.
     * @return all {@link CoinType}s from the external endpoint.
     */
    @Async
    public CompletableFuture<List<CoinType>> pullExternalData() {

        List coinTypesAsJson = this.template.getForObject(this.externalEndpointUrl, List.class);
        List<CoinType> coinTypes = new ArrayList<>();
        assert coinTypesAsJson != null;
        int numberOfCoinTypes = coinTypesAsJson.size();
        CoinType tempCoinType;

        for (int i = 0; i < numberOfCoinTypes; i++) {

            tempCoinType = Parse((LinkedHashMap) coinTypesAsJson.get(i));
            coinTypes.add(tempCoinType);
            log.info("Parsed CoinType {}/{} = {}", (i + 1), numberOfCoinTypes, tempCoinType);
        }
        return CompletableFuture.completedFuture(coinTypes);
    }

    private CoinType Parse(LinkedHashMap dataToParse) {
        return new CoinType(dataToParse.get("name").toString(),
                            dataToParse.get("image").toString(),
                            Double.parseDouble(dataToParse.get("current_price").toString()),
                            Double.parseDouble(dataToParse.get("market_cap").toString()),
                            Double.parseDouble(dataToParse.get("high_24h").toString()),
                            Double.parseDouble(dataToParse.get("low_24h").toString()),
                            Double.parseDouble(dataToParse.get("price_change_24h").toString()));
    }
}
