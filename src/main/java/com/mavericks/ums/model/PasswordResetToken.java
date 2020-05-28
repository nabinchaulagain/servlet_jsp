/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.model;

import java.sql.Timestamp;

/**
 *
 * @author nabin
 */
public class PasswordResetToken {
    private String token;
    private Timestamp requestedTime;
    private User user;

    public PasswordResetToken(String token, Timestamp requestedTime, User user) {
        this.token = token;
        this.requestedTime = requestedTime;
        this.user = user;
    }
    
    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(Timestamp requestedTime) {
        this.requestedTime = requestedTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
