/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.sse;

import com.domrade.domain.Role;
import com.domrade.domain.User;
import com.domrade.jsf.common.UserSessionObject;
import com.domrade.service.interfaces.IMessageFormatterService;
import com.domrade.service.interfaces.IMessageService;
import com.domrade.service.interfaces.IUserService;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author David
 */
@Component
@Scope("session")
public class ManageServerSentEvents {

    public final static Logger LOGGER = Logger.getLogger(ManageServerSentEvents.class);

    @Autowired
    private UserSessionObject userSessionObject;

    @Autowired
    private IUserService userService;
    
    @Autowired
    private IMessageFormatterService messageFormatterService;
    
    @Autowired
    private IMessageService messageService;

    public void processSse(String messageType, String messageCategory, long userId, long networkId, long entityId) {
        switch (messageCategory) {
            case "FOR_USERS":
                processMessageForUsers(messageType, userId, entityId);
                break;
            case "FOR_ADMIN":
                processMessageForAdmins(messageType, networkId);
                break;
            case "FOR_NETWORK_MEMBERS":
                processMessageForNetworkMembers(messageType, networkId);
                break;
        }
    }

    public void processMessageForUsers(String messageType, long id, long entityId) {
        LOGGER.log(Level.DEBUG, "processMessageForUsers " + userSessionObject.getLoggedInUser());
        // If the message references the loggedInUser's id then 
        try {
            if (id == userSessionObject.getLoggedInUser().getId()) {
                LOGGER.log(Level.DEBUG, "Updating collections for user " + userSessionObject.getLoggedInUser());
                switch (messageType) {
                    case "SEND_FRIEND_REQUEST":
                        userSessionObject.setPendingUserFriendRequestsUpdated(Boolean.TRUE);
                        break;
                    case "DELETE_FRIEND_REQUEST_SENT":
                        userSessionObject.setPendingUserFriendRequestsUpdated(Boolean.TRUE);
                        break;
                    case "ACCEPT_FRIEND_REQUEST":
                        userSessionObject.setUserFriendsAsUsersUpdated(Boolean.TRUE);
                        userSessionObject.setCurrentUserFriendRequestsSentUpdated(Boolean.TRUE);
                    case "DELETE_FRIEND":
                        userSessionObject.setUserFriendsAsUsersUpdated(Boolean.TRUE);
                        break;
                    case "DELETE_FRIEND_REQUEST_RECEIVED":
                        userSessionObject.setCurrentUserFriendRequestsSentUpdated(Boolean.TRUE);
                        break;
                    case "SEND_MESSAGE":
                        userSessionObject.setCurrentUserMessagesUpdated(Boolean.TRUE);
                        if(userSessionObject.getMessagesForDataScrollerParentMessageId() == entityId) {
                            userSessionObject.setMessagesForDataScrollerUpdated(Boolean.TRUE);
                        }
                        /*long msgId = userSessionObject.getMessagesForDataScroller().get(userSessionObject.getMessagesForDataScroller().size() - 1).getMessageId();
                        LOGGER.log(Level.DEBUG, "messageId from messagesForDataScroller: " + msgId);
                        if(msgId == entityId) {
                        LOGGER.log(Level.DEBUG, "Updating messagesForDataScroller for user " + userSessionObject.getLoggedInUser());
                        Message aMessage = messageService.getMessageById(entityId);
                        userSessionObject.setMessagesForDataScroller(messageFormatterService.formatMessagesForView(
                        aMessage, aMessage.getMessageReplies()));
                        }*/
                        break;
                    case "ACCEPT_REQUEST_JOIN_NETWORK":
                        User tempUser = userService.get(id);
                        tempUser.setRole(Role.ROLE_MEMBER);
                        LOGGER.log(Level.DEBUG, "Updated role for member " + tempUser);
                        userService.save(tempUser);

                        break;
                    default:
                        LOGGER.log(Level.WARN, "Something has gone wrong in ManageServerSentEvents,.java");
                }
                //userSessionObject.resetFriendCollections();
            }
        } catch (NullPointerException npe) {
            LOGGER.log(Level.WARN, "No user logged in for this bean");
        }
    }

    public void processMessageForAdmins(String messageType, long id) {
        LOGGER.log(Level.DEBUG, "processMessageForAdmins " + userSessionObject.getLoggedInUser());
        // networkdId is sent when a user has requested to join a network
        // and it is necessary to update the networkAdmin's collections
        // Will throw an NPE if the user is not a member of a network
        long userSessionObjectCurrentNetworkId;

        try {
            userSessionObjectCurrentNetworkId = userSessionObject.getCurrentNetwork().getId();
        } catch (NullPointerException npe) {
            LOGGER.log(Level.WARN, "User is not a member of a network");
            return;
        }
        if (id == userSessionObjectCurrentNetworkId) {
            LOGGER.log(Level.DEBUG, "Updating collections for networkAdmin " + userSessionObject.getLoggedInUser());
            switch (messageType) {
                case "REQUEST_JOIN_NETWORK":
                    userSessionObject.setPendingJoinRequestsUpdated(Boolean.TRUE);
                    userSessionObject.resetNetworkAdminCollections();
                    break;
                case "CANCEL_REQUEST_JOIN_NETWORK":
                    userSessionObject.setPendingJoinRequestsUpdated(Boolean.TRUE);
                    userSessionObject.resetNetworkAdminCollections();
                    break;
                case "LEAVE_NETWORK":
                    userSessionObject.setNetworkMembersUpdated(Boolean.TRUE);
                    userSessionObject.resetNetworkAdminCollections();
                    break;
                default:
                    LOGGER.log(Level.WARN, "Something has gone wrong in MySseClient.java");
            }
        }
    }

    public void processMessageForNetworkMembers(String messageType, long id) {
        LOGGER.log(Level.DEBUG, "processMessageForNetworkMembers " + userSessionObject.getLoggedInUser());
        // Check that the logged in user is a member of a network
        long networkId;
        try {
            networkId = userSessionObject.getCurrentNetwork().getId();
        } catch (NullPointerException npe) {
            LOGGER.log(Level.WARN, "User is not a member of a network");
            return;
        }
        if (id == networkId) {
            LOGGER.log(Level.DEBUG, "Updating collections for networkMember " + userSessionObject.getLoggedInUser());
            switch (messageType) {
                case "NETWORK_WALL_UPDATED":
                    userSessionObject.setWallPostsUpdated(Boolean.TRUE);
                    userSessionObject.resetNetworkWallCollections();
                    break;
                case "EVENTS_UPDATED":
                    userSessionObject.setEventsUpdated(Boolean.TRUE);
                default:
                    LOGGER.log(Level.WARN, "Something has gone wrong in MySseClient.java");
            }
        }
    }
}
