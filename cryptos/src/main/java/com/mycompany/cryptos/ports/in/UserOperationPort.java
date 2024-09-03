/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.ports.in;

import com.mycompany.cryptos.domain.model.User;
import java.math.BigDecimal;
import java.util.Optional;

/**
 *
 * @author User
 */
public interface UserOperationPort {
    User signUpUser(String name, String email, String password);
    Optional<User> signInUser(String email, String password);
    void depositFunds(String userID, BigDecimal amount);

}
