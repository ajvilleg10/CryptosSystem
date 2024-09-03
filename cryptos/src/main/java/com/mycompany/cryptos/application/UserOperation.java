/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.application;

import com.mycompany.cryptos.domain.model.User;
import com.mycompany.cryptos.domain.model.Wallet;
import com.mycompany.cryptos.ports.in.UserOperationPort;
import com.mycompany.cryptos.ports.out.UserStorePort;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author User
 */
public class UserOperation implements UserOperationPort {
    private final UserStorePort userStore;

    public UserOperation(UserStorePort userStore) {
        this.userStore = userStore;
    }

    @Override
    public User signUpUser(String name, String email, String password) {
        User user = new User(UUID.randomUUID().toString(), name, email, password, new Wallet());
        userStore.storeUser(user);
        return user;
    }

    @Override
    public Optional<User> signInUser(String email, String password) {
        return userStore.findByEmail(email).filter(user -> user.getPassword().equals(password));
    }

    @Override
    public void depositFunds(String userID, BigDecimal amount) {
        Optional<User> userOption = userStore.findByID(userID);
        if (userOption.isPresent()) {
            User user = userOption.get();
            user.getWallet().deposit(amount);
            userStore.storeUser(user);
        }
    }
    
}
