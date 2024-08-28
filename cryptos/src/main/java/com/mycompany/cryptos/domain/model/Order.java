/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.domain.model;

import com.mycompany.cryptos.domain.enums.OrderType;
import java.math.BigDecimal;

/**
 *
 * @author User
 */
public class Order {
    private final String orderID;
    private final String userID;
    private final Crypto crypto;
    private final BigDecimal amount;
    private final BigDecimal price;
    private final OrderType type;

    public Order(String orderID, String userID, Crypto crypto, BigDecimal amount, BigDecimal price, OrderType type) {
        this.orderID = orderID;
        this.userID = userID;
        this.crypto = crypto;
        this.amount = amount;
        this.price = price;
        this.type = type;
    }

    public String getOrderID() {
        return orderID;
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

    public OrderType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Order{" + "orderID=" + orderID + ", userID=" + userID + ", crypto=" + crypto + ", amount=" + amount + ", price=" + price + ", type=" + type + '}';
    }
    
}
