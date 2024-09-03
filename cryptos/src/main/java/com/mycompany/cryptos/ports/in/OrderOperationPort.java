/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.ports.in;

import com.mycompany.cryptos.domain.model.Crypto;
import com.mycompany.cryptos.domain.model.Order;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author User
 */
public interface OrderOperationPort {
    void setBuyOrder(String userID, Crypto crypto, BigDecimal amount, BigDecimal maxPrice);
    void setSellOrder(String userID, Crypto crypto, BigDecimal amount, BigDecimal minPrice);
    List<Order> getAllOrders();
}
