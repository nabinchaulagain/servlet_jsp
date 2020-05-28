/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.dao;

import com.mavericks.ums.model.User;
import com.mavericks.ums.util.DBSingleton;
import com.mavericks.ums.util.Hasher;
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
    
    // add user in database and return user's id
    public int createUser(User user) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO user(username,email,password,phone_num,role,first_name,last_name) VALUES (?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        );
        stmt.setString(1,user.getUsername());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, Hasher.hash(user.getPassword()));
        stmt.setLong(4, Long.parseLong(user.getPhoneNum()));
        stmt.setString(5,user.getRole());
        stmt.setString(6,user.getFirstName());
        stmt.setString(7, user.getLastName());
        stmt.executeUpdate();
        ResultSet resSet = stmt.getGeneratedKeys();
        resSet.next();
        return resSet.getInt(1);
    }
    
    // delete user by id in database
    public boolean deleteUser(int id) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM user WHERE id=?");
        stmt.setInt(1, id);
        boolean wasDeleted = stmt.executeUpdate() == 1;
        return wasDeleted;
    }
    
    // edit user's information in database
    public boolean editUser(User user) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE user SET username=?,email=?,password=?,phone_num=?,first_name=?,last_name=? WHERE id = ?"
        );
        stmt.setString(1,user.getUsername());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, Hasher.hash(user.getPassword()));
        stmt.setLong(4, Long.parseLong(user.getPhoneNum()));
        stmt.setString(5, user.getFirstName());
        stmt.setString(6, user.getLastName());
        stmt.setInt(7,user.getId());
        boolean wasEdited = stmt.executeUpdate() == 1;
        return wasEdited;
    }
    
    // get list of users in database
    public List<User> getUserList() throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT  user.*,!isnull(block_list.id) AS isBlocked FROM user LEFT JOIN block_list ON user.id = block_list.user_id ORDER BY joined_date DESC"
        );
        ResultSet resSet = stmt.executeQuery();
        List<User> users = new ArrayList<>();
        while(resSet.next()){
            User user = getUserFromResultSet(resSet);
            user.setIsBlocked(resSet.getBoolean("isBlocked"));
            users.add(user);
        }
        return users;
    }
    
    // get user by id in database
    public User getUserById(int id) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT user.*,!isnull(block_list.id) AS isBlocked FROM user LEFT JOIN block_list ON user.id = block_list.user_id WHERE user.id=?"
        );
        stmt.setInt(1, id);
        ResultSet resSet = stmt.executeQuery();
        if(resSet.next()){
            User user = getUserFromResultSet(resSet);
            user.setIsBlocked(resSet.getBoolean("isBlocked"));
            return user;
        }
        return null;
    }
    
    // get user by username in database
    public User getUserByUsermame(String username) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT user.*,!isnull(block_list.id) AS isBlocked FROM user LEFT JOIN block_list ON user.id = block_list.user_id WHERE user.username=?"
        );
        stmt.setString(1, username);
        ResultSet resSet = stmt.executeQuery();
        if(resSet.next()){
            User user = getUserFromResultSet(resSet);
            user.setIsBlocked(resSet.getBoolean("isBlocked"));
            return user;
        }
        return null;
    }
    
    // get user by email in database
    public User getUserByEmail(String email) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM user WHERE user.email=?"
        );
        stmt.setString(1, email);
        ResultSet resSet = stmt.executeQuery();
        if(resSet.next()){
            User user = getUserFromResultSet(resSet);
            return user;
        }
        return null;
    }
    
    // get total count of users in database
    public int getTotalUsers() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM user");
        ResultSet resSet = stmt.executeQuery();
        resSet.next();
        int result;
        result = resSet.getInt(1);
        return result;
    }
    
    //get total count of blocked users in database
    public int getBlockedUsers() throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM block_list");
        ResultSet resSet = stmt.executeQuery();
        resSet.next();
        int result;
        result = resSet.getInt(1);
        return result;
    }
    
    //change user's password in database
    public boolean changePassword(String password, int userId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE user SET password=? WHERE id = ?"
        );
        stmt.setString(1, Hasher.hash(password));
        stmt.setInt(2, userId);
        boolean wasEdited = stmt.executeUpdate() == 1;
        return wasEdited;
    }
    
    //insert record in block_list table in database
    public boolean blockUser(int userId,int blockerId,String reason) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO block_list(user_id,blocker_id,reason) VALUES (?,?,?)");
        stmt.setInt(1,userId);
        stmt.setInt(2,blockerId);
        stmt.setString(3, reason);
        boolean wasInserted = stmt.executeUpdate() == 1;
        return wasInserted;
    }
    
    //delete record from block_list in database
    public boolean unblockUser(int userId) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM block_list WHERE user_id=?");
        stmt.setInt(1,userId);
        boolean wasDeleted = stmt.executeUpdate() == 1;
        return wasDeleted;
    }
    
    //get reason for why user is blocked 
    public String getBlockedMsg(int userId) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT reason FROM block_list WHERE user_id = ?");
        stmt.setInt(1,userId);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getString(1);
    }
    
    // get list of users in a date interval
    public List<User> getUserListInInterval(String from,String to) throws SQLException{
        List<User> users = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM user WHERE joined_date BETWEEN ? AND ? ORDER BY joined_date DESC"
        );
        stmt.setString(1, from);
        stmt.setString(2, to);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            User user = getUserFromResultSet(rs);
            users.add(user);
        }
        return users;
    }
    // form User object from ResultSet
    private User getUserFromResultSet(ResultSet resSet) throws SQLException{
        User user = new User(
            resSet.getInt("id"),
            resSet.getString("username"),
            resSet.getString("password"),
            resSet.getString("email"),
            resSet.getString("first_name"),
            resSet.getString("last_name"),
            resSet.getString("role"),
            resSet.getTimestamp("joined_date"),
            resSet.getString("phone_num")
        );
        return user;
    }
    
}