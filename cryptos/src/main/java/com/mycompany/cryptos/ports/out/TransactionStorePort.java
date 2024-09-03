/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.ports.out;

import com.mycompany.cryptos.domain.model.Transaction;
import java.util.List;

/**
 *
 * @author User
 */
public interface TransactionStorePort {
    void storeTransaction(Transaction transaction);
    List<Transaction> searchTransactionByUserID(String userID);
}
