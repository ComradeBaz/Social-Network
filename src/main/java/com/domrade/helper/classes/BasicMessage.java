/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.helper.classes;

/**
 *
 * @author ComradeBaz
 */
public class BasicMessage {
    
    private String senderName;
    private String messageText;
    private String senderPlusText;
    private String senderTextTimeStamp;
    private String timeStamp;
    private long messageId;
    
    public BasicMessage() {
        // no arg constructor
    }
    
    public BasicMessage(String senderName, String messageText, String timeStamp, long messageId) {
        this.senderName = senderName;
        this.messageText = messageText;
        this.senderPlusText = senderName + "\n" + messageText;
        this.timeStamp = timeStamp;
        this.senderTextTimeStamp = senderName + "\n" + messageText + "\n" + timeStamp;
        this.messageId = messageId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderPlusText() {
        return senderPlusText;
    }

    public void setSenderPlusText(String senderPlusText) {
        this.senderPlusText = senderPlusText;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSenderTextTimeStamp() {
        return senderTextTimeStamp;
    }

    public void setSenderTextTimeStamp(String senderTextTimeStamp) {
        this.senderTextTimeStamp = senderTextTimeStamp;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
}
