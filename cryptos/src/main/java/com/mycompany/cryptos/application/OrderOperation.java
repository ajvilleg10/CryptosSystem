/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.application;

import com.mycompany.cryptos.domain.model.Crypto;
import com.mycompany.cryptos.domain.model.Order;
import com.mycompany.cryptos.domain.model.Transaction;
import com.mycompany.cryptos.domain.model.User;
import com.mycompany.cryptos.domain.model.enums.OrderType;
import com.mycompany.cryptos.ports.in.OrderOperationPort;
import com.mycompany.cryptos.ports.out.MarketService;
import com.mycompany.cryptos.ports.out.OrderStorePort;
import com.mycompany.cryptos.ports.out.TransactionStorePort;
import com.mycompany.cryptos.ports.out.UserStorePort;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author User
 */
public class OrderOperation implements OrderOperationPort{

    private final UserStorePort userStore;
    private final OrderStorePort orderStore;
    private final TransactionStorePort transactionStore;

    public OrderOperation(UserStorePort userStore, OrderStorePort orderStore, TransactionStorePort transactionStore) {
        this.userStore = userStore;
        this.orderStore = orderStore;
        this.transactionStore = transactionStore;
    }

    public void matchOrders() {
        List<Order> buyOrders = orderStore.findAllOrders().stream()
            .filter(order -> order.getType() == OrderType.BUY)
            .sorted((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()))
             .collect(Collectors.toList());

        List<Order> sellOrders = orderStore.findAllOrders().stream()
            .filter(order -> order.getType() == OrderType.SELL)
            .sorted((o1, o2) -> o1.getPrice().compareTo(o2.getPrice()))
            .collect(Collectors.toList());

        for (Order buyOrder : buyOrders) {
            Iterator<Order> sellOrderIterator = sellOrders.iterator();
            while (sellOrderIterator.hasNext()) {
                Order sellOrder = sellOrderIterator.next();

                if (buyOrder.getCrypto().equals(sellOrder.getCrypto()) &&
                        buyOrder.getPrice().compareTo(sellOrder.getPrice()) >= 0) {

                    BigDecimal tradeAmount = buyOrder.getAmount().min(sellOrder.getAmount());
                    BigDecimal tradePrice = sellOrder.getPrice();

                    executeOrder(buyOrder, sellOrder, tradeAmount, tradePrice);

                    buyOrder = new Order(buyOrder.getUserID(), buyOrder.getCrypto(),
                            buyOrder.getAmount().subtract(tradeAmount), buyOrder.getPrice(), OrderType.BUY);
                    sellOrder = new Order(sellOrder.getUserID(), sellOrder.getCrypto(),
                            sellOrder.getAmount().subtract(tradeAmount), sellOrder.getPrice(), OrderType.SELL);

                    if (sellOrder.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                        sellOrderIterator.remove();
                    }

                    if (buyOrder.getAmount().compareTo(BigDecimal.ZERO) == 0) break;
                }
            }
        }
    }
    
    public void executeOrder(Order buyOrder, Order sellOrder, BigDecimal amount, BigDecimal price) {
        Optional<User> buyer = userStore.findByID(buyOrder.getOrderID());
        Optional<User> seller = userStore.findByID(sellOrder.getUserID());

        if (buyer.isEmpty() || seller.isEmpty()) {
        System.out.println("Error: Usuario comprador o vendedor no encontrado.");
        return;
        }

        User buyerUser = buyer.get();
        User sellerUser = seller.get();

        BigDecimal totalCost = amount.multiply(price);

        if (buyerUser.getWallet().getBalance().compareTo(totalCost) < 0) {
            System.out.println("Error: Saldo insuficiente en la cuenta del comprador.");
            return;
        }

        if (!sellerUser.getWallet().hasEnoughCrypto(sellOrder.getCrypto(), amount)) {
            System.out.println("Error: Criptomoneda insuficiente en la cuenta del vendedor.");
            return;
        }

        buyerUser.getWallet().withdraw(totalCost);
        buyerUser.getWallet().addCrypto(buyOrder.getCrypto(), amount);

        sellerUser.getWallet().withdrawCrypto(sellOrder.getCrypto(), amount);
        sellerUser.getWallet().deposit(totalCost);

        userStore.storeUser(buyerUser);
        userStore.storeUser(sellerUser);

        Transaction transaction = new Transaction(buyerUser.getID(), buyOrder.getCrypto(), amount, price, true);
        transactionStore.storeTransaction(transaction);

        System.out.println("Orden ejecutada con éxito: " + transaction);
    }

    @Override
    public void setBuyOrder(String userID, Crypto crypto, BigDecimal amount, BigDecimal maxPrice) {
        Optional<User> userOption = userStore.findByID(userID);
        if (userOption.isPresent()) {
            User user = userOption.get();
            if (user.getWallet().getBalance().compareTo(amount.multiply(maxPrice)) >= 0) {
                user.getWallet().withdraw(amount.multiply(maxPrice));
                user.getWallet().addCrypto(crypto, amount);
                orderStore.storeOrder(new Order(userID, crypto, amount, maxPrice, OrderType.BUY));
                transactionStore.storeTransaction(new Transaction(userID, crypto, amount, maxPrice, true));
                userStore.storeUser(user);
                
                matchOrders();
            } else {
                 throw new IllegalArgumentException("Insufficient fiat balance to place the buy order.");
            }
        }
    }

    @Override
    public void setSellOrder(String userID, Crypto crypto, BigDecimal amount, BigDecimal minPrice) {
        Optional<User> userOption = userStore.findByID(userID);
        if (userOption.isPresent()) {
            User user = userOption.get();
            if (user.getWallet().hasEnoughCrypto(crypto, amount)) {
                user.getWallet().withdrawCrypto(crypto, amount);
                user.getWallet().deposit(amount.multiply(minPrice));
                orderStore.storeOrder(new Order(userID, crypto, amount, minPrice,OrderType.SELL));
                transactionStore.storeTransaction(new Transaction(userID, crypto, amount, minPrice, false));
                userStore.storeUser(user);
                
                matchOrders();
            } else {
                 throw new IllegalArgumentException("Insufficient cryptocurrency balance to place the sell order.");
            }
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return orderStore.findAllOrders();
    }
 
}

