/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.adapters.persistence;

import com.mycompany.cryptos.domain.model.User;
import com.mycompany.cryptos.ports.out.UserStorePort;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author User
 */
public class InMemoryUserStore implements UserStorePort{
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void storeUser(User user) {
        users.put(user.getID(),user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream().filter((User user) -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public Optional<User> findByID(String userID) {
        return Optional.ofNullable(users.get(userID));
    }
    
    
}
