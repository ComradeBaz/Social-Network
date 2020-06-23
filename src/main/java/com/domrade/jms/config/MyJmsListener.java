/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.jms.config;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author David
 */
//@Component
public class MyJmsListener {
    
    private Map<String, String> events = new HashMap<>();

    private final static Logger LOGGER = Logger.getLogger(MyJmsListener.class);

    @JmsListener(destination = "inbound.queue")
    public void receiveMessage(Map values) throws JMSException {
        events = values;
        LOGGER.log(Level.INFO, "Got a message: " + values);

    }
    
    public Map<String, String> getEvents() {
        return events;
    }
}
