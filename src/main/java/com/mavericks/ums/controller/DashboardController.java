/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.controller;

import com.mavericks.ums.dao.UserDao;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Acer
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {
    private UserDao dao = new UserDao();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String path = req.getServletPath();
        try{
            switch(path) {
                case "/dashboard":
                    showDashboardPage(req, resp);
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    private void showDashboardPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/dashboard/dashboard.jsp");
        req.setAttribute("pageTitle", "Dashboard");
        req.setAttribute("totalUser", dao.getTotalUsers());
        dispatcher.forward(req, resp);
    }
}
