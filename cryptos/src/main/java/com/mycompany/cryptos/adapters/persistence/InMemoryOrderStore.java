/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.adapters.persistence;

import com.mycompany.cryptos.domain.model.Order;
import com.mycompany.cryptos.ports.out.OrderStorePort;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class InMemoryOrderStore implements OrderStorePort {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public void storeOrder(Order order) {
        orders.add(order);
    }

    @Override
    public List<Order> findAllOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public List<Order> fetchOrdersForUser(String userID) {
        List<Order> userOrders = new ArrayList<>();
        orders.stream().filter(order -> (order.getUserID().equals(userID))).forEachOrdered(order -> {
            userOrders.add(order);
        });
        return userOrders;

    }
    
}
