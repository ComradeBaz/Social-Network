/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ComradeBaz
 */
@Entity
@Table(name = "message_reply",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id"})
        })
public class MessageReply extends BaseEntity implements Comparable {
    
    @ManyToOne
    @JoinColumn(name = "parent_message_id")
    @NotNull
    private Message parentMessage;
    
    @ManyToOne
    @JoinColumn(name = "receiver")
    @NotNull
    private User receiver;
    
    @ManyToOne
    @JoinColumn(name = "sender")
    @NotNull
    private User sender;
    
    @Column(name = "reply_text")
    private String replyText;
    
    @Column(name = "time_stamp")
    private String timeStamp;
    
    public MessageReply() {
        // no arg constructor
    }

    public Message getParentMessageId() {
        return parentMessage;
    }

    public void setParentMessageId(Message parentMessage) {
        this.parentMessage = parentMessage;
    }    

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
    
    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int compareTo(Object o) {
        long otherId = ((MessageReply)o).getId();
        
        return (int)(otherId - this.getId());
    }
}
