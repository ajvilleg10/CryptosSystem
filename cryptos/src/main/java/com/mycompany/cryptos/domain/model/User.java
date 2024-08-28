/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptos.domain.model;

import java.util.Objects;

/**
 *
 * @author User
 */
public class User {
    private final String ID;
    private final String name;
    private final String email;
    private final String password;
    private final Wallet wallet;

    public User(String ID, String name, String email, String password, Wallet wallet) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.wallet = wallet;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.ID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.ID, other.ID)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "ID=" + ID + ", name=" + name + ", email=" + email + ", password=" + password + '}';
    }
    
}
