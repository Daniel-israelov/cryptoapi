package com.example.cryptoapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

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
}
