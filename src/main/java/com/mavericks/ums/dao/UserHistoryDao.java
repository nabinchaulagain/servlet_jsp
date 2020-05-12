/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.dao;

import com.mavericks.ums.model.User;
import com.mavericks.ums.model.UserHistory;
import com.mavericks.ums.util.DBSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Acer
 */
public class UserHistoryDao {
    Connection conn = DBSingleton.getConnection();
    private final UserDao userDao = new UserDao();
    
    public int createUserHistory(UserHistory userHistory) throws SQLException {        
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO user_history(user_id, action, detail) VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        );
        stmt.setInt(1, userHistory.getUser().getId());
        stmt.setString(2, userHistory.getAction());
        stmt.setString(3, userHistory.getDetail());
        stmt.executeUpdate();
        ResultSet resSet = stmt.getGeneratedKeys();
        resSet.next();
        return resSet.getInt(1);
    }
    public boolean deleteUserHistory(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM user_history WHERE id=?");
        stmt.setInt(1, id);
        boolean wasDeleted = stmt.executeUpdate() == 1;
        return wasDeleted;
    }
    public boolean editUserHistory(UserHistory userHistory) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE user_history SET user_id=?,action=?,detail=? WHERE id = ?"
        );
        stmt.setInt(1, userHistory.getUser().getId());
        stmt.setString(2, userHistory.getAction());
        stmt.setString(3, userHistory.getDetail());
        stmt.setInt(4, userHistory.getId());
        boolean wasEdited = stmt.executeUpdate() == 1;
        return wasEdited;
    }
    public List<UserHistory> getUserHistoryListByUserId(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_history WHERE user_id = ? ORDER BY date_and_time DESC");
        stmt.setInt(1,id);
        ResultSet resSet = stmt.executeQuery();
        List<UserHistory> userhistories = new ArrayList<>();
        while (resSet.next()) {
            UserHistory userHistory = getUserHistoryFromResultSet(resSet);
            userhistories.add(userHistory);
        }
        return userhistories;
    }
    public boolean deleteUserHistoryByUserId(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM user_history WHERE user_id = ?");
        stmt.setInt(1, id);
        boolean wasDeleted = stmt.executeUpdate() == 1;
        return wasDeleted;
    }
    private UserHistory getUserHistoryFromResultSet(ResultSet resSet) throws SQLException {
        UserHistory userHistory = new UserHistory(
                resSet.getInt("id"),
                userDao.getUserById(resSet.getInt("user_id")),
                resSet.getString("action"),
                resSet.getString("detail"),
                resSet.getTimestamp("date_and_time")
        );
        return userHistory;
    }
}
