/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author User
 */
public class Crypto {
    private final String cryptoID;
    private final String name;
    private final String symbol;
    private BigDecimal actualPrice;

    public Crypto(String name,String symbol, BigDecimal price) {
        this.cryptoID =  symbol + '@' + symbol.hashCode();
        this.name = name;
        this.symbol = symbol;
        this.actualPrice = price;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.cryptoID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Crypto other = (Crypto) obj;
        if (!Objects.equals(this.cryptoID, other.cryptoID)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Crypto{" + "cryptoID=" + cryptoID + ", name=" + name + ", symbol=" + symbol + ", actualPrice=" + actualPrice + '}';
    }

}
