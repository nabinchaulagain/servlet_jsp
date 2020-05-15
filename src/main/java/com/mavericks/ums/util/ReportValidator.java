package com.mavericks.ums.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nabin
 */
public class ReportValidator {
    public static Map<String,String> validate(String from, String to){
        Map<String,String> errors = new HashMap<>();
        if(from.length() == 0){
            errors.put("from", "This is required");
        }
        if(to.length() == 0){
            errors.put("to", "This is required");
        }
        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher fromMatcher = datePattern.matcher(from);
        Matcher toMatcher = datePattern.matcher(to);
        if(!errors.containsKey("from") && !fromMatcher.matches()){
            errors.put("from","Invalid date");
        }
        if(! errors.containsKey("to") && !toMatcher.matches()){
            errors.put("to","Invalid date");
        }
        return errors;
    }
}
