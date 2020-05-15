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
import com.mavericks.ums.util.ReportValidator;
import com.mavericks.ums.util.Toast;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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
 * @author Acer
 */
@WebServlet(name = "AdminController", urlPatterns = {
    "/admin", 
    "/admin/users",
    "/admin/deleteUser",
    "/admin/addUser","/admin/editUser",
    "/admin/blockUser",
    "/admin/unblockUser",
    "/admin/reports"
})
public class AdminController extends HttpServlet {
    private final UserDao userDao = new UserDao();
    private final UserHistoryDao userHistoryDao = new UserHistoryDao();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User sessionUser = (User)session.getAttribute("sessionUser");
        if(sessionUser == null || !sessionUser.isAdmin()){
            resp.sendRedirect(req.getContextPath()+"/login");
            return;
        }
        String method = req.getMethod();
        req.setAttribute("path", req.getServletPath());
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
                case "/admin/addUser":
                    showAddUserPage(req, resp);
                    break;
                case "/admin/editUser":
                    showEditUserPage(req,resp);
                    break;
                case "/admin/deleteUser":
                    showDeleteUserPage(req, resp);
                    break;
                case "/admin/blockUser":
                    showBlockUserPage(req, resp);
                    break;
                case "/admin/reports":
                    showReportsPage(req,resp);
                default:
                    super.doGet(req, resp);
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
            switch(path) {
                case "/admin/addUser":
                    addUser(req, resp);
                    break;
                case "/admin/editUser":
                    editUser(req,resp);
                    break;
                case "/admin/deleteUser":
                    deleteUser(req, resp);
                    break;
                case "/admin/blockUser":
                    blockUser(req,resp);
                    break;
                case "/admin/unblockUser":
                    unblockUser(req, resp);
                    break;
                default:
                    super.doPost(req, resp);
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    private void showDashboardPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp");
        req.setAttribute("pageTitle", "Dashboard");
        req.setAttribute("totalUser", userDao.getTotalUsers());
        dispatcher.forward(req, resp);
    }

