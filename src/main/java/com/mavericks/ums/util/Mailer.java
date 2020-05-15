/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.util;
import com.mavericks.ums.model.User;
import com.sun.mail.util.MailSSLSocketFactory;
import java.security.GeneralSecurityException;
import javax.mail.PasswordAuthentication;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nabin
 */
public class Mailer{
    private static final String USERNAME = Secrets.USERNAME;
    private static final String PASSWORD = Secrets.PASSWORD;

    private static void sendMail(final String toAddress,final String subject, final String message) throws AddressException, GeneralSecurityException {
        Runnable emailTask = new Runnable() {
            @Override
            public void run() {
                try{
                    Properties properties = new Properties();
                    MailSSLSocketFactory sf = new MailSSLSocketFactory();
                    sf.setTrustAllHosts(true);
                    properties.put("mail.smtp.ssl.socketFactory", sf);
                    properties.put("mail.smtp.host", "smtp.gmail.com");
                    properties.put("mail.smtp.port", 587);
                    properties.put("mail.smtp.auth", true);
                    properties.put("mail.smtp.starttls.enable", true);
                    Authenticator auth = new Authenticator() {
                        @Override
                        public PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(USERNAME, PASSWORD);
                        }
                    };
                    Session session = Session.getInstance(properties, auth);
                    final Message msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(USERNAME));
                    InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
                    msg.setRecipients(Message.RecipientType.TO, toAddresses);
                    msg.setSubject(subject);
                    msg.setContent(message, "text/html");
                    Transport.send(msg);
                }
                catch(AddressException ex  ){
                    ex.printStackTrace();
                }
                catch(GeneralSecurityException | MessagingException ex){
                    ex.printStackTrace();
                }
            }
        };
        Thread emailThread = new Thread(emailTask);
        emailThread.start();
    }
    
    public static void sendCredentialsAfterUserAdd(User user,HttpServletRequest req){
        String mailContent = 
                "<html>"+
                    "<body>"+
                           "<h2>Hello, "+user.getFullName()+"</h2>"+
                           "<p>A new account has been created by admin"+
                           "<br>Your credentials are:</p>"+
                           "<ul>"+
                                "<li>Username: "+user.getUsername()+"</li>"+
                                "<li>Password: "+user.getPassword()+"</li>"+
                           "</ul>"+
                            "<button style='background:#7bb8c9;padding:8px;border:1px solid #7bb8c9;border-radius:3px;font-size:120%;'>"+
                                    "<a href='"+ getBaseUrl(req) +"/login' style='text-decoration:none;color:black'>Login Here</a>"+
                            "</button>"+
                    "</body>"+
                 "</html>"; 
        try {
            sendMail(user.getEmail(),"Account created",mailContent);
        } catch (MessagingException | GeneralSecurityException ex) {
            ex.printStackTrace();
        }
    }
    private static String getBaseUrl(HttpServletRequest req){
        String scheme = req.getScheme() + "://";
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        return scheme + serverName + ":" + serverPort + req.getContextPath();
    }
    public static void sendCredentialsAfterUserEdit(User user,HttpServletRequest req){
        String mailContent = 
                "<html>"+
                "<body>"+
                           "<h2>Hello, "+user.getFullName()+"</h2>"+
                           "<p>Your account has been updated by an admin"+
                           "<br>Your new credentials are:</p>"+
                           "<ul>"+
                                "<li>Username: "+user.getUsername()+"</li>"+
                                "<li>Password: "+user.getPassword()+"</li>"+
                           "</ul>"+
                           "<button style='background:#7bb8c9;padding:8px;border:1px solid #7bb8c9;border-radius:3px;font-size:120%;'>"+
                                "<a href='"+ getBaseUrl(req) +"/login' style='text-decoration:none;color:black'>Login Here</a>"+
                           "</button>"+
                    "</body>"+
                 "</html>";
        try {
            sendMail(user.getEmail(),"Account created",mailContent);
        } catch (MessagingException | GeneralSecurityException ex) {
           ex.printStackTrace();       
        }
    }
    public static void sendToken(User user,String token,HttpServletRequest req){
        String mailContent = 
                "<html>"+
                    "<body>"+
                           "<h2>Hello, "+user.getFullName()+"</h2>"+
                           "<p>You requested a password reset for your UMS account.Click on the link below to reset your password.</p>"+
                           "<a href='"+ getBaseUrl(req) +"/resetPassword?token="+token+"&user_id="+user.getId()+"'>Reset Here</a>"+
                           "<p>If this wasn't you, just ignore this.</p>"+
                    "</body>"+
                 "</html>";
        try {
            sendMail(user.getEmail(),"Password reset",mailContent);
        } catch (MessagingException | GeneralSecurityException ex) {
           ex.printStackTrace();       
        }
    }
}
