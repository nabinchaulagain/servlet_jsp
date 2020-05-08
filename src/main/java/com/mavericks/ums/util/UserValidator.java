/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.util;

import com.mavericks.ums.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nabin
 */
public class UserValidator {
    public static Map<String,String> validateForLogin(User user){
        Map<String,String> errors = new HashMap<>();
        if(user.getUsername().length() < 6){
            errors.put("username","Username should be at least 6 characters long");
        }
        if(user.getPassword().length() < 8){
            errors.put("password","Password should be at least 8 characters length");
        }
        return errors;
    }
    public static Map<String,String> validateForRegister(User user){
        Map<String,String> errors = validateForLogin(user);
        Pattern validEmailPattern =  Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Matcher emailMatcher = validEmailPattern.matcher(user.getEmail());
        if(!emailMatcher.matches()){
            errors.put("email","Email is in incorrect format");
        }
        if(user.getFirstName().length() == 0){
            errors.put("firstName","Your first name is required");
        }
        if(user.getLastName().length() == 0){
            errors.put("lastName","Your last name is required");
        }
        Pattern validPhonePattern =  Pattern.compile("^\\d{10}$");
        Matcher phoneNumMatcher = validPhonePattern.matcher(String.valueOf(user.getPhoneNum()));
        if(! phoneNumMatcher.matches()){
            errors.put("phoneNum","Phone number is incorrect");
        }
        return errors;
    }
    
}
