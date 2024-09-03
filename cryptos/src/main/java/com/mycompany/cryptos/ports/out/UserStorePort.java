/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.ports.out;

import com.mycompany.cryptos.domain.model.User;
import java.util.Optional;

/**
 *
 * @author User
 */
public interface UserStorePort {
    void storeUser(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByID(String userID);
}
