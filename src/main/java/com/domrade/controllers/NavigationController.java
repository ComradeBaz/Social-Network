/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.jsf.common.UserSessionObject;
import com.domrade.service.interfaces.INavigationService;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author ComradeBaz
 */
@Component
@Scope("request")
public class NavigationController {

    @Autowired
    private INavigationService navigationService;

    @Autowired
    private UserSessionObject userSessionObject;

    public String setUpNetwork() {
        return navigationService.getSetUpNetwork();
    }

    public String manageNetworkUsers() {
        // Set the collections on the session bean first
        userSessionObject.resetNetworkAdminCollections();
        return navigationService.getManageNetworkUsers();
    }

    public String goToNetwork() {
        // Set the collections on the session bean first
        userSessionObject.resetNetworkWallCollections();
        return navigationService.getTheNetwork();
    }

    public String goToHomePage() {
        return navigationService.getHomePage();
    }

    public String goToProfile() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String userIdString = params.get("userId");
        String loggedInUserIdString = params.get("userIdSse");
        
        return navigationService.getProfile(userIdString, loggedInUserIdString);
    }

    public String goToProfileSettings() {
        return navigationService.getProfileSettings();
    }

    public String goToEvents() {
        userSessionObject.resetEventCollections();
        return navigationService.getEvents();
    }

    public String goToNewEvent() {
        return navigationService.getNewEvent();
    }

    public String goToEventDetail() {
        return navigationService.getEventDetail();
    }

    public String goToMemberProfile() {
        return navigationService.getMemberProfile();
    }

    public String goToFriendsList() {
        return navigationService.getFriendsList();
    }

    public String goToMessages() {
        userSessionObject.resetMessageCollections();
        // User refers to friend collections if they wish to send a message
        userSessionObject.resetFriendCollections();
        return navigationService.getMessages();
    }

    public String goToWaitingConfirmation() {
        return navigationService.getWaitingConfirmation();
    }

    public String goToSetUpNetwork() {
        return navigationService.getSetUpNetwork();
    }

    public String goToRequestJoinNetwork() {
        return navigationService.getRequestJoinNetwork();
    }

    public String goToConfirmLeaveNetwork() {
        return navigationService.getConfirmLeaveNetwork();
    }

    public String goToLogUserOutOfNetworkConfirmation() {
        return navigationService.getLogUserOutOfNetworkConfirmation();
    }

    public String goToManageFriends() {
        // Set the collections on the session bean first
        userSessionObject.resetFriendCollections();
        return navigationService.getManageFriends();
    }
}
