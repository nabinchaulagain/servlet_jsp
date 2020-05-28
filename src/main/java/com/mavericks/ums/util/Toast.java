/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author nabin
 */
public class Toast {
    private String message;
    private String type;
    public static final String MSG_TYPE_SUCCESS = "toast-success";
    public static final String MSG_TYPE_ERROR = "toast-error";
    
    public Toast(String message,String type){
        this.message = message;
        this.type = type;
    }
    
    // set toast msg in token
    public void show(HttpServletRequest req){
        HttpSession session = req.getSession();
        session.setAttribute("toast", this);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getMessage() {
        return this.message;
    }

    public String getType() {
        return this.type;
    }
}
