/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.dao;

import com.mavericks.ums.model.User;
import com.mavericks.ums.util.DBSingleton;
import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nabin
 */
public class UserDao {
    Connection conn = DBSingleton.getConnection();
    
    // returns id of inserted user
    public int createUser(User user) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO user(username,email,password,phone_num,role,first_name,last_name) VALUES (?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        );
        stmt.setString(1,user.getUsername());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getPassword());
        stmt.setLong(4, user.getPhoneNum());
        stmt.setString(5,user.getRole());
        stmt.setString(6,user.getFirstName());
        stmt.setString(7, user.getLastName());
        stmt.executeUpdate();
        ResultSet resSet = stmt.getGeneratedKeys();
        resSet.next();
        return resSet.getInt(1);
    }
    public boolean deleteUser(int id) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM user WHERE id=?");
        stmt.setInt(1, id);
        boolean wasDeleted = stmt.executeUpdate() == 1;
        return wasDeleted;
    }
    public boolean editUser(User user) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE user SET username=?,email=?,password=?,phone_num=?,first_name=?,last_name=? WHERE id = ?"
        );
        stmt.setString(1,user.getUsername());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getPassword());
        stmt.setLong(4, user.getPhoneNum());
        stmt.setString(5, user.getFirstName());
        stmt.setString(6, user.getLastName());
        stmt.setInt(7,user.getId());
        boolean wasEdited = stmt.executeUpdate() == 1;
        return wasEdited;
    }
    public List<User> getUserList() throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user");
        ResultSet resSet = stmt.executeQuery();
        List<User> users = new ArrayList<>();
        while(resSet.next()){
            User user = getUserFromResultSet(resSet);
            users.add(user);
        }
        return users;
    }
    public User getUserById(int id) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE id=?");
        stmt.setInt(1, id);
        ResultSet resSet = stmt.executeQuery();
        if(resSet.next()){
            User user = getUserFromResultSet(resSet);
            return user;
        }
        return null;
    }
    public User getUserByUsermame(String username) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE username=?");
        stmt.setString(1, username);
        ResultSet resSet = stmt.executeQuery();
        if(resSet.next()){
            User user = getUserFromResultSet(resSet);
            return user;
        }
        return null;
    }
    public User getUserByEmail(String email) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE email=?");
        stmt.setString(1, email);
        ResultSet resSet = stmt.executeQuery();
        if(resSet.next()){
            User user = getUserFromResultSet(resSet);
            return user;
        }
        return null;
    }
    public int getTotalUsers() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM user");
        ResultSet resSet = stmt.executeQuery();
        resSet.next();
        int result;
        result = resSet.getInt(1);
        return result;
    }
     private User getUserFromResultSet(ResultSet resSet) throws SQLException{
         User user = new User(
                resSet.getInt("id"),
                resSet.getString("username"),
                resSet.getString("password"),
                resSet.getString("email"),
                resSet.getString("first_name"),
                resSet.getString("last_name"),
                resSet.getString("role"),
                resSet.getDate("joined_date"),
                resSet.getLong("phone_num")
         );
         return user;
    }
}