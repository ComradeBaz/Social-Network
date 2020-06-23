/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.service.interfaces.IJmsService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class JmsService implements IJmsService {
    
    private static final Logger LOGGER = Logger.getLogger(JmsService.class);

    @Autowired
    public JmsTemplate jmsTemplate;
    
    //@Override
    @SendTo("inboud.queue")
    private void send(Map values) {
        jmsTemplate.setDefaultDestinationName("inbound.queue");
        jmsTemplate.convertAndSend(values);
    }
    
    /*@SendTo("sse.queue")
    private void sendGson(String gsonString) {
    jmsTemplate.setDefaultDestinationName("sse.queue");
    jmsTemplate.convertAndSend(gsonString);
    }*/
    
    @SendTo("sse.topic")
    private void sendGson(String gsonString) {
        jmsTemplate.setDefaultDestinationName("sse.topic");
        jmsTemplate.convertAndSend(gsonString);
    }
    
    @SendTo("sse.browser.topic")
    private void sendGsonToBrowserTopic(String gsonString) {
        jmsTemplate.setDefaultDestinationName("sse.browser.topic");
        jmsTemplate.convertAndSend(gsonString);
    }

    @Override
    public void formatAndSendData(String messageType, String messageCategory, long userId, long networkId, String userWhoGeneratedEvent) {
        LOGGER.log(Level.DEBUG, "Sending JMS with no entityId");
        String messageTypeLabel = "messageType";
        String messageCategoryLabel = "messageCategory";
        String userIdLabel = "userId";
        String networkIdLabel = "networkId";
        String userWhoGeneratedEventLabel = "userWhoGeneratedEvent";
        
        Map<String, String> toSend = new HashMap<>();
        toSend.put(messageTypeLabel, messageType);
        toSend.put(userIdLabel, Long.toString(userId));
        toSend.put(networkIdLabel, Long.toString(networkId));
        toSend.put(messageCategoryLabel, messageCategory);
        toSend.put(userWhoGeneratedEventLabel, userWhoGeneratedEvent);
        //send(toSend);
        
        Gson gson = new Gson();
        String gsonString = gson.toJson(toSend);
        LOGGER.log(Level.INFO, "Gson String to send: " + gsonString);
        sendGson(gsonString);
        //sendGsonToBrowserTopic(gsonString);
    }
    
    @Override
    public void formatAndSendDataWithEntityId(String messageType, String messageCategory, long userId, long networkId, long entityId, String userWhoGeneratedEvent) {
        LOGGER.log(Level.DEBUG, "Sending JMS with entityId");
        String messageTypeLabel = "messageType";
        String messageCategoryLabel = "messageCategory";
        String userIdLabel = "userId";
        String networkIdLabel = "networkId";
        String entityIdLabel = "entityId";
        String userWhoGeneratedEventLabel = "userWhoGeneratedEvent";
        
        Map<String, String> toSend = new HashMap<>();
        toSend.put(messageTypeLabel, messageType);
        toSend.put(userIdLabel, Long.toString(userId));
        toSend.put(networkIdLabel, Long.toString(networkId));
        toSend.put(entityIdLabel, Long.toString(entityId));
        toSend.put(messageCategoryLabel, messageCategory);
        toSend.put(userWhoGeneratedEventLabel, userWhoGeneratedEvent);
        //send(toSend);
        
        Gson gson = new Gson();
        String gsonString = gson.toJson(toSend);
        LOGGER.log(Level.INFO, "Gson String to send: " + gsonString);
        sendGson(gsonString);
        //sendGsonToBrowserTopic(gsonString);
    }
}
