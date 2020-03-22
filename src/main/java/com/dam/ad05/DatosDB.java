/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dam.ad05;

import java.io.Serializable;

/**
 *
 * @author hak
 */
public class DatosDB implements Serializable{
    String address;
    String name;
    String user;
    String password;

    public DatosDB() {
    }

    public DatosDB(String address, String name, String user, String password) {
        this.address = address;
        this.name = name;
        this.user = user;
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DatosDB{" + "address=" + address + ", name=" + name + ", user=" + user + ", password=" + password + '}';
    }
    
    
}
