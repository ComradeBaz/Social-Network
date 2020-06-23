/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.helper.classes;

/**
 *
 * @author David
 */
public class MessageStyling {
    
    private final static String NOT_READ_STYLE = "font-weight: bold; font-size: 14px";
    private final static String READ_STYLE = "font-weight: normal; font-size: 14px";
    
    public MessageStyling() {
        // no arg constructor
    }
    
    public static String getNotReadSyle() {
        return NOT_READ_STYLE;
    }
    
    public static String getReadStyle() {
        return READ_STYLE;
    }
}
