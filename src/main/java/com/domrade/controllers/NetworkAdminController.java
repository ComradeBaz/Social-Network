/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.domain.Network;
import com.domrade.domain.Role;
import com.domrade.domain.User;
import com.domrade.jms.config.JmsMessageCategories;
import com.domrade.jms.config.JmsMessageTypes;
import com.domrade.jsf.common.UserSessionObject;
import com.domrade.jsf.common.SessionMB;
import com.domrade.service.interfaces.IJmsService;
import com.domrade.service.interfaces.IMessageService;
import com.domrade.service.interfaces.INavigationService;
import com.domrade.service.interfaces.INetworkService;
import com.domrade.service.interfaces.IUserService;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ComradeBaz
 */
@Component("networkAdminController")
@Scope("request")
public class NetworkAdminController {

    @Autowired
    private INetworkService networkService;

    @Autowired
    private IUserService userService;

    @Autowired
    private INavigationService navigationService;
        
    @Autowired
    private IJmsService jmsService;
    
    @Autowired
    private IMessageService messageService;

    private Network network;
    private SessionMB sessionMB;
    private String networkNameFromInputText;
    private UserSessionObject userSessionObject;

    private static final Logger LOGGER = Logger.getLogger(SessionMB.class);

    @PostConstruct
    public void initBean() {
        network = new Network();
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

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public String getNetworkNameFromInputText() {
        return networkNameFromInputText;
    }

    public void setNetworkNameFromInputText(String networkNameFromInputText) {
        this.networkNameFromInputText = networkNameFromInputText;
    }

    @Transactional
    public void logUserOutOfNetwork(ActionEvent ae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String userIdString = params.get("userId");
        Network theNetwork = sessionMB.getCurrentNetwork();
        long anotherUserId = Long.parseLong(userIdString);
        networkService.leaveNetwork(theNetwork.getName(), anotherUserId);
        // Update collections for the other user
        //jmsService.formatAndSendData(JmsMessageTypes.LOG_USER_OUT_OF_NETWORK.toString(),
                //JmsMessageCategories.FOR_USERS.toString(), anotherUserId, theNetwork.getId(), sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());
        // Update collections for network admin
        userSessionObject.setNetworkMembersUpdated(Boolean.TRUE);
        userSessionObject.resetNetworkAdminCollections();

        //return navigationService.getManageNetworkUsers();
    }
    
    // Set up a new network
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String establishNetwork() {
        User aUser = sessionMB.getLoggedInUser();

        network.setEmail(aUser.getEmail());
        //network.setIpAddress(locationServicesJsonObject.get("ip").getAsString());

        // Check location service but in a try block
        // If the user ip is a reserved (eg 127.0.0.1) no info is returned by the service
        //try {
            //network.setCity(locationServicesJsonObject.get("city").getAsString());
            //network.setCountry(locationServicesJsonObject.get("country_name").getAsString());
            //network.setPostal(locationServicesJsonObject.get("postal").getAsString());
        //} catch (NullPointerException npe) {
            //LOGGER.log(Level.WARN, "No information returned from locationService for IP Address" + locationServicesJsonObject.get("ip").getAsString());
            // Set default data
            network.setCity(network.getCity());
            network.setCountry(network.getCountry());
            network.setPostal("default-postal");
            network.setIpAddress("default-ip-address");
            sessionMB.setCurrentNetwork(network);
        //}

        networkService.save(network);
        // Add the user that established the network to the network member list
        aUser.setNetworkId(networkService.getNetworkIdByEmailAddress(aUser.getEmail()));
        // Change the Role of the user
        aUser.setRole(Role.ROLE_ADMIN_MEMBER);
        userService.save(aUser);

        network = new Network();

        return navigationService.getTheNetwork();
    }

    // A network admin must accept a request to join a network before
    // a user to interact with the network
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN_MEMBER')")
    public void acceptRequest(ActionEvent ae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String userIdString = params.get("userId");
        
        User aUser = userService.findById(Long.parseLong(userIdString));
        
        networkService.acceptRequestJoinNetwork(userIdString);
        // Send a message to the user to inform them they have been added to the network
        messageService.sendWelcomeMessage(aUser, sessionMB.getLoggedInUser());
        // Send sse to inform user their request has been approved
        jmsService.formatAndSendData(JmsMessageTypes.SEND_MESSAGE.toString(),
                JmsMessageCategories.FOR_USERS.toString(), Long.parseLong(userIdString), sessionMB.getLoggedInUser().getNetworkId(), sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());
        // Send sse to inform other network users that there is a new member
        jmsService.formatAndSendData(JmsMessageTypes.NETWORK_WALL_UPDATED.toString(),
                JmsMessageCategories.FOR_NETWORK_MEMBERS.toString(), Long.parseLong(userIdString), sessionMB.getLoggedInUser().getNetworkId(), aUser.getFirstName() + " " + aUser.getLastName());
        userSessionObject.setWallPostsUpdated(Boolean.TRUE);
        userSessionObject.resetNetworkWallCollections();
        // Update the list of pendingJoinRequests
        userSessionObject.setPendingJoinRequestsUpdated(Boolean.TRUE);
        userSessionObject.setNetworkMembersUpdated(Boolean.TRUE);
        userSessionObject.resetNetworkAdminCollections();
        // Update messages to include the message sent to the new member
        userSessionObject.setCurrentUserMessagesUpdated(Boolean.TRUE);
        userSessionObject.resetMessageCollections();
    }

    // A network admin may reject a request to join a network
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN_MEMBER')")
    public void rejectRequest(ActionEvent ae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String userIdString = params.get("userId");
        
        networkService.cancelRequestJoinNetwork(Long.parseLong(userIdString));
        // Send sse to inform user their request has been approved
        jmsService.formatAndSendData(JmsMessageTypes.DENY_REQUEST_JOIN_NETWORK.toString(),
                JmsMessageCategories.FOR_USERS.toString(), Long.parseLong(userIdString), sessionMB.getLoggedInUser().getNetworkId(), sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());

        // Update the list of pendingJoinRequests
        userSessionObject.setPendingJoinRequestsUpdated(Boolean.TRUE);
        userSessionObject.resetNetworkAdminCollections();
    } 
}
