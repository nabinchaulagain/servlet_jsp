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
import com.mavericks.ums.util.AuthValidator;
import com.mavericks.ums.util.Mailer;
import com.mavericks.ums.util.Toast;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.mail.Session;
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
@WebServlet(name = "UserController", urlPatterns = {"/profile","/profile/edit"})
public class UserController extends HttpServlet {
    private final UserDao userDao = new UserDao();
    private final UserHistoryDao userHistoryDao = new UserHistoryDao();
    
    //called when request is  made 
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        User user = (User) req.getSession().getAttribute("sessionUser");
        if(user == null){
            resp.sendRedirect(req.getContextPath()+"/login");// redirect to login page if not logged in
            return;
        }
        if(user.isBlocked()){
            // if user is blocked
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/blocked.jsp");
            try{
                String blockedMsg = userDao.getBlockedMsg(user.getId());
                req.setAttribute("blockedMsg", blockedMsg);
            }
            catch(Exception ex){
                RequestDispatcher dispatcherForErrorPage = req.getRequestDispatcher("/WEB-INF/500.html");
                dispatcherForErrorPage.forward(req, resp);
                return;
            }
            req.setAttribute("pageTitle", "You are blocked");
            dispatcher.forward(req, resp); // show blocked page with blocked msg
            return;
        }
        if (method.equals("GET")) {
            doGet(req, resp);
        } else if (method.equals("POST")) {
            doPost(req, resp);
        } else {
            super.service(req, resp); // show 405(METHOD NOT SUPPORTED) error
        }
    }
    
    //called when GET request is  made 
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        try{
            switch(path){
                case "/profile":
                    showProfilePage(req, resp);
                    break;
                case "/profile/edit":
                    showEditProfilePage(req,resp);
                    break;
                default:
                    super.doGet(req, resp);
            }
        }
        catch(Exception ex){
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/500.html");
            dispatcher.forward(req, resp);
        }
    }
    
    //called when POST request is  made 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        try{
            switch (path) {
                case "/profile/edit":
                    editProfile(req, resp);
                    break;
                default:
                    super.doPost(req, resp);
            }
        }
        catch(Exception ex){
           RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/500.html");
           dispatcher.forward(req, resp);
        }
    }
    
    // route handler for (GET => /profile) that shows user's profile page
    private void showProfilePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException{
        try{
            int userId = Integer.parseInt(req.getParameter("id"));
            User sessionUser = (User) req.getSession().getAttribute("sessionUser");
            if(!sessionUser.isAdmin() && sessionUser.getId() != userId){
                // if user is not an admin or if a normal user is trying to access someone else's profile
                resp.sendRedirect(req.getContextPath());
                return;
            }
            User user = userDao.getUserById(userId);
            if(user == null){
                resp.sendRedirect(req.getContextPath());// if userId is not found in database redirect to homepage
                return;
            }
            List<UserHistory> historyList = userHistoryDao.getUserHistoryListByUserId(userId);
            if(sessionUser.getId() == userId){
                req.setAttribute("pageTitle","Your profile"); // set webpage title to 'Your Profile' is user is trying to access his profile
            }
            else{
                req.setAttribute("pageTitle", user.getUsername()+"'s profile"); // set webpage title to 'user's Profile' is admin is trying to access a user's profile
            }
            req.setAttribute("historyList", historyList);
            req.setAttribute("user", user);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/profile.jsp");
            dispatcher.forward(req, resp);
        }
        catch(NumberFormatException ex){
            resp.sendRedirect(req.getContextPath());
        }
    }
    
    // route handler for (GET => /profile/edit) that shows edit profile page
    private void showEditProfilePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/editProfile.jsp");
        req.setAttribute("initialValues", req.getSession().getAttribute("sessionUser"));
        req.setAttribute("pageTitle","Edit Profile");
        dispatcher.forward(req, resp);
    }
    
    // route handler for (GET => /profile/edit) that shows edit profile form
    private void editProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException,SQLException{
         User prevUser = (User) req.getSession().getAttribute("sessionUser");
         User user = new User(
                req.getParameter("username"),
                prevUser.getPassword(),
                req.getParameter("email"),
                req.getParameter("firstName"),
                req.getParameter("lastName"),
                req.getParameter("phoneNum")
        );
        user.setId(prevUser.getId());
        user.setRole(prevUser.getRole());
        Map<String, String> errors = AuthValidator.validateForEditUser(prevUser,user, userDao); //get errors
        if (errors.isEmpty()) {
            userDao.editUser(user);
            String changedMsg = user.getChangedFields(prevUser); // get changed fields of user
            if(!changedMsg.equals("")){
                userHistoryDao.createUserHistory(new UserHistory(
                        user,
                        "Profile Update",
                        changedMsg + " by user"
                ));
            }
            Toast toast = new Toast("Profile updated successfully", Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            req.getSession().setAttribute("sessionUser", user);
            resp.sendRedirect(req.getContextPath() + "/profile?id="+user.getId());
            return;
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/editProfile.jsp");
        req.setAttribute("initialValues", user);
        req.setAttribute("pageTitle","Edit Profile");
        req.setAttribute("errors", errors);
        dispatcher.forward(req,resp);
    }
}
