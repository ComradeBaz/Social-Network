/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

import com.domrade.domain.Message;
import com.domrade.domain.MessageReply;
import com.domrade.helper.classes.BasicMessage;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public interface IMessageFormatterService {
    
    public List<Message> changeSenderReceiverForCurrentUserMessages(List<Message> currentUserMessages, long userId);
    
    public void setStylingForCurrentUserMessages(List<Message> currentUserMessages);
    
    public List<BasicMessage> formatMessagesForView(Message message, Set<MessageReply> messageReplies);
    
    public Set sortMessageReplies(Set<MessageReply> messageReplies);
    
}
