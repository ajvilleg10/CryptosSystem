/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.adapters.persistence;

import com.mycompany.cryptos.domain.model.Transaction;
import com.mycompany.cryptos.ports.out.TransactionStorePort;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author User
 */
public class InMemoryTransactionStore implements TransactionStorePort {
     private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void storeTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> searchTransactionByUserID(String userID) {
        return transactions.stream()
                .filter(transaction -> transaction.getUserID().equals(userID))
                .collect(Collectors.toList());
    }
}
