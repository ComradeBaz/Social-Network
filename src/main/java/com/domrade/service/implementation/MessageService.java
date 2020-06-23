/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.service.interfaces.IMessageService;
import com.domrade.service.interfaces.IUserService;
import com.domrade.service.interfaces.BaseService;
import com.domrade.service.interfaces.ITimeAndDateService;
import com.domrade.domain.Message;
import com.domrade.domain.User;
import com.domrade.repository.IMessageRepository;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ComradeBaz
 */
@Service
public class MessageService extends BaseService<Message> implements IMessageService {
    
    private final String readStyling = "font-weight: normal; font-size: 14px;";
    private final String notReadStyling = "font-weight: bold; font-size: 14px;";
    
    @Autowired
    private IMessageRepository messageRepository;
    
    @Autowired
    private ITimeAndDateService timeAndDateService;
    
    @Autowired
    private IUserService userService;
    
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    @Override
    public Message get(Long id) {
        return messageRepository.findById(id).get();
    }
    
    @Override
    public void saveMessage(Message theMessage) {
        messageRepository.save(theMessage);
    }

    @Override
    public Set<Message> getReceivedMessagesByUser(User currentUser) {       
        return messageRepository.getReceivedMessagesByUser(currentUser);
    }
    
    @Override
    public Set<Message> getSentMessagesByUser(User currentUser) {
        return messageRepository.getSentMessagesByUser(currentUser);
    }
    
    @Override
    public Message getMessageById(long id) {
        return messageRepository.findById(id).get();
    }
    
    // Get the existing message trail between two users
    // If a trail exists new messages are added to the trail, if not then 
    // a new message trail is created
    @Override
    public Message getExistingMessage(User sender, User receiver) {
        List<Message> listOfMessages = messageRepository.getExistingMessages(sender, receiver);
        // If there is a message
        if(listOfMessages.size() > 0) {
            // Get the latest message in the trail
            return listOfMessages.get(0);
        }
        // Return null if no message exists
        return null;
    }
    
    @Override
    public List<Message> getExistingMessages(User sender) {
        return messageRepository.getExistingMessages(sender, sender);
    }

    @Override
    public List<Message> getCurrentUserMessages(User user) {
        return messageRepository.getCurrentUserMessages(user);
    }
    
    @Override
    public String getReadStyling() {
        return readStyling;
    }

    @Override
    public String getNotReadStyling() {
        return notReadStyling;
    }
    
    @Override
    public void sendWelcomeMessage(User newMember, User loggedInUser) {
        Message messageToNewNetworkMember = new Message();
        //User loggedInUser = sessionMB.getLoggedInUser();
        messageToNewNetworkMember.setSender(loggedInUser);
        messageToNewNetworkMember.setMessageOriginator(loggedInUser);
        messageToNewNetworkMember.setReceiver(newMember);
        messageToNewNetworkMember.setLastUpdatedBy(loggedInUser);
        String messageText = "Hi " + newMember.getFirstName() + ", welcome! To complete registration log out of the application and log back in.";
        messageToNewNetworkMember.setMessageText(messageText);
        messageToNewNetworkMember.setTimeStamp(timeAndDateService.getTimeAndDateForMessage());
        messageToNewNetworkMember.setFullTimeStamp(timeAndDateService.getTimeAndDateForMessageTimeStamp());
        messageToNewNetworkMember.setHasBeenRead(Boolean.FALSE);
        messageToNewNetworkMember.setStyling("placeholder");
        messageToNewNetworkMember.setLastReplyMessageText(messageText);
        saveMessage(messageToNewNetworkMember);
        userService.setUserHasNotification(newMember.getId());
    }
}
