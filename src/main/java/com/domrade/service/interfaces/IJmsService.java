/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

import java.util.Map;

/**
 *
 * @author David
 */
public interface IJmsService {
    
    //public void send(Map values);
    
    //public void formatAndSendData(String messageType, long id);
    
    public void formatAndSendData(String messageType, String messageCategory, long userId, long networkId, String userWhoGeneratedEvent);
    
    public void formatAndSendDataWithEntityId(String messageType, String messageCategory, long userId, long networkId, long entityId, String userWhoGeneratedEvent);
    
}
