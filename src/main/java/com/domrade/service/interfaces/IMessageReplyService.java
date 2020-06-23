/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

import com.domrade.domain.MessageReply;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public interface IMessageReplyService {    
    public void saveMessageReply(MessageReply messageReply);
    
}
