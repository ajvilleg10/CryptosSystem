/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.domain.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author User
 */
public class Wallet {
    private BigDecimal balance;
    private final Map<Crypto, BigDecimal> cryptos;

    public Wallet(BigDecimal balance) {
        this.balance = balance;
        this.cryptos = new HashMap<>();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }
    
    public void withdraw(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public Map<Crypto, BigDecimal> getCryptos() {
        return cryptos;
    }

    public void addCrypto(Crypto crypto, BigDecimal amount) {
        cryptos.merge(crypto, amount, BigDecimal::add);
    }

    public void withdrawCrypto(Crypto crypto, BigDecimal amount) {
        cryptos.merge(crypto, amount.negate(), BigDecimal::add);
    }

    public boolean hasEnoughCrypto(Crypto crypto, BigDecimal amount) {
        return cryptos.getOrDefault(crypto, BigDecimal.ZERO).compareTo(amount) >= 0;
    }
    
}
