/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.controller;

import com.mavericks.ums.dao.UserDao;
import com.mavericks.ums.model.User;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Acer
 */
@WebServlet(name = "AdminController", urlPatterns = {"/admin"})
public class AdminController extends HttpServlet {
    private UserDao dao = new UserDao();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User)session.getAttribute("user");
        if(user == null || !user.isAdmin()){
            resp.sendRedirect(req.getContextPath()+"/login");
            return;
        }
        String method = req.getMethod();
        if(method.equals("GET")){
            doGet(req,resp);
        }
        else if(method.equals("POST")){
            doPost(req,resp);
        }
    }
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        System.out.println("do Get still called");
        String path = req.getServletPath();
        try{
            switch(path) {
                case "/admin":
                    showDashboardPage(req, resp);
                    break;
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
