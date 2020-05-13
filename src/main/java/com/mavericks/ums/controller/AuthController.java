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
                case "/forgotPassword":
                    showForgotPasswordPage(req, resp);
                    break;
                case "/resetPassword":
                    showResetPasswordPage(req, resp);
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
                req.getParameter("phoneNum")
        );
        Map<String, String> errors = AuthValidator.validateForRegister(user, userDao);
        if (errors.isEmpty()) {
            int id = userDao.createUser(user);
            user.setId(id);
            userHistoryDao.createUserHistory(new UserHistory(user, "Sign up", "User created his account."));
            HttpSession session = req.getSession();
            session.setAttribute("sessionUser", user);
            Toast toast = new Toast("Account created successfully", Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            resp.sendRedirect(req.getContextPath() + "/profile?id=" + user.getId());
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
        Map<String, String> errors = AuthValidator.validateForLogin(user, userDao);
        if (errors.isEmpty()) {
            user = userDao.getUserByUsermame(user.getUsername());
            HttpSession session = req.getSession();
            session.setAttribute("sessionUser", user);
            userHistoryDao.createUserHistory(new UserHistory(user, "Login", "User logged into the application"));
            Toast toast = new Toast("You are now logged in as " + user.getUsername(), Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            if (user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin");
            } else {
                resp.sendRedirect(req.getContextPath() + "/profile?id=" + user.getId());
            }
            return;
        }
        req.setAttribute("initialValues", user);
        req.setAttribute("errors", errors);
        showLoginPage(req, resp);
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("sessionUser");
        userHistoryDao.createUserHistory(new UserHistory(user, "Logout", "User logged out of the application"));
        session.invalidate();
        Toast toast = new Toast("You are now logged out", Toast.MSG_TYPE_SUCCESS);
        toast.show(req);
        resp.sendRedirect(req.getContextPath());
    }

    private void showForgotPasswordPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/forgotPassword.jsp");
        req.setAttribute("pageTitle", "Forgot Password");
        dispatcher.forward(req, resp);
    }

    private void forgotPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String email = req.getParameter("email");
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            req.setAttribute("error", "email not found");
            req.setAttribute("initialValue", email);
            showForgotPasswordPage(req, resp);
            return;
        }
        String token = UUID.randomUUID().toString();
        userHistoryDao.createUserHistory(new UserHistory(user, "Password Change Request", "Request for password change was made by user"));
        Mailer.sendToken(user, token, req);
        passwordResetDao.generateResetToken(new PasswordResetToken(token, user));
        Toast toast = new Toast("Please check your email. it might take a few seconds", Toast.MSG_TYPE_SUCCESS);
        toast.show(req);
        resp.sendRedirect(req.getContextPath());
    }

    private void showResetPasswordPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        try {
            String token = req.getParameter("token");
            int userId = Integer.parseInt(req.getParameter("user_id"));
            PasswordResetToken resetToken = passwordResetDao.getToken(userId);
            if (!resetToken.getToken().equals(token)) {
                resp.sendRedirect(req.getContextPath());
                return;
            }
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/editPassword.jsp");
            req.setAttribute("pageTitle", "Edit password");
            dispatcher.forward(req, resp);
        } catch (NumberFormatException | NullPointerException ex) {
            resp.sendRedirect(req.getContextPath());
        }

    }

    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        try {
            String password = req.getParameter("password");
            String token = req.getParameter("token");
            int userId = Integer.parseInt(req.getParameter("user_id"));
            PasswordResetToken resetToken = passwordResetDao.getToken(userId);
            if (!resetToken.getToken().equals(token)) {
                resp.sendRedirect(req.getContextPath());
                return;
            }
            if(password.length() < 8){
                RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/auth/editPassword.jsp");
                req.setAttribute("error", "Password should be at least 8 characters long");
                req.setAttribute("pageTitle", "Edit password");
                dispatcher.forward(req, resp);
                return;
            }
            userDao.changePassword(password, userId);
            passwordResetDao.deleteResetToken(userId);
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
            ex.printStackTrace();
            resp.sendRedirect(req.getContextPath());
        }
    }
}
