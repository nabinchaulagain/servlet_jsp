/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author nabin
 */
public class DBSingleton {
    static Connection conn;
    //get database connection object
    public static Connection getConnection(){
        if(conn == null){
            conn = connect();
        }
        return conn;
    }
    
    // returns fresh new connection to database
    private static Connection connect(){
        String url = "jdbc:mysql://localhost:3306/ums"; 
        String user = "root"; 
        String pass = ""; 
        try { 
            Class.forName("com.mysql.jdbc.Driver"); 
            Connection connection = DriverManager.getConnection(url, user, pass); 
            return connection;
        } 
        catch (ClassNotFoundException | SQLException e) { 
        } 
        return null;
    }
}
