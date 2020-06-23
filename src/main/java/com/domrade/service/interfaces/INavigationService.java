/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

import org.springframework.stereotype.Service;

/**
 *
 * @author ComradeBaz
 */
@Service
public interface INavigationService {
    
    public String getSetUpNetwork();
    
    public String getTheNetwork();
    
    public String getHomePage();
    
    public String getSignUpPageTwo();
    
    public String getLoginPage();
    
    public String getManageNetworkUsers();
    
    public String getWaitingConfirmation();
    
    public String getRequestJoinNetwork();
    
    public String getErrorRequestingJoinNetwork();
    
    public String getProfile(String targetUserId, String loggedInUserId);
    
    public String getProfileSettings();
    
    public String getEvents();
    
    public String getNewEvent();
    
    public String getEventDetail();
    
    public String getMemberProfile();
    
    public String getFriendsList();
    
    public String getMessages();
    
    public String getConfirmLeaveNetwork();
    
    public String getLogUserOutOfNetworkConfirmation();
    
    public String getManageFriends();
    
    public String getDestination(String requestSource);
}
