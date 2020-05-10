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
@WebServlet(name = "AdminController", urlPatterns = {"/admin", "/admin/users", "/profile"})
public class AdminController extends HttpServlet {
    private UserDao dao = new UserDao();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User sessionUser = (User)session.getAttribute("sessionUser");
        if(sessionUser == null || !sessionUser.isAdmin()){
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
        String path = req.getServletPath();
        try{
            switch(path) {
                case "/admin":
                    showDashboardPage(req, resp);
                    break;
                case "/admin/users":
                    showViewUserPage(req, resp);
                    break;
                case "/profile":
                    showProfilePage(req, resp);
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

    private void showViewUserPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/dashboard/viewUsers.jsp");
        req.setAttribute("pageTitle", "View Users");
        req.setAttribute("allusers", dao.getUserList());
        dispatcher.forward(req, resp);
    }

    private void showProfilePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(req.getParameter("id"));
        User u = dao.getUserById(id);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/dashboard/profile.jsp");
        req.setAttribute("pageTitle", u.getUsername());
        req.setAttribute("user", u);
        dispatcher.forward(req, resp);
    }
}
