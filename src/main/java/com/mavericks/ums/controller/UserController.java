/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.controller;

import com.mavericks.ums.dao.UserDao;
import com.mavericks.ums.dao.UserHistoryDao;
import com.mavericks.ums.model.User;
import com.mavericks.ums.model.UserHistory;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
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
@WebServlet(name = "UserController", urlPatterns = {"/profile"})
public class UserController extends HttpServlet {
    private final UserDao userDao = new UserDao();
    private final UserHistoryDao userHistoryDao = new UserHistoryDao();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("sessionUser");
        if(user == null){
            resp.sendRedirect(req.getContextPath()+"/login");
            return;
        }
        showProfile(req,resp);
    }
    
    private void showProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        try{
            int userId = Integer.parseInt(req.getParameter("id"));
            User sessionUser = (User) req.getSession().getAttribute("sessionUser");
            if(!sessionUser.isAdmin() && sessionUser.getId() != userId){
                resp.sendRedirect(req.getContextPath());
                return;
            }
            User user = userDao.getUserById(userId);
            if(user == null){
                resp.sendRedirect(req.getContextPath());
                return;
            }
            List<UserHistory> historList = userHistoryDao.getUserHistoryListByUserId(userId);
            if(sessionUser.getId() == userId){
                req.setAttribute("pageTitle","Your profile");
            }
            else{
                req.setAttribute("pageTitle", user.getUsername()+"'s profile");
            }
            req.setAttribute("historyList", historList);
            req.setAttribute("user", user);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/profile.jsp");
            dispatcher.forward(req, resp);
        }
        catch(NumberFormatException ex){
            ex.printStackTrace();
            resp.sendRedirect(req.getContextPath());
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
}
