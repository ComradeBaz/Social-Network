/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.sse.ManageServerSentEvents;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author David
 * 
 * When a client receives an SSE it is submitted via Ajax to this controller
 */
@Scope("request")
@Component
public class ServerSentEventController {
    
    private static final Logger LOGGER = Logger.getLogger(ServerSentEventController.class);
    
    @Autowired
    private ManageServerSentEvents manageServerSentEvents;
    
    private String messageCategory;
    private String messageType;
    private long userId;
    private long networkId;
    private long entityId;
    
    
    public ServerSentEventController() {
        // no arg constructor
    }
    
    public String handleClientSubmittedServerSentEvent() {
        
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        //String messageIdString = params.get("messageId");
        messageCategory = params.get("messageCategory");
        messageType = params.get("messageType");
        userId = Long.parseLong(params.get("userIdSse"));
        networkId = Long.parseLong(params.get("userNetworkIdSse"));
        try {
        entityId = Long.parseLong(params.get("entityId"));
        } catch (NumberFormatException npe) {
            entityId = -1;
        }
        
        LOGGER.log(Level.DEBUG, messageCategory + " " + messageType + " " + userId + " " + networkId + " " + entityId);
        
        manageServerSentEvents.processSse(messageType, messageCategory, userId, networkId, entityId);
        
        return "";
    }
}
