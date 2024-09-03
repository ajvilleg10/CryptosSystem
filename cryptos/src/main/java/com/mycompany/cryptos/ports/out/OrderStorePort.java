/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.ports.out;

import com.mycompany.cryptos.domain.model.Order;
import com.mycompany.cryptos.domain.model.Transaction;
import java.util.List;

/**
 *
 * @author User
 */
public interface OrderStorePort {
    void storeOrder(Order order);
    List<Order> findAllOrders();
    List<Order> fetchOrdersForUser(String userID);
}
