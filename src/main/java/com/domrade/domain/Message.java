/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ComradeBazrft6
 */
@Entity
@Table(name = "message",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id"})
        })
public class Message extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "sender")
    @NotNull
    private User sender;
    
    @ManyToOne
    @JoinColumn(name = "receiver")
    @NotNull
    private User receiver;
    
    @ManyToOne
    @JoinColumn(name = "message_originator")
    @NotNull
    private User messageOriginator;
    
    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    @NotNull
    private User lastUpdatedBy;
    
    @Column(name="message_text")
    private String messageText;
    
    @OneToMany(
            mappedBy = "parentMessage", // parentMessage is the property name in com.domrade.domain.MessageReply
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
            //orphanRemoval = true
    )
    private Set<MessageReply> messageReplies = new HashSet<>();
    
    @Column(name = "time_stamp")
    private String timeStamp;
           
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "full_time_stamp")
    private Date fullTimeStamp;
    
    // Used to style the message depending on whether it's read or not
    @Column(nullable = false, 
            columnDefinition = "TINYINT(1) default 0")
    private boolean hasBeenRead;
    
    @Column(nullable = false)
    private String styling;
    
    @Column(name = "last_reply_message_test")
    private String lastReplyMessageText;

    public User getMessageSender() {
        return sender;
    }

    public void setMessageSender(User sender) {
        this.sender = sender;
    }

    public User getMessageReceiver() {
        return receiver;
    }

    public void setMessageReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getMessageOriginator() {
        return messageOriginator;
    }

    public void setMessageOriginator(User messageOriginator) {
        this.messageOriginator = messageOriginator;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Set<MessageReply> getMessageReplies() {
        return messageReplies;
    }

    public void setMessageReplies(Set<MessageReply> messageReplies) {
        this.messageReplies = messageReplies;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public boolean isHasBeenRead() {
        return hasBeenRead;
    }

    public void setHasBeenRead(boolean hasBeenRead) {
        this.hasBeenRead = hasBeenRead;
    }

    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getStyling() {
        return styling;
    }

    public void setStyling(String styling) {
        this.styling = styling;
    }

    public Date getFullTimeStamp() {
        return fullTimeStamp;
    }

    public void setFullTimeStamp(Date fullTimeStamp) {
        this.fullTimeStamp = fullTimeStamp;
    }

    public String getLastReplyMessageText() {
        return lastReplyMessageText;
    }

    public void setLastReplyMessageText(String lastReplyMessageText) {
        this.lastReplyMessageText = lastReplyMessageText;
    }
    
}
