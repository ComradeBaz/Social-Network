/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.service.interfaces.IMessageReplyService;
import com.domrade.domain.MessageReply;
import com.domrade.repository.IMessageReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class MessageReplyService implements IMessageReplyService {
    
    @Autowired
    private IMessageReplyRepository messageReplyRepository;

    @Override
    public void saveMessageReply(MessageReply messageReply) {
        messageReplyRepository.save(messageReply);
    }  
}
