/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.service.interfaces.IMessageFormatterService;
import com.domrade.service.interfaces.IMessageService;
import com.domrade.service.interfaces.IUserService;
import com.domrade.domain.Message;
import com.domrade.domain.MessageReply;
import com.domrade.domain.User;
import com.domrade.helper.classes.BasicMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class MessageFormatterService implements IMessageFormatterService {
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IMessageService messageService;
    
    @Override
    public Set sortMessageReplies(Set<MessageReply> messageReplies) {
        ArrayList<MessageReply> tempList = new ArrayList(messageReplies);
        //Collections.sort(tempList);
        tempList.sort(Comparator.comparing(MessageReply::getId).reversed());
        
        return new LinkedHashSet<>(tempList);
    }
    
    @Override
    public List<BasicMessage> formatMessagesForView(Message message, Set<MessageReply> messageReplies) {
        List<BasicMessage> returnValues = new ArrayList<>();
        
        BasicMessage basicMessage;// = new BasicMessage();
        BasicMessage originalMessage;
        StringBuilder sb = new StringBuilder();
        //sb.append(message.getMessageSender().getFirstName());
        sb.append(message.getMessageOriginator().getFirstName());
        sb.append(" ");
        //sb.append(message.getMessageSender().getLastName());
        sb.append(message.getMessageOriginator().getLastName());
        originalMessage = new BasicMessage(sb.toString(), message.getMessageText(), message.getTimeStamp(), message.getId());
        // Sort message replies
        messageReplies = sortMessageReplies(messageReplies);

        for (MessageReply reply : messageReplies) {
            sb.delete(0, sb.length());
            sb.append(reply.getSender().getFirstName());
            sb.append(" ");
            sb.append(reply.getSender().getLastName());
            // Add data to list which is displayed by a dataScroller in messages.xhtml
            basicMessage = new BasicMessage(sb.toString(), reply.getReplyText(), reply.getTimeStamp(), reply.getId());
            returnValues.add(basicMessage);
        }
        // Add the original message to the end of the list so it appears last
        returnValues.add(originalMessage);
        
        return returnValues;
    }
    
    // Helper to swap sender for receiver so that on messages.xhtml the user
    // to whom the currentUser sent messages to or received messages from
    // is listed as the "Sender" even if the currentUser initiated the conversation. 
    // userId is the Id of the logged in user from userSessionObject
    @Override
    public List<Message> changeSenderReceiverForCurrentUserMessages(List<Message> currentUserMessages, long userId) {
        for (Message m : currentUserMessages) {
            User messageSender = m.getMessageSender();
            User messageReceiver = m.getMessageReceiver();
            if (messageReceiver.equals(userService.findById(userId))) {
                // Set the receiver to the other party to the conversation
                m.setMessageReceiver(messageSender);
                m.setMessageSender(messageReceiver);
            }
            setStylingForCurrentUserMessages(currentUserMessages);
        }
        return currentUserMessages;
    }

    // Helper to set styling for messages listed in the user's inbox
    @Override
    public void setStylingForCurrentUserMessages(List<Message> currentUserMessages) {
        for (Message m : currentUserMessages) {
            if (m.isHasBeenRead()) {
                m.setStyling(messageService.getReadStyling());
            } else if ((!(m.isHasBeenRead())) && (m.getLastUpdatedBy().equals(userService.findById(userService.getLoggedInUserId())))) {
                m.setStyling(messageService.getReadStyling());
            } else {
                m.setStyling(messageService.getNotReadStyling());
            }
        }
    }
}
