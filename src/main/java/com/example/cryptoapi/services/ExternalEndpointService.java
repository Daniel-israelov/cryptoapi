package com.example.cryptoapi.services;

import com.example.cryptoapi.entities.CoinTypeEntity;
import com.example.cryptoapi.exceptions.ApiServerNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This Service pulls {@link CoinTypeEntity} data from an external endpoint.
 */
@SuppressWarnings("rawtypes")
@Slf4j
@Service
public class ExternalEndpointService {

    @Value("${coingecko.url}")
    private String externalEndpointUrl;
    @Value("${coingecko.ping}")
    private String externalEndpointPingUrl;
    private final RestTemplate template;

    public ExternalEndpointService(@NotNull RestTemplateBuilder templateBuilder) {
        this.template = templateBuilder.build();
    }

    /**
     * This method executes a ping to the external API server to check if the server is up & available.<br>
     * If the server is not available for any reason (status code != 200), a new {@link ApiServerNotAvailableException} will be thrown.
     *
     * @throws IOException If the parsed URL fails to comply with the specific syntax.
     */
    @PostConstruct
    public void ping() throws IOException {
        URL pingUrl = new URL(externalEndpointPingUrl);
        HttpURLConnection connection = (HttpURLConnection) pingUrl.openConnection();
        connection.setRequestMethod("GET");
        log.info("Pinging endpoint API...");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            log.info("Endpoint server is active (Response code " + responseCode + ")");
        } else {
            throw new ApiServerNotAvailableException(responseCode);
        }
    }

    /**
     * This method returns {@link CoinTypeEntity}s from an external endpoint asynchronously
     * as a {@link CompletableFuture<List>} of {@link CoinTypeEntity}s.
     *
     * @return all {@link CoinTypeEntity}s from the external endpoint.
     */
    @Async
    public CompletableFuture<List<CoinTypeEntity>> pullExternalData() {
        log.info("Remote API URL - '" + externalEndpointUrl + "'");

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

    private CoinTypeEntity Parse(@NotNull LinkedHashMap dataToParse) {
        return new CoinTypeEntity(dataToParse.get("name").toString(),
                dataToParse.get("image").toString(),
                Double.parseDouble(dataToParse.get("current_price").toString()),
                Double.parseDouble(dataToParse.get("market_cap").toString()),
                Double.parseDouble(dataToParse.get("high_24h").toString()),
                Double.parseDouble(dataToParse.get("low_24h").toString()),
                Double.parseDouble(dataToParse.get("price_change_24h").toString()));
    }
}
