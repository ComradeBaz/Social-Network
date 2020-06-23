/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.domain.Friend;
import com.domrade.domain.User;
import com.domrade.jms.config.JmsMessageCategories;
import com.domrade.jms.config.JmsMessageTypes;
import com.domrade.jsf.common.SessionMB;
import com.domrade.jsf.common.UserSessionObject;
import com.domrade.service.interfaces.IFriendService;
import com.domrade.service.interfaces.IJmsService;
import com.domrade.service.interfaces.IUserService;
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
@Component("friendController")
@Scope("request")
public class FriendController {

    private static final Logger LOGGER = Logger.getLogger(FriendController.class);

    @Autowired
    public IFriendService friendService;

    @Autowired
    public IUserService userService;

    @Autowired
    public IJmsService jmsService;

    public SessionMB sessionMB;
    private UserSessionObject userSessionObject;

    public FriendController() {
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

    /*
        Function is called from memberProfile.xhtml to determine
        what Friend interaction the sessionMB.loggedInUser wants to have with
        sessionMB.anotherUser
     */
    public void startFriendInteraction() {
        // Get the friend object if it exists
        Friend friendObject = friendService.getAFriendRequestBySenderAndReceiver(sessionMB.getLoggedInUser().getId(), sessionMB.getAnotherUser().getId());

        if (friendObject == null) {
            sendFriendRequest();
            sessionMB.setFriendBooleans();
            return;
        }
        if (friendObject.isEnabled()) {
            deleteFriend();
        } else {
            if (friendObject.getRequesterId() == sessionMB.getLoggedInUser().getId()) {
                deleteFriendRequestSent();
            } else {
                deleteFriendRequestReceived();
            }

        }
        // Reset booleans so the correct modal is displayed if the loggedInUser
        // clicks the add/delete friend button from memberProfile.xhtml
        sessionMB.setFriendBooleans();
    }

    // Add a new disabled friend to the database to be later approved or deleted 
    // by the user that received a friend request
    @Transactional
    public String sendFriendRequest() {
        // Get the id of the user to whom the logged in user wants to send the request
        // It is saved when sessionMB.setUserForBean() is called
        long anotherUserId = sessionMB.getAnotherUser().getId();
        long networkId = sessionMB.getAnotherUser().getNetworkId();
        long currentUser = userService.getLoggedInUserId();
        LOGGER.log(Level.INFO, sessionMB.getLoggedInUser() + " sending friend request to " + sessionMB.getAnotherUser());
        Friend aUserAsFriend = new Friend(currentUser, anotherUserId, Boolean.FALSE);
        friendService.saveFriend(aUserAsFriend);
        // Set hasNotification to true for the other user
        userService.setUserHasNotification(anotherUserId);
        // Reset relevant collections for the logged in user
        userSessionObject.setCurrentUserFriendRequestsSentUpdated(Boolean.TRUE);
        userSessionObject.resetFriendCollections();
        // Update collections for the other user
        jmsService.formatAndSendData(JmsMessageTypes.SEND_FRIEND_REQUEST.toString(), JmsMessageCategories.FOR_USERS.toString(), 
                anotherUserId, networkId, sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());        

        return "";
    }

    // When a user sends a friend request a friend object is created
    // The friend object includes the user_id (who received the request),
    // the requestSentBy (who sent the request) and the id of the friend object
    // The user_id (who received the request) should approve/deny the request
    @Transactional
    public String acceptFriendRequest() {
        // Get the id of the user to whom the request was sent
        long anotherUserId = sessionMB.getAnotherUser().getId();
        long networkId = sessionMB.getAnotherUser().getNetworkId();
        // Get the logged in User
        User currentUser = userService.findById(userService.getLoggedInUserId());
        LOGGER.log(Level.INFO, currentUser + " accepting friend request from " + sessionMB.getAnotherUser());
        // Get the Friend object that represents the friend request
        Friend theFriendRequest = friendService.getAFriendRequestBySenderAndReceiver(sessionMB.getAnotherUser().getId(),
                currentUser.getId(), Boolean.FALSE);
        // Enable the Friend object and save it
        theFriendRequest.setEnabled(Boolean.TRUE);
        friendService.saveFriend(theFriendRequest);
        // Set hasNotification to true for the other user
        userService.setUserHasNotification(anotherUserId);
        userSessionObject.setUserFriendsAsUsersUpdated(Boolean.TRUE);
        userSessionObject.setPendingUserFriendRequestsUpdated(Boolean.TRUE);
        userSessionObject.resetFriendCollections();
        jmsService.formatAndSendData(JmsMessageTypes.ACCEPT_FRIEND_REQUEST.toString(),
                JmsMessageCategories.FOR_USERS.toString(), anotherUserId, networkId, sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());
        // Reset collections with friends/friendRequests        

        return "";
    }

    @Transactional
    public String deleteFriendRequestSent() {
        // Get the id of the user to whom the logged in user wants to send the request
        // It is saved when sessionMB.setUserForBean() is called
        long anotherUserId = sessionMB.getAnotherUser().getId();
        long networkId = sessionMB.getAnotherUser().getNetworkId();
        // Get the logged in User
        User currentUser = userService.findById(userService.getLoggedInUserId());
        LOGGER.log(Level.INFO, currentUser + " deleting friend request sent to " + sessionMB.getAnotherUser());
        // Get the Friend object that represents the friend request
        // userWhoSentFriendRequest is, in this case, the user who received the request
        // See query defined in IFriendRepository.java
        Friend theFriendRequest = friendService.getAFriendRequestBySenderAndReceiver(currentUser.getId(), anotherUserId, Boolean.FALSE);
        // Delete the friend request
        friendService.deleteFriendRequest(theFriendRequest);
        // Update collections for this user
        userSessionObject.setCurrentUserFriendRequestsSentUpdated(Boolean.TRUE);
        userSessionObject.resetFriendCollections();
        // Update collections for the other user
        jmsService.formatAndSendData(JmsMessageTypes.DELETE_FRIEND_REQUEST_SENT.toString(),
                JmsMessageCategories.FOR_USERS.toString(), anotherUserId, networkId, sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());        

        return "";
    }

    @Transactional
    public String deleteFriendRequestReceived() {
        // Get the id of the other user
        long anotherUserId = sessionMB.getAnotherUser().getId();
        long networkId = sessionMB.getAnotherUser().getNetworkId();
        // Get the logged in User
        User currentUser = userService.findById(userService.getLoggedInUserId());
        LOGGER.log(Level.INFO, currentUser + " deleting friend request received from " + sessionMB.getAnotherUser());
        // Get the Friend object that represents the friend request
        Friend theFriendRequest = friendService.getAFriendRequestBySenderAndReceiver(sessionMB.getAnotherUser().getId(), currentUser.getId(), Boolean.FALSE);
        // Delete the friend request
        friendService.deleteFriendRequest(theFriendRequest);
        // Reset collections with friends/friendRequests
        userSessionObject.setPendingUserFriendRequestsUpdated(Boolean.TRUE);
        userSessionObject.resetFriendCollections();
        // Update collections for the other user
        jmsService.formatAndSendData(JmsMessageTypes.DELETE_FRIEND_REQUEST_RECEIVED.toString(),
                JmsMessageCategories.FOR_USERS.toString(), anotherUserId, networkId, sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());        

        return "";
    }

    @Transactional
    public String deleteFriend() {
        // Get the id of the other user
        long anotherUserId = sessionMB.getAnotherUser().getId();
        long networkId = sessionMB.getAnotherUser().getNetworkId();
        // Get the logged in User
        User currentUser = userService.findById(userService.getLoggedInUserId());
        LOGGER.log(Level.INFO, currentUser + " deleting friend " + sessionMB.getAnotherUser());
        // Get the Friend object that represents the friend request - current user could be the sender or the receiver of the request
        Friend theFriendRequest = friendService.getAFriendRequestBySenderAndReceiver(sessionMB.getAnotherUser().getId(), currentUser.getId(), Boolean.TRUE);
        // If theFriendRequest is null try reversing the order of sender and receiver
        if (theFriendRequest == null) {
            theFriendRequest = friendService.getAFriendRequestBySenderAndReceiver(currentUser.getId(), sessionMB.getAnotherUser().getId(), Boolean.TRUE);
        }
        // Delete the friend request
        friendService.deleteFriendRequest(theFriendRequest);
        // Reset collections for this user
        userSessionObject.setUserFriendsAsUsersUpdated(Boolean.TRUE);
        userSessionObject.resetFriendCollections();
        // Reset collections for the other user
        jmsService.formatAndSendData(JmsMessageTypes.DELETE_FRIEND.toString(),
                JmsMessageCategories.FOR_USERS.toString(), anotherUserId, networkId, sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());        

        return "";
    }
}
