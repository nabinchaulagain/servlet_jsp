/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author nabin
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String role = "user";
    private Date joinedDate;
    private long phoneNum;

    public User(int id, String username, String password, String email,String firstName,String lastName,String role, Date joinedDate, long phoneNum) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.joinedDate = joinedDate;
        this.phoneNum = phoneNum;
    }

    public User(String username, String password, String email, String firstName, String lastName, long phoneNum) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    public long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(long phoneNum) {
        this.phoneNum = phoneNum;
    }
    
    public boolean isAdmin(){
        return this.role.equals("admin");
    }
    public String getFullName(){
        return this.getFirstName()+ " "+ this.getLastName();
    }
    
    public String getChangedFields(User prevState){
       List<String> changedMessages = new ArrayList<>();
       if(!this.getEmail().equals(prevState.getEmail())){
           changedMessages.add("email changed from "+ prevState.getEmail() + " to "+ this.getEmail());
       }
       if(!this.getUsername().equals(prevState.getUsername())){
           changedMessages.add("username changed from "+ prevState.getUsername()+ " to "+ this.getUsername());
       }
       if(!this.getFullName().equals(prevState.getFullName())){
          changedMessages.add("name changed from "+ prevState.getFullName()+ " to "+ this.getFullName());
       }
       if(!this.getPassword().equals(prevState.getPassword())){
           changedMessages.add("password changed");
       }
       if(this.getPhoneNum() != prevState.getPhoneNum()){
           changedMessages.add("email changed from "+ prevState.getPhoneNum() + " to "+ this.getPhoneNum());
       }
       StringBuilder buff = new StringBuilder();
       if(changedMessages.isEmpty()){
           return "";
       }
       buff.append(changedMessages.get(0));
       for(int i=1; i<= changedMessages.size() -2 ; i++){
           buff.append(", ").append(changedMessages.get(i));
       }
       if(changedMessages.size() >= 2){
           buff.append(" and ").append(changedMessages.get(changedMessages.size()-1));
       }
       return buff.toString();
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", role=" + role + ", joinedDate=" + joinedDate + ", phoneNum=" + phoneNum + '}';
    }
}