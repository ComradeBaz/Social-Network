/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.domain.Message;
import com.domrade.domain.MessageReply;
import com.domrade.domain.User;
import com.domrade.helper.classes.BasicMessage;
import com.domrade.jms.config.JmsMessageCategories;
import com.domrade.jms.config.JmsMessageTypes;
import com.domrade.jsf.common.SessionMB;
import com.domrade.jsf.common.UserSessionObject;
import com.domrade.service.interfaces.IJmsService;
import com.domrade.service.interfaces.IMessageFormatterService;
import com.domrade.service.interfaces.IMessageReplyService;
import com.domrade.service.interfaces.IMessageService;
import com.domrade.service.interfaces.ITimeAndDateService;
import com.domrade.service.interfaces.IUserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ComradeBaz
 */
@Component("messageController")
@Scope("request")
public class MessageController implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(MessageController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private ITimeAndDateService timeAndDateService;

    @Autowired
    private IJmsService jmsService;

    private UserSessionObject userSessionObject;

    @Autowired
    private IMessageFormatterService messageFormatterService;

    private SessionMB sessionMB;
    private User receiver;
    private long receiverUserId;
    private User sender;
    private String messageText;
    private Message theMessage;
    private MessageReply messageReply;
    private Set<MessageReply> messageReplies;
    private String messageTextToShow;
    private List<BasicMessage> messagesForDataScroller;

    public MessageController() {
        // on arg constructor
    }

    @PostConstruct
    @Transactional
    public void initBean() {
        sender = sessionMB.getLoggedInUser();
        theMessage = new Message();
        messageReply = new MessageReply();
        messagesForDataScroller = new ArrayList<>();
    }
    
    @Autowired
    public void setUserSessionObject(UserSessionObject userSessionObject) {
        this.userSessionObject = userSessionObject;
    }

    public UserSessionObject getUserSessionObject() {
        return userSessionObject;
    }
    
    public SessionMB getSessionMB() {
        return sessionMB;
    }

    @Autowired
    public void setSessionMB(SessionMB sessionMB) {
        this.sessionMB = sessionMB;
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

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(long receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public Set<MessageReply> getMessageReplies() {
        return messageReplies;
    }

    public void setMessageReplies(Set<MessageReply> messageReplies) {
        this.messageReplies = messageReplies;
    }

    public Message getTheMessage() {
        return theMessage;
    }

    public void setTheMessage(Message theMessage) {
        this.theMessage = theMessage;
    }

    public MessageReply getMessageReply() {
        return messageReply;
    }

    public void setMessageReply(MessageReply messageReply) {
        this.messageReply = messageReply;
    }

    public String getMessageTextToShow() {
        return messageTextToShow;
    }

    public void setMessageTextToShow(String messageTextToShow) {
        this.messageTextToShow = messageTextToShow;
    }

    public List<BasicMessage> getMessagesForDataScroller() {
        return messagesForDataScroller;
    }

    public void setMessagesForDataScroller(List<BasicMessage> messagesForDataScroller) {
        this.messagesForDataScroller = messagesForDataScroller;
    }

    @Transactional
    public String sendMessage() {
        receiver = userService.findById(receiverUserId);
        // Check if a message trail exists in the db for the sender/receiver
        theMessage = messageService.getExistingMessage(sender, receiver);
        // A message trail may not exist between the two users
        // If it does add to that message trail rather than create a new Message object
        if (theMessage != null) {
            initialiseMessageReplyWhenUserInitiatesNewMessageButThereIsAnExistingMessageTrail(theMessage.getId());
            sessionMB.setCurrentMessage(theMessage);
            sendReply();

            //return "";
        } else {

            theMessage = new Message();
            theMessage.setMessageOriginator(sender);
            theMessage.setMessageSender(sender);
            theMessage.setMessageReceiver(receiver);
            theMessage.setLastUpdatedBy(sessionMB.getLoggedInUser());
            theMessage.setMessageText(messageText);
            theMessage.setTimeStamp(timeAndDateService.getTimeAndDateForMessage());
            theMessage.setFullTimeStamp(timeAndDateService.getTimeAndDateForMessageTimeStamp());
            theMessage.setHasBeenRead(Boolean.FALSE);
            theMessage.setStyling("placeholder");
            theMessage.setLastReplyMessageText(messageText);
            messageService.saveMessage(theMessage);
            // Set hasNotification to true for the other user
            userService.setUserHasNotification(receiver.getId());

            LOGGER.log(Level.DEBUG, sender + " sent a message to " + receiver);

            // Update message collection for the sender/receiver
            jmsService.formatAndSendData(JmsMessageTypes.SEND_MESSAGE.toString(),
                    JmsMessageCategories.FOR_USERS.toString(), receiver.getId(), receiver.getNetworkId(), sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());
            userSessionObject.setCurrentUserMessagesUpdated(Boolean.TRUE);
            userSessionObject.resetMessageCollections();

        }

        return "";
    }

    @Transactional
    public String sendReply() {
        messageReplies = sessionMB.getCurrentMessage().getMessageReplies();
        messageReply.setReceiver(sessionMB.getCurrentMessage().getMessageSender());
        // Check that sender is different to receiver
        if(messageReply.getReceiver().equals(sessionMB.getLoggedInUser())) {
            messageReply.setReceiver(sessionMB.getCurrentMessage().getMessageReceiver());
        }
        messageReply.setParentMessageId(sessionMB.getCurrentMessage());
        messageReply.setSender(sessionMB.getLoggedInUser());
        messageReply.setTimeStamp(timeAndDateService.getTimeAndDateForMessage());
        sessionMB.getCurrentMessage().setLastUpdatedBy(sessionMB.getLoggedInUser());
        // Check if messageReply.replyText has been set by the user replying to a message
        // If it has not been set get the text from the new message inputTextArea
        if (messageReply.getReplyText() == null) {
            messageReply.setReplyText(messageText);
            sessionMB.getCurrentMessage().setLastReplyMessageText(messageText);
        }
        messageReplies.add(messageReply);
        sessionMB.getCurrentMessage().setHasBeenRead(Boolean.FALSE);
        sessionMB.getCurrentMessage().setLastReplyMessageText(messageReply.getReplyText());
        sessionMB.getCurrentMessage().setFullTimeStamp(timeAndDateService.getTimeAndDateForMessageTimeStamp());
        sessionMB.getCurrentMessage().setMessageReplies(messageReplies);
        messageService.saveMessage(sessionMB.getCurrentMessage());
        // Set hasNotification to true for the other user
        userService.setUserHasNotification(messageReply.getReceiver().getId());
        sessionMB.setTheMessage(sessionMB.getCurrentMessage().getId());
        long userIdForJms = messageReply.getReceiver().getId();
        long loggedInUserId = sessionMB.getLoggedInUser().getId();
        if (userIdForJms == sessionMB.getLoggedInUser().getId()) {
            userIdForJms = sessionMB.getCurrentMessage().getReceiver().getId();
        }
        jmsService.formatAndSendDataWithEntityId(JmsMessageTypes.SEND_MESSAGE.toString(), JmsMessageCategories.FOR_USERS.toString(), userIdForJms,
                messageReply.getReceiver().getNetworkId(), sessionMB.getCurrentMessage().getId(), sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());

        messageReply = new MessageReply();
        userSessionObject.setCurrentUserMessagesUpdated(Boolean.TRUE);
        userSessionObject.resetMessageCollections();
        userSessionObject.setMessagesForDataScroller(messageFormatterService.formatMessagesForView(sessionMB.getCurrentMessage(), sessionMB.getCurrentMessage().getMessageReplies()));

        return "";
    }

    @Transactional
    public void initialiseMessageReplyWhenUserInitiatesNewMessageButThereIsAnExistingMessageTrail(long messageId) {
        sessionMB.setCurrentMessage(messageService.getMessageById(messageId));
        messageReplies = sessionMB.getCurrentMessage().getMessageReplies();
    }
}
