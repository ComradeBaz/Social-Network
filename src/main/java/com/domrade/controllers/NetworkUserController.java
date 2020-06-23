/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.domain.Network;
import com.domrade.jms.config.JmsMessageCategories;
import com.domrade.jms.config.JmsMessageTypes;
import com.domrade.jsf.common.SessionMB;
import com.domrade.jsf.common.UserSessionObject;
import com.domrade.service.interfaces.IJmsService;
import com.domrade.service.interfaces.INavigationService;
import com.domrade.service.interfaces.INetworkService;
import com.domrade.service.interfaces.IUserService;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author David
 */
@Component("networkUserController")
@Scope("request")
public class NetworkUserController {
    
    private static final Logger LOGGER = Logger.getLogger(NetworkUserController.class);

    @Autowired
    private INetworkService networkService;

    @Autowired
    private IUserService userService;

    @Autowired
    private INavigationService navigationService;
        
    @Autowired
    private IJmsService jmsService;

    private SessionMB sessionMB;
    private String networkNameFromInputText;
    private String loggedInUsersNetworkName;
    private String networkNameUserRequestedToJoin;
    private UserSessionObject userSessionObject;

    public NetworkUserController() {
        // no arg constructor
    }

    @PostConstruct
    public void initBean() {
        //Network aNetwork = networkService.findOneById(userService.findById(userService.getLoggedInUserId()).getNetworkId());
        
        // Check if the user is a member of a network or has requested to join a network
        try {
            Network aNetwork = sessionMB.getCurrentNetwork();
            setLoggedInUsersNetworkName(aNetwork.getName());
        } catch (NullPointerException npe) {
            LOGGER.log(Level.INFO, "User is not a member of a network");            
        }
        try {
            setNetworkNameUserRequestedToJoin(networkService.getNetworkJoinRequestByLoggedInUser(sessionMB.getLoggedInUser()));
        } catch (NullPointerException npe) {
            LOGGER.log(Level.INFO, "User has not requested to join a network");
        }
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

    public String getNetworkNameFromInputText() {
        return networkNameFromInputText;
    }

    public void setNetworkNameFromInputText(String networkNameFromInputText) {
        this.networkNameFromInputText = networkNameFromInputText;
    }

    public String getLoggedInUsersNetworkName() {
        return loggedInUsersNetworkName;
    }

    public void setLoggedInUsersNetworkName(String loggedInUsersNetworkName) {
        this.loggedInUsersNetworkName = loggedInUsersNetworkName;
    }

    public String getNetworkNameUserRequestedToJoin() {
        return networkNameUserRequestedToJoin;
    }

    public void setNetworkNameUserRequestedToJoin(String networkNameUserRequestedToJoin) {
        this.networkNameUserRequestedToJoin = networkNameUserRequestedToJoin;
    }

    // Called when a user requests to join an existing network
    // The user enters the network name in a textBox on requestJoinNetwork.xhtml
    public String requestJoinNetwork() {
        long userId = userService.getLoggedInUserId();
        try {
            if (!(networkNameFromInputText.equals(""))) {
                networkService.requestJoinNetwork(networkNameFromInputText, userId);
                userService.setUserHasNotification(networkService.getNetworkAdminByNetworkId(networkService.getNetworkByName(networkNameFromInputText).getId()).getId());
                LOGGER.log(Level.INFO, "UserId " + userId + " requesting to join " + networkNameFromInputText);
                jmsService.formatAndSendData(JmsMessageTypes.REQUEST_JOIN_NETWORK.toString(), 
                        JmsMessageCategories.FOR_ADMIN.toString(), userId, networkService.getNetworkByName(networkNameFromInputText).getId(), sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());
            }
        } catch (NullPointerException npe) {
            LOGGER.log(Level.ERROR, "Error setting networkJoinRequest - check the network name \"" + networkNameFromInputText
                    + "\" is valid " + npe.getMessage());
            return navigationService.getErrorRequestingJoinNetwork();
        }

        return navigationService.getWaitingConfirmation();
    }

    // User wishes to leave the network of which they are a member
    @Transactional
    public String leaveNetwork() {
        // Get the logged in user
        long userId = userService.getLoggedInUserId();
        // Get the network of which they are a member
        Network theNetwork = networkService.findOneById(userService.findById(userService.getLoggedInUserId()).getNetworkId());
        networkService.leaveNetwork(theNetwork.getName(), userId);
        jmsService.formatAndSendData(JmsMessageTypes.LEAVE_NETWORK.toString(), 
                JmsMessageCategories.FOR_ADMIN.toString(), userId, networkService.getNetworkByName(sessionMB.getCurrentNetwork().getName()).getId(), sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return navigationService.getLoginPage();
    }

    // Cancel a request to join a network
    public String cancelRequestJoinNetwork() {
        long userId = sessionMB.getLoggedInUser().getId();
        networkService.cancelRequestJoinNetwork(userId);
        jmsService.formatAndSendData(JmsMessageTypes.CANCEL_REQUEST_JOIN_NETWORK.toString(), 
                JmsMessageCategories.FOR_ADMIN.toString(), userId, networkService.getNetworkByName(networkNameUserRequestedToJoin).getId(), sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());

        return navigationService.getHomePage();
    }
}
