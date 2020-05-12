/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.model;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Acer
 */
public class UserHistory {
    private int id;
    private User user;
    private String action;
    private String detail;
    private Timestamp dateAndTime;

    public UserHistory(int id, User user, String action, String detail, Timestamp dateAndTime) {
        this.id = id;
        this.user = user;
        this.action = action;
        this.detail = detail;
        this.dateAndTime = dateAndTime;
    }
    
    public UserHistory(User user, String action, String detail) {
        this.user = user;
        this.action = action;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDateAndTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
        return formatter.format(dateAndTime);
    }

    public void setDateAndTime(Timestamp dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
    
    @Override
    public String toString() {
        return "User History(" + "id=" + id + ", user=" + user.getUsername() + ", action=" + action + ", detail=" + detail + ", date and time=" + dateAndTime + '}';
    }
}
