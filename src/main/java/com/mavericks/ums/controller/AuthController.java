/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.controller;

import com.mavericks.ums.dao.UserDao;
import com.mavericks.ums.model.User;
import com.mavericks.ums.util.AuthValidator;
import com.mavericks.ums.util.Toast;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author nabin
 */
@WebServlet(name = "AuthController", urlPatterns = {"/login", "/register","/logout"})
public class AuthController extends HttpServlet {
    private final UserDao dao = new UserDao();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            switch (path) {
                case "/login":
                    showLoginPage(req, resp);
                    break;
                case "/register":
                    showRegisterPage(req, resp);
                    break;
                default:
                    super.doGet(req, resp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            switch (path) {
                case "/login":
                    login(req, resp);
                    break;
                case "/register":
                    register(req, resp);
                    break;
                case "/logout":
                    logout(req,resp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showRegisterPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/authForm.jsp");
        req.setAttribute("formType", "Sign up");
        req.setAttribute("pageTitle", "Sign Up");
        dispatcher.forward(req, resp);
    }

    private void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        User user = new User(
                req.getParameter("username"),
                req.getParameter("password"),
                req.getParameter("email"),
                req.getParameter("firstName"),
                req.getParameter("lastName"),
                Long.parseLong(req.getParameter("phoneNum"))
        );
        Map<String, String> errors = AuthValidator.validateForRegister(user, dao);
        if (errors.isEmpty()) {
            int id = dao.createUser(user);
            user.setId(id);
            HttpSession session = req.getSession();
            session.setAttribute("sessionUser", user);
            Toast toast = new Toast("Account created successfully", Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            resp.sendRedirect(req.getContextPath());
            return;
        }
        req.setAttribute("initialValues", user);
        req.setAttribute("errors", errors);
        showRegisterPage(req, resp);
    }

    private void showLoginPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/authForm.jsp");
        req.setAttribute("formType", "Login");
        req.setAttribute("pageTitle", "Login");
        dispatcher.forward(req, resp);
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        User user = new User(
                req.getParameter("username"),
                req.getParameter("password")
        );
        Map<String, String> errors = AuthValidator.validateForLogin(user, dao);
        if (errors.isEmpty()) {
            user = dao.getUserByUsermame(user.getUsername());
            HttpSession session = req.getSession();
            session.setAttribute("sessionUser", user);
            Toast toast = new Toast("You are now logged in as "+user.getUsername(), Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            if(user.isAdmin()){
                resp.sendRedirect(req.getContextPath()+"/admin");
            }
            else{
                resp.sendRedirect(req.getContextPath());
            }
            return;
        }
        req.setAttribute("initialValues", user);
        req.setAttribute("errors", errors);
        showLoginPage(req, resp);
    }
    private void logout(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
        HttpSession session = req.getSession(false);
        session.invalidate();
        Toast toast = new Toast("You are now logged out", Toast.MSG_TYPE_SUCCESS);
        toast.show(req);
        resp.sendRedirect(req.getContextPath());
    }
}
