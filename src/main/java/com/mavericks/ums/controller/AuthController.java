/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.controller;

import com.mavericks.ums.dao.UserDao;
import com.mavericks.ums.model.User;
import com.mavericks.ums.util.UserValidator;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nabin
 */
@WebServlet(name = "AuthController", urlPatterns = {"/login", "/register"})
public class AuthController extends HttpServlet {
    private UserDao dao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String path = req.getServletPath();
        try{
            switch(path){
                case "/login":
                    showLoginPage(req, resp);
                case "/register":
                    showRegisterPage(req, resp);
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       String path = req.getServletPath();
        try{
            switch(path){
                case "/login":
                    login(req, resp);
                case "/register":
                    register(req, resp);
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    

    private void showRegisterPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/authForm.jsp");
        req.setAttribute("formType","Sign up");
        req.setAttribute("pageTitle","Sign Up");
        dispatcher.forward(req,resp);  
    }
    
    private void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException,SQLException{
        User user = new User(
                req.getParameter("username"),
                req.getParameter("password"),
                req.getParameter("email"),
                req.getParameter("firstName"),
                req.getParameter("lastName"),
                Long.parseLong(req.getParameter("phoneNum"))
        );
        Map<String,String> errors =UserValidator.validateForRegister(user);
        if(errors.isEmpty()){
            User existingUser = dao.getUserByUsermame(user.getUsername());
            if(existingUser != null){
                errors.put("username","Username is already taken");
            }
            else{
                boolean wasUserCreated = dao.createUser(user);
                if(wasUserCreated){
                    resp.sendRedirect("/");
                }
                return;
            }
        }
        req.setAttribute("initialValues", user);
        req.setAttribute("errors", errors);
        showRegisterPage(req, resp);
    }
    private void showLoginPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException,SQLException{
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/authForm.jsp");
        req.setAttribute("formType", "Login");
        req.setAttribute("pageTitle","Login");
        dispatcher.forward(req, resp);
    }
    
    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException,SQLException{
        User user = new User(
            req.getParameter("username"),
            req.getParameter("password")
        );
        resp.getWriter().println(user.toString());
    }
}
