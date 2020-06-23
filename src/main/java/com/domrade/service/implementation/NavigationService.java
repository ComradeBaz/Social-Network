/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.service.interfaces.IUserService;
import com.domrade.service.interfaces.INavigationService;
import com.domrade.domain.Role;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ComradeBaz
 */
@Service
public class NavigationService implements INavigationService {

    @Autowired
    private IUserService userService;

    @Override
    public String getSetUpNetwork() {
        return "/secured/view/setUpNetwork.xhtml?faces-redirect=true";
    }

    @Override
    public String getManageNetworkUsers() {
        return "/secured/view/manageNetworkUsers.xhtml?faces-redirect=true";
    }

    @Override
    public String getTheNetwork() {
        return "/secured/view/network.xhtml?faces-redirect=true";
    }

    @Override
    public String getHomePage() {
        List<Role> userRoles = userService.getUserRole(userService.getLoggedInEmailAddress());
        String oneRole = userRoles.get(0).toString();

        switch (oneRole) {
            case "ROLE_ADMIN":
                return getSetUpNetwork();
            case "ROLE_ADMIN_MEMBER":
                return getProfileSettings();
            case "ROLE_USER":
                return getProfileSettings();
            case "ROLE_USER_JOIN_REQUEST_SENT":
                return getWaitingConfirmation();
            case "ROLE_MEMBER":
                return "/secured/view/profile.xhtml?faces-redirect=true";
        }
        return "/secured/view/home.xhtml";
    }

    @Override
    public String getSignUpPageTwo() {
        return "/signuppage2.xhtml";
    }

    @Override
    public String getLoginPage() {
        return "/login.xhtml?faces-redirect=true";
    }

    @Override
    public String getWaitingConfirmation() {
        return "/secured/view/waitingConfirmation.xhtml?faces-redirect=true";
    }

    @Override
    public String getRequestJoinNetwork() {
        return "/secured/view/requestJoinNetwork.xhtm?faces-redirect=truel";
    }
    
    @Override
    public String getErrorRequestingJoinNetwork() {
        return "/secured/view/errorRequestingJoinNetwork.xhtml";
    }
    
    @Override
    public String getProfile(String targetUserId, String loggedInUserId) {
        if(Long.parseLong(targetUserId) == (Long.parseLong(loggedInUserId))) {
            return "/secured/view/profile.xhtml?faces-redirect=true";
        }
        return "/secured/view/memberProfile.xhtml?faces-redirect=true";
    }
    
    @Override
    public String getProfileSettings() {
        return "/secured/view/profileSettings.xhtml?faces-redirect=true";
    }
    
    @Override
    public String getEvents() {
        return "/secured/view/events.xhtml?faces-redirect=true";
    }
    
    @Override
    public String getNewEvent() {
        return "/secured/view/newEvent.xhtml?faces-redirect=true";
    }
    
    @Override
    public String getEventDetail() {
        return "/secured/view/eventDetail.xhtml?faces-redirect=true";
    }
    
    @Override
    public String getMemberProfile() {
        return "/secured/view/memberProfile.xhtml?faces-redirect=true";
    }
    
    @Override
    public String getFriendsList() {
        return "/secured/view/manageFriends.xhtml?faces-redirect=true";
    }

    @Override
    public String getMessages() {
        return "/secured/view/messages.xhtml?faces-redirect=true";
    }
    
    @Override
    public String getConfirmLeaveNetwork() {
        return "/secured/view/confirmLeaveNetwork.xhtml";
    }

    @Override
    public String getLogUserOutOfNetworkConfirmation() {
        return "/secured/view/logUserOutOfNetworkConfirmation.xhtml";
    }

    @Override
    public String getManageFriends() {
        return "/secured/view/manageFriends.xhtml?faces-redirect=true";
    }
    
    @Override
    public String getDestination(String requestSource) {
        // Called from sessionMB.setUserForBean() to determine navigation
        switch (requestSource) {
            case "MANAGE_FRIENDS":
                return "/secured/view/memberProfile.xhtml?faces-redirect=true";
            case "MANAGE_NETWORK_USERS":
                return "/secured/view/logUserOutOfNetworkConfirmation.xhtml";
            case "NETWORK":
                return "/secured/view/memberProfile.xhtml?faces-redirect=true";
        }
        return "";
    }
}
