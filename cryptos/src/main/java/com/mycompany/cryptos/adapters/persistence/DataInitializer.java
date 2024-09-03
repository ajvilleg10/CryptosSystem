/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.adapters.persistence;

import com.mycompany.cryptos.domain.model.Crypto;
import com.mycompany.cryptos.ports.out.MarketService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author User
 */
public class DataInitializer {
    private final MarketService marketDataService;
    private final Map<String, BigDecimal> initialCryptoSupply = new HashMap<>();

    public DataInitializer(MarketService marketDataService) {
        this.marketDataService = marketDataService;
        initializeCryptoSupply();
    }

    public List<Crypto> initializeCryptos() {
        List<Crypto> cryptos = new ArrayList<>();
        cryptos.add(new Crypto("Bitcoin", "BTC", marketDataService.getMarketPrice(new Crypto("Bitcoin", "BTC", BigDecimal.ZERO))));
        cryptos.add(new Crypto("Ethereum", "ETH", marketDataService.getMarketPrice(new Crypto("Ethereum", "ETH", BigDecimal.ZERO))));
        return cryptos;
    }
    
    private void initializeCryptoSupply() {
        initialCryptoSupply.put("BTC", new BigDecimal("100"));
        initialCryptoSupply.put("ETH", new BigDecimal("500")); 
    }

    public BigDecimal getAvailableSupply(String symbol) {
        return initialCryptoSupply.getOrDefault(symbol, BigDecimal.ZERO);
    }

    public void updateSupply(String symbol, BigDecimal amount) {
        BigDecimal actualSupply = initialCryptoSupply.getOrDefault(symbol, BigDecimal.ZERO);
        initialCryptoSupply.put(symbol, actualSupply.subtract(amount));
    }
}
