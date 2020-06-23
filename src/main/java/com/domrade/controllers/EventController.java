/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.domain.Event;
import com.domrade.domain.User;
import com.domrade.jms.config.JmsMessageCategories;
import com.domrade.jms.config.JmsMessageTypes;
import com.domrade.jsf.common.SessionMB;
import com.domrade.jsf.common.UserSessionObject;
import com.domrade.service.interfaces.IEventService;
import com.domrade.service.interfaces.IJmsService;
import com.domrade.service.interfaces.INavigationService;
import com.domrade.service.interfaces.INetworkService;
import java.io.Serializable;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ComradeBaz
 */
@Component("eventController")
@Scope("request")
public class EventController implements Serializable {

    @Autowired
    private IEventService eventService;

    @Autowired
    private INetworkService networkService;

    @Autowired
    private INavigationService navigationService;

    @Autowired
    private IJmsService jmsService;

    private SessionMB sessionMB;
    
    private UserSessionObject userSessionObject;

    private Event event = new Event();

    public EventController() {
        // no arg constructor
    }
    
    @Autowired
    public void setUserSessionObject(UserSessionObject userSessionObject) {
        this.userSessionObject = userSessionObject;
    }
    
    public UserSessionObject getUserSessionObject() {
        return userSessionObject;
    }

    public SessionMB getSessionMB() {
        return sessionMB;
    }

    @Autowired
    public void setSessionMB(SessionMB sessionMB) {
        this.sessionMB = sessionMB;
    }
    
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    // Events are created by a user - they are group activities and other users 
    // sign up to attend. Examples such as Beer Pong, Pub Crawl etc
    public String saveEvent() {
        event.setOwner(sessionMB.getLoggedInUser());
        User aUser = sessionMB.getLoggedInUser();
        event.setNetwork(networkService.findOneById(userSessionObject.getLoggedInUser().getNetworkId()));
        eventService.save(event);
        event = new Event();

        jmsService.formatAndSendData(JmsMessageTypes.EVENTS_UPDATED.toString(),
                JmsMessageCategories.FOR_NETWORK_MEMBERS.toString(), aUser.getId(), aUser.getNetworkId(), sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());
        networkService.setMembersHaveNotification(aUser.getNetworkId(), aUser.getId());
        userSessionObject.setEventsUpdated(Boolean.TRUE);
        userSessionObject.resetEventCollections();

        return navigationService.getEvents();
    }

    // Save the user to the event
    // The user will be listed as an attendee on the event view
    @Transactional
    public String addUserToEvent() {
        Event currentEvent = sessionMB.getCurrentEvent();
        Set<User> theEventGuests = currentEvent.getTheUsers();
        theEventGuests.add(sessionMB.getLoggedInUser());
        eventService.save(currentEvent);

        return "";
    }
}
