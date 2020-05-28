/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.dao;

import com.mavericks.ums.model.PasswordResetToken;
import com.mavericks.ums.model.User;
import com.mavericks.ums.util.DBSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author nabin
 */
public class PasswordResetTokenDao {
    Connection conn = DBSingleton.getConnection();
    
    // create reset token for user in database
    public boolean generateResetToken(PasswordResetToken token) throws SQLException{
        deleteResetToken(token.getUser().getId());
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO password_reset(token,user_id) VALUES (?,?)");
        stmt.setString(1, token.getToken());
        stmt.setInt(2, token.getUser().getId());
        boolean wasInserted = (stmt.executeUpdate() == 1);
        return wasInserted;
    }
    
    // delete token by user_id in database
    public boolean deleteResetToken(int userId) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM password_reset WHERE user_id=?");
        stmt.setInt(1, userId);
        boolean wasDeleted = (stmt.executeUpdate() == 1);
        return wasDeleted;
    }
    
    // get token by user_id in database
    public PasswordResetToken getToken(int userId) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM password_reset WHERE user_id=?");
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            PasswordResetToken resetToken = new PasswordResetToken(
                rs.getString("token"),
                rs.getTimestamp("requested_time"),
                null
            );
            return resetToken;
        }
        return null;
    }
    
}
