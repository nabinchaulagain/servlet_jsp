/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.controller;

import com.mavericks.ums.dao.PasswordResetTokenDao;
import com.mavericks.ums.dao.UserDao;
import com.mavericks.ums.dao.UserHistoryDao;
import com.mavericks.ums.model.PasswordResetToken;
import com.mavericks.ums.model.User;
import com.mavericks.ums.model.UserHistory;
import com.mavericks.ums.util.AuthValidator;
import com.mavericks.ums.util.Hasher;
import com.mavericks.ums.util.Mailer;
import com.mavericks.ums.util.Toast;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
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
@WebServlet(name = "AuthController", urlPatterns = {"/login", "/register", "/logout", "/forgotPassword", "/resetPassword"})
public class AuthController extends HttpServlet {

    private final UserDao userDao = new UserDao();
    private final UserHistoryDao userHistoryDao = new UserHistoryDao();
    private final PasswordResetTokenDao passwordResetDao = new PasswordResetTokenDao();
    
    //called when GET request is  made 
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath(); // get path of request
        User sessionUser  =(User) req.getSession().getAttribute("sessionUser");
        if(sessionUser != null){
            resp.sendRedirect(req.getContextPath());
            return;
        }
        try {
            switch (path) {
                // match path to appropriate method
                case "/login":
                    showLoginPage(req, resp);
                    break;
                case "/register":
                    showRegisterPage(req, resp);
                    break;
                case "/forgotPassword":
                    showForgotPasswordPage(req, resp);
                    break;
                case "/resetPassword":
                    showResetPasswordPage(req, resp);
                    break;
                default:
                    super.doGet(req, resp);
            }
        } catch (Exception ex) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/500.html");
            dispatcher.forward(req, resp);
        }

    }

    //called when POST request is  made 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();// get path of request
        try {
            switch (path) {
                // match path to appropriate method
                case "/login":
                    login(req, resp);
                    break;
                case "/register":
                    register(req, resp);
                    break;
                case "/logout":
                    logout(req, resp);
                    break;
                case "/forgotPassword":
                    forgotPassword(req, resp);
                    break;
                case "/resetPassword":
                    resetPassword(req, resp);
                    break;
                default:
                    super.doPost(req, resp);
            }
        } catch (Exception ex) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/500.html");
            dispatcher.forward(req, resp);
        }
    }
    
    // route handler for (GET => /register) that shows signup form
    private void showRegisterPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/authForm.jsp");
        req.setAttribute("formType", "Sign up");
        req.setAttribute("pageTitle", "Sign Up");
        dispatcher.forward(req, resp);
    }

    // route handler for (POST => /register) that registers user
    private void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        User user = new User(
                req.getParameter("username"),
                req.getParameter("password"),
                req.getParameter("email"),
                req.getParameter("firstName"),
                req.getParameter("lastName"),
                req.getParameter("phoneNum")
        );
        Map<String, String> errors = AuthValidator.validateForRegister(user, userDao); // get all errors
        if (errors.isEmpty()) {
            //if no errors
            int id = userDao.createUser(user);
            user.setId(id);
            userHistoryDao.createUserHistory(new UserHistory(user, "Sign up", "User created his account."));
            HttpSession session = req.getSession();
            session.setAttribute("sessionUser", user);
            Toast toast = new Toast("Account created successfully", Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            resp.sendRedirect(req.getContextPath() + "/profile?id=" + user.getId()); // redirect user to his profile page
            return;
        }
        req.setAttribute("initialValues", user); // send previously entered values
        req.setAttribute("errors", errors); // send errors
        showRegisterPage(req, resp);
    }

    // route handler for (GET => /login) that shows login form
    private void showLoginPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/authForm.jsp");
        req.setAttribute("formType", "Login");
        req.setAttribute("pageTitle", "Login");
        dispatcher.forward(req, resp);
    }

    // route handler for (POST => /login) that logs user in
    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        User user = new User(
                req.getParameter("username"),
                req.getParameter("password")
        );
        Map<String, String> errors = AuthValidator.validateForLogin(user, userDao); // get errors
        if (errors.isEmpty()) {
            user = userDao.getUserByUsermame(user.getUsername());
            HttpSession session = req.getSession();
            session.setAttribute("sessionUser", user);
            userHistoryDao.createUserHistory(new UserHistory(user, "Login", "User logged into the application"));
            Toast toast = new Toast("You are now logged in as " + user.getUsername(), Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            if (user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin"); // redirect to dashboard if user is admin
            } else {
                resp.sendRedirect(req.getContextPath() + "/profile?id=" + user.getId());// redirect to dashboard if user is a regular user
            }
            return;
        }
        req.setAttribute("initialValues", user);
        req.setAttribute("errors", errors);
        showLoginPage(req, resp);
    }
    
    // route handler for (POST => /logout) that logs user out
    private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("sessionUser");
        userHistoryDao.createUserHistory(new UserHistory(user, "Logout", "User logged out of the application"));
        session.invalidate(); // destroy session
        Toast toast = new Toast("You are now logged out", Toast.MSG_TYPE_SUCCESS);
        toast.show(req);
        resp.sendRedirect(req.getContextPath());
    }

    // route handler for (GET => /forgotPassword) that shows forgot password form
    private void showForgotPasswordPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/forgotPassword.jsp");
        req.setAttribute("pageTitle", "Forgot Password");
        dispatcher.forward(req, resp);
    }
    
    // route handler for (POST => /forgotPassword) that rends recovery email
    private void forgotPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String email = req.getParameter("email");
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            req.setAttribute("error", "email not found");
            req.setAttribute("initialValue", email);
            showForgotPasswordPage(req, resp);
            return;
        }
        String token = UUID.randomUUID().toString(); // generate unique token for reset password
        userHistoryDao.createUserHistory(new UserHistory(user, "Password Change Request", "Request for password change was made by user"));
        Mailer.sendToken(user, token, req); // send email with reset link
        passwordResetDao.generateResetToken(new PasswordResetToken(token, user));
        Toast toast = new Toast("Please check your email. it might take a few seconds", Toast.MSG_TYPE_SUCCESS);
        toast.show(req);
        resp.sendRedirect(req.getContextPath());
    }
    
    // route handler for (POST => /editPassword) that shows reset password form
    private void showResetPasswordPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        try {
            String token = req.getParameter("token");
            int userId = Integer.parseInt(req.getParameter("user_id"));
            PasswordResetToken resetToken = passwordResetDao.getToken(userId); // get token in database
            if (!Hasher.check(resetToken.getToken(),token)) {
                resp.sendRedirect(req.getContextPath()); // redirect to homepage if token doesn't match token in database
                return;
            }
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/editPassword.jsp");
            req.setAttribute("pageTitle", "Edit password");
            dispatcher.forward(req, resp);
        } catch (NumberFormatException | NullPointerException ex) {
            resp.sendRedirect(req.getContextPath());
        }

    }

    // route handler for (POST => /admin/editPassword) that resets user's password
    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        try {
            String password = req.getParameter("password");
            String token = req.getParameter("token");
            int userId = Integer.parseInt(req.getParameter("user_id"));
            PasswordResetToken resetToken = passwordResetDao.getToken(userId);// get token in database
            if (!Hasher.check(resetToken.getToken(),token)) {
                resp.sendRedirect(req.getContextPath());// redirect to homepage if token doesn't match token in database
                return;
            }
            if(password.length() < 8){
                RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/editPassword.jsp");
                req.setAttribute("error", "Password should be at least 8 characters long");
                req.setAttribute("pageTitle", "Edit password");
                dispatcher.forward(req, resp);
                return;
            }
            userDao.changePassword(password, userId); // change user's password in database
            passwordResetDao.deleteResetToken(userId);// delete token in database
            userHistoryDao.createUserHistory(
                    new UserHistory(
                            new User(userId), 
                            "Password Reset", 
                            "Password was reset by user"
                    )
            );
            Toast toast = new Toast("Password was changed", Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            resp.sendRedirect(req.getContextPath()+"/login");
        }
        catch (NumberFormatException | NullPointerException ex) {
            // if invalid id or user doesn't exist in database
            ex.printStackTrace();
            resp.sendRedirect(req.getContextPath());
        }
    }
}
