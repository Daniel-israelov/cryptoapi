package com.example.cryptoapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * This data class represents a crypto coin type and not a concrete coin. e.g. Bitcoin, Ethereum, etc.
 */
@Entity
@NoArgsConstructor
@Data
public class CoinType {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String imageUrl;
    private Double currentPrice;
    private Double marketCap;
    private Double high24h;
    private Double low24h;
    private Double priceChange24h;
    private LocalDateTime lastUpdated;

    public CoinType(String name, String imageUrl, Double currentPrice, Double marketCap,
                    Double high24h, Double low24h, Double priceChange24h) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.currentPrice = currentPrice;
        this.marketCap = marketCap;
        this.high24h = high24h;
        this.low24h = low24h;
        this.priceChange24h = priceChange24h;
        this.lastUpdated = LocalDateTime.now();
    }
}
