/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.adapters.persistence;

import com.mycompany.cryptos.domain.model.Crypto;
import com.mycompany.cryptos.ports.out.MarketService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author User
 */
public class MarketPriceService implements MarketService {
    private final Map<String, BigDecimal> cryptoPrices = new HashMap<>();
    private final Random random = new Random();

    public MarketPriceService() {
        cryptoPrices.put("BTC", new BigDecimal("50000"));
        cryptoPrices.put("ETH", new BigDecimal("3000"));
    }
    
    public void fluctuatePrices() {
        cryptoPrices.entrySet().forEach(entry -> {
            BigDecimal currentPrice = entry.getValue();
            double randomNumber = random.nextDouble() * 100 - 500;
            BigDecimal fluctuation = BigDecimal.valueOf(randomNumber);
            cryptoPrices.put(entry.getKey(), currentPrice.add(fluctuation));
        });
    }

    @Override
    public BigDecimal getMarketPrice(Crypto crypto) {
        if (!cryptoPrices.containsKey(crypto.getSymbol())) {
            throw new IllegalArgumentException("Unknown cryptocurrency symbol: " + crypto.getSymbol());
        }
        return cryptoPrices.getOrDefault(crypto.getSymbol(), BigDecimal.ZERO);
    }
    
}