    private void showViewUserPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/admin/viewUsers.jsp");
        req.setAttribute("pageTitle", "View Users");
        req.setAttribute("allusers", userDao.getUserList());
        dispatcher.forward(req, resp);
    }
    
    private void showDeleteUserPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException{
        try{
            int id = Integer.parseInt(req.getParameter("id"));
            User user = userDao.getUserById(id);
            if(user == null || user.isAdmin()){
                resp.sendRedirect(req.getContextPath()+"/admin/users");
                return;
            }
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/admin/deleteUser.jsp");
            req.setAttribute("user", user);
            dispatcher.forward(req,resp);
        }
        catch(NumberFormatException ex){
            resp.sendRedirect(req.getContextPath()+"/admin");
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        try{
            int id = Integer.parseInt(req.getParameter("id"));
            User user = userDao.getUserById(id);
            if(user == null || user.isAdmin()){
                resp.sendRedirect(req.getContextPath()+"/admin/users");
                return;
            }
            boolean isDeleted = userDao.deleteUser(id);
            if(isDeleted){
                HttpSession session = req.getSession();
                User sessionUser = (User) session.getAttribute("sessionUser");
                userHistoryDao.createUserHistory(new UserHistory(sessionUser, "User Deletion", "Deleted user with username " + user.getUsername()));
            }
            Toast toast = new Toast("User deleted successfully", Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            resp.sendRedirect(req.getContextPath()+"/admin/users");
        }
        catch(NumberFormatException ex){
            resp.sendRedirect(req.getContextPath()+"/admin");
        }
    }
    private void showAddUserPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/admin/userForm.jsp");
        req.setAttribute("pageTitle", "Add User");
        req.setAttribute("formType","Add");
        dispatcher.forward(req,resp);          
    }
    
    private void addUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException{
        User user = new User(
                req.getParameter("username"),
                req.getParameter("password"),
                req.getParameter("email"),
                req.getParameter("firstName"),
                req.getParameter("lastName"),
                req.getParameter("phoneNum")
        );
        user.setRole(req.getParameter("role"));
        Map<String, String> errors = AuthValidator.validateForRegister(user, userDao);
        if (errors.isEmpty()) {
            int id = userDao.createUser(user);
            user.setId(id);
            User admin = (User) req.getSession().getAttribute("sessionUser");
            userHistoryDao.createUserHistory(new UserHistory(user, "User Creation", "Account created by admin ("+ admin.getUsername()+")"));
            userHistoryDao.createUserHistory(new UserHistory(
                    admin, 
                    "Account creation",
                    user.getRole()+" created with username "+user.getUsername()+" and email "+user.getEmail()
            ));
            Toast toast = new Toast("Account created successfully", Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            resp.sendRedirect(req.getContextPath()+"/admin/users");
            if(req.getParameter("sendMail") !=  null){
                Mailer.sendCredentialsAfterUserAdd(user, req);
            }
            return;
        }
        req.setAttribute("initialValues", user);
        req.setAttribute("errors", errors);
        showAddUserPage(req, resp);
    }
    private void showEditUserPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException{
        try{    
            int id = Integer.parseInt(req.getParameter("id"));
            User user = userDao.getUserById(id);
            if(user == null || user.isAdmin()){
                resp.sendRedirect(req.getContextPath()+"/admin/users");
                return;
            }
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/admin/userForm.jsp");
            req.setAttribute("pageTitle", "Edit User");
            req.setAttribute("formType","Edit");
            req.setAttribute("initialValues", user);
            dispatcher.forward(req,resp);
        }
        catch(NumberFormatException ex){
            resp.sendRedirect(req.getContextPath()+"/admin");
        }
    }
    
    private void editUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException{
        int id = -1;
        User prevUser = null;
        try {    
            id = Integer.parseInt(req.getParameter("id"));
            prevUser = userDao.getUserById(id);
            if(prevUser == null || prevUser.isAdmin()){
                resp.sendRedirect(req.getContextPath()+"/admin/users");
                return;
            }
        }
        catch (NumberFormatException ex){
            resp.sendRedirect(req.getContextPath()+"/admin");
        }
        User user = new User(
                req.getParameter("username"),
                req.getParameter("password"),
                req.getParameter("email"),
                req.getParameter("firstName"),
                req.getParameter("lastName"),
                req.getParameter("phoneNum")
        );
        user.setRole(req.getParameter("role"));
        user.setId(id);
        Map<String, String> errors = AuthValidator.validateForEditUser(prevUser,user, userDao);
        if (errors.isEmpty()) {
            userDao.editUser(user);
            User admin = (User) req.getSession().getAttribute("sessionUser");
            String changedMsg = user.getChangedFields(prevUser);
            if(!changedMsg.equals("")){
                userHistoryDao.createUserHistory(new UserHistory(user, "User Edit",changedMsg + " by admin("+admin.getUsername()+")"));
                userHistoryDao.createUserHistory(new UserHistory(
                        admin,
                        "User Edit",
                        changedMsg + " for user with username "+ prevUser.getUsername()
                ));
            }
            Toast toast = new Toast("User Edited successfully", Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            resp.sendRedirect(req.getContextPath() + "/admin/users");
            if(req.getParameter("sendMail") !=  null){
                Mailer.sendCredentialsAfterUserEdit(user, req);
            }
            return;
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/admin/userForm.jsp");
        req.setAttribute("pageTitle", "Edit User");
        req.setAttribute("formType","Edit");
        req.setAttribute("initialValues", user);
        req.setAttribute("errors", errors);
        dispatcher.forward(req,resp);
    }
    
    private void showBlockUserPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException{
        try{
            int id = Integer.parseInt(req.getParameter("id"));
            User user = userDao.getUserById(id);
            if(user == null || user.isBlocked() || user.isAdmin()){
                resp.sendRedirect(req.getContextPath()+"/admin/users");
                return;
            }
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/admin/blockUser.jsp");
            req.setAttribute("pageTitle", "Block "+user.getUsername());
            dispatcher.forward(req, resp);
        }
        catch(NumberFormatException ex ){
            resp.sendRedirect(req.getContextPath()+"/admin/users");
        }
    }
    private void blockUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException{
         try{
            int id = Integer.parseInt(req.getParameter("id"));
            User user = userDao.getUserById(id);
            if(user == null || user.isBlocked() || user.isAdmin()){
                resp.sendRedirect(req.getContextPath()+"/admin");
                return;
            }
            String reason = req.getParameter("reason");
            if(reason.equals("")){
                RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/admin/blockUser.jsp");
                req.setAttribute("pageTitle", "Block " + user.getUsername());
                req.setAttribute("error", "Reason is required");
                req.setAttribute("initialValue", reason);
                dispatcher.forward(req, resp);
                return;
            }
            User admin = (User) req.getSession().getAttribute("sessionUser");
            userDao.blockUser(id, admin.getId(), reason);
            userHistoryDao.createUserHistory(new UserHistory(admin, "User block", "Admin blocked user("+user.getUsername()+") for "+reason));
            userHistoryDao.createUserHistory(new UserHistory(user, "Block","User was blocked by an admin for "+reason));
            Toast toast = new Toast("User was blocked", Toast.MSG_TYPE_SUCCESS);
            toast.show(req);
            resp.sendRedirect(req.getContextPath()+"/admin/users");
        }
        catch(NumberFormatException ex ){
            resp.sendRedirect(req.getContextPath()+"/admin/users");
        }
    }
    
    private void unblockUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException{
         try{
            int id = Integer.parseInt(req.getParameter("id"));
            User user = userDao.getUserById(id);
            if(user == null || !user.isBlocked() || user.isAdmin()){
                resp.sendRedirect(req.getContextPath()+"/admin");
                return;
            }
            User admin = (User) req.getSession().getAttribute("sessionUser");
            userDao.unblockUser(id);
            userHistoryDao.createUserHistory(new UserHistory(admin, "User Unblock", "Admin unblocked user("+user.getUsername()+")"));
            userHistoryDao.createUserHistory(new UserHistory(user, "Unblock","User was unblocked by an admin"));
            resp.sendRedirect(req.getContextPath()+"/admin/users");
        }
        catch(NumberFormatException ex ){
            resp.sendRedirect(req.getContextPath()+"/admin/users");
        }
    }
    
    private void showReportsPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException{
        String startDate = req.getParameter("from");
        String endDate  = req.getParameter("to");
        List<User> users = null;
        if((startDate == null || startDate.equals("")) && (endDate == null || endDate.equals(""))){
            users = userDao.getUserList();
        }
        else{
            Map<String,String> errors = ReportValidator.validate(startDate, endDate);
            if(!errors.isEmpty()){
                req.setAttribute("errors", errors);
            }
            else{
                users = userDao.getUserListInInterval(startDate,endDate);
            }
        }
        req.setAttribute("fromDate", startDate);
        req.setAttribute("toDate", endDate);
        req.setAttribute("users", users);
        req.setAttribute("pageTitle", "Reports");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/admin/reports.jsp");
        dispatcher.forward(req,resp);
    }
}
