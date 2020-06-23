/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

import com.domrade.domain.Message;
import com.domrade.domain.User;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 *
 * @author ComradeBaz
 */
@Service
public interface IMessageService {
    
    public void saveMessage(Message theMessage);
    
    public Set<Message> getReceivedMessagesByUser(User currentUser);
    
    public Set<Message> getSentMessagesByUser(User currentUser);
    
    public Message getMessageById(long id);
    
    public List<Message> getExistingMessages(User sender);
    
    public Message getExistingMessage(User sender, User receiver);
    
    public String getReadStyling();
    
    public String getNotReadStyling();
    
    public List<Message> getCurrentUserMessages(User user);
    
    public void sendWelcomeMessage(User newMember, User loggedInUser);
}
