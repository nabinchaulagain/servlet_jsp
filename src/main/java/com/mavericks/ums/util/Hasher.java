/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericks.ums.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author nabin
 */
public class Hasher {
    
    public static String hash(String str) {
	String salt = BCrypt.gensalt(12);
	String hashedStr = BCrypt.hashpw(str, salt);
	return hashedStr;
    }
    public static boolean check(String str, String hashedStr){ 
	boolean password_verified = BCrypt.checkpw(str, hashedStr);
	return password_verified;
    }
}