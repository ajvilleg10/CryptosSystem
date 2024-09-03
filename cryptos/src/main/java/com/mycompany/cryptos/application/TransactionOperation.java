/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.application;

import com.mycompany.cryptos.domain.model.Transaction;
import com.mycompany.cryptos.ports.out.TransactionStorePort;
import com.mycompany.cryptos.ports.out.UserStorePort;
import java.util.List;

/**
 *
 * @author User
 */
public class TransactionOperation {
    private final TransactionStorePort transactionStore;
    private final UserStorePort userStore;

    public TransactionOperation(TransactionStorePort transactionStore, UserStorePort userStore) {
        this.transactionStore = transactionStore;
        this.userStore = userStore;
    }
    
    public void recordTransaction(Transaction transaction) {
        transactionStore.storeTransaction(transaction);
    }

    public List<Transaction> getUserTransactionHistory(String userID) {
        if (userID == null || userID.isEmpty()) {
            throw new IllegalArgumentException("Empty or null IDs are not allowed.");
        }   
        return transactionStore.searchTransactionByUserID(userID);
    }

}
