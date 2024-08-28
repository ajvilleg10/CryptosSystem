/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 *
 * @author User
 */
public class Transaction {
    private final String transactionID;
    private final String userID;
    private final Crypto crypto;
    private final BigDecimal amount;
    private final BigDecimal price;
    private final boolean isBuy;

    public Transaction( String userID, Crypto crypto, BigDecimal amount, BigDecimal price, boolean isBuy) {
        this.transactionID = UUID.randomUUID().toString();
        this.userID = userID;
        this.crypto = crypto;
        this.amount = amount;
        this.price = price;
        this.isBuy = isBuy;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getUserID() {
        return userID;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isIsBuy() {
        return isBuy;
    }

    @Override
    public String toString() {
        return "Transaction{" + "transactionID=" + transactionID + ", userID=" + userID + ", crypto=" + crypto + ", amount=" + amount + ", price=" + price + ", isBuy=" + isBuy + '}';
    }
    
    
}
