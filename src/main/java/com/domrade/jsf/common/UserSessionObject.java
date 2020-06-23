/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.jsf.common;

import com.domrade.cache.entities.CachedWallPostObject;
import com.domrade.cache.service.ICachingService;
import com.domrade.domain.Event;
import com.domrade.domain.Message;
import com.domrade.domain.Network;
import com.domrade.domain.User;
import com.domrade.domain.WallPost;
import com.domrade.helper.classes.BasicMessage;
import com.domrade.service.interfaces.IEventService;
import com.domrade.service.interfaces.IFriendService;
import com.domrade.service.interfaces.IMessageFormatterService;
import com.domrade.service.interfaces.IMessageService;
import com.domrade.service.interfaces.INetworkService;
import com.domrade.service.interfaces.IUserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author David
 *
 * Object to store collections relevant to the logged in user These collections
 * are updated as necessary. The application gets collection data from this
 * class and not directly from the dB
 */
//@Configuration
@Component("userSessionObject")
@Scope("session")
public class UserSessionObject implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(UserSessionObject.class);

    private User loggedInUser;
    private Network currentNetwork;

    @Autowired
    private IUserService userService;

    @Autowired
    private IFriendService friendService;

    @Autowired
    private INetworkService networkService;

    @Autowired
    private ICachingService cachingService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IMessageFormatterService messageFormatterService;

    @Autowired
    private IEventService eventService;

    /*
    Friends collections
     */
    private List<User> pendingUserFriendRequests;
    private List<User> userFriendsAsUsers;
    private List<User> currentUserFriendRequestsSent;
    private boolean pendingUserFriendRequestsUpdated;
    private boolean userFriendsAsUsersUpdated;
    private boolean currentUserFriendRequestsSentUpdated;

    /*
    Network Admin collections
     */
    private List<User> pendingJoinRequests;
    private List<User> networkMembers;
    private boolean pendingJoinRequestsUpdated;
    private boolean networkMembersUpdated;

    /*
    Network Wall collections
     */
    private List<WallPost> wallPosts;
    private boolean wallPostsUpdated;
    //private List<UserWallPost> userWallPosts;
    //private boolean userWallPostsUpdated;
    private List<CachedWallPostObject> cachedWallPostObjects;

    /*
    Messages
     */
    private List<Message> currentUserMessages;
    private List<BasicMessage> messagesForDataScroller;
    private boolean currentUserMessagesUpdated;
    private boolean messagesForDataScrollerUpdated;
    private long messagesForDataScrollerParentMessageId;
    /*
    Events
     */
    private List<Event> events;
    private boolean eventsUpdated;

    public UserSessionObject() {
        /*
        Friends
         */
        LOGGER.log(Level.INFO, "Creating UserSessionObject...");
        pendingUserFriendRequests = new ArrayList<>();
        userFriendsAsUsers = new ArrayList<>();
        currentUserFriendRequestsSent = new ArrayList<>();
        currentUserFriendRequestsSentUpdated = Boolean.TRUE;
        pendingUserFriendRequestsUpdated = Boolean.TRUE;
        userFriendsAsUsersUpdated = Boolean.TRUE;

        /*
        Network Admin stuff
         */
        pendingJoinRequests = new ArrayList<>();
        networkMembers = new ArrayList<>();
        pendingJoinRequestsUpdated = Boolean.TRUE;
        networkMembersUpdated = Boolean.TRUE;

        /*
        Network wall stuff
         */
        wallPostsUpdated = Boolean.TRUE;
        wallPosts = new ArrayList<>();
        //userWallPostsUpdated = Boolean.TRUE;

        /*
        Messages
         */
        currentUserMessages = new ArrayList<>();
        currentUserMessagesUpdated = Boolean.TRUE;

        /*
        Events
         */
        events = new ArrayList<>();
        eventsUpdated = Boolean.TRUE;
    }

    public List<User> getPendingUserFriendRequests() {
        return pendingUserFriendRequests;
    }

    public void setPendingUserFriendRequests(List<User> pendingUserFriendRequests) {
        this.pendingUserFriendRequests = pendingUserFriendRequests;
    }

    public List<User> getUserFriendsAsUsers() {
        return userFriendsAsUsers;
    }

    public void setUserFriendsAsUsers(List<User> userFriendsAsUsers) {
        this.userFriendsAsUsers = userFriendsAsUsers;
    }

    public List<User> getCurrentUserFriendRequestsSent() {
        return currentUserFriendRequestsSent;
    }

    public void setCurrentUserFriendRequestsSent(List<User> currentUserFriendRequestsSent) {
        this.currentUserFriendRequestsSent = currentUserFriendRequestsSent;
    }

    public boolean isPendingUserFriendRequestsUpdated() {
        return pendingUserFriendRequestsUpdated;
    }

    public void setPendingUserFriendRequestsUpdated(boolean pendingUserFriendRequestsUpdated) {
        LOGGER.log(Level.DEBUG, "Updating pendingUserFriendRequests boolean to " + pendingUserFriendRequestsUpdated + " for user " + getLoggedInUser());
        this.pendingUserFriendRequestsUpdated = pendingUserFriendRequestsUpdated;
    }

    public boolean isUserFriendsAsUsersUpdated() {
        return userFriendsAsUsersUpdated;
    }

    public void setUserFriendsAsUsersUpdated(boolean userFriendsAsUsersUpdated) {
        LOGGER.log(Level.DEBUG, "Updating userFriendsAsUsersUpdated boolean to " + userFriendsAsUsersUpdated + " for user " + getLoggedInUser());
        this.userFriendsAsUsersUpdated = userFriendsAsUsersUpdated;
    }

    public boolean isCurrentUserFriendRequestsSentUpdated() {
        return currentUserFriendRequestsSentUpdated;
    }

    public void setCurrentUserFriendRequestsSentUpdated(boolean currentUserFriendRequestsSentUpdated) {
        LOGGER.log(Level.DEBUG, "Updating currentUserFriendRequestsSentUpdated boolean to " + currentUserFriendRequestsSentUpdated + " for user " + getLoggedInUser());
        this.currentUserFriendRequestsSentUpdated = currentUserFriendRequestsSentUpdated;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public List<User> getPendingJoinRequests() {
        return pendingJoinRequests;
    }

    public void setPendingJoinRequests(List<User> pendingJoinRequests) {
        this.pendingJoinRequests = pendingJoinRequests;
    }

    public List<User> getNetworkMembers() {
        return networkMembers;
    }

    public void setNetworkMembers(List<User> networkMembers) {
        this.networkMembers = networkMembers;
    }

    public boolean isPendingJoinRequestsUpdated() {
        return pendingJoinRequestsUpdated;
    }

    public void setPendingJoinRequestsUpdated(boolean pendingJoinRequestsUpdated) {
        this.pendingJoinRequestsUpdated = pendingJoinRequestsUpdated;
    }

    public boolean isNetworkMembersUpdated() {
        return networkMembersUpdated;
    }

    public void setNetworkMembersUpdated(boolean networkMembersUpdated) {
        this.networkMembersUpdated = networkMembersUpdated;
    }

    public Network getCurrentNetwork() {
        return currentNetwork;
    }

    public void setCurrentNetwork(Network currentNetwork) {
        this.currentNetwork = currentNetwork;
    }

    public List<WallPost> getWallPosts() {
        return wallPosts;
    }

    public void setWallPosts(List<WallPost> wallPosts) {
        this.wallPosts = wallPosts;
    }

    public boolean isWallPostsUpdated() {
        return wallPostsUpdated;
    }

    public void setWallPostsUpdated(boolean wallPostsUpdated) {
        this.wallPostsUpdated = wallPostsUpdated;
    }

    /*
    public List<UserWallPost> getUserWallPosts() {
        return userWallPosts;
    }

    public void setUserWallPosts(List<UserWallPost> userWallPosts) {
        this.userWallPosts = userWallPosts;
    }

    public boolean isUserWallPostsUpdated() {
        return userWallPostsUpdated;
    }

    public void setUserWallPostsUpdated(boolean userWallPostsUpdated) {
        this.userWallPostsUpdated = userWallPostsUpdated;
    }
     */
    public List<CachedWallPostObject> getCachedWallPostObjects() {
        return cachedWallPostObjects;
    }

    public void setCachedWallPostObjects(List<CachedWallPostObject> cachedWallPostObjects) {
        this.cachedWallPostObjects = cachedWallPostObjects;
    }

    public List<Message> getCurrentUserMessages() {
        return currentUserMessages;
    }

    public void setCurrentUserMessages(List<Message> currentUserMessages) {
        this.currentUserMessages = currentUserMessages;
    }

    public boolean isCurrentUserMessagesUpdated() {
        return currentUserMessagesUpdated;
    }

    public void setCurrentUserMessagesUpdated(boolean currentUserMessagesUpdated) {
        this.currentUserMessagesUpdated = currentUserMessagesUpdated;
    }

    public List<BasicMessage> getMessagesForDataScroller() {
        return messagesForDataScroller;
    }

    public void setMessagesForDataScroller(List<BasicMessage> messagesForDataScroller) {
        this.messagesForDataScroller = messagesForDataScroller;
    }

    public boolean isMessagesForDataScrollerUpdated() {
        return messagesForDataScrollerUpdated;
    }

    public void setMessagesForDataScrollerUpdated(boolean messagesForDataScrollerUpdated) {
        this.messagesForDataScrollerUpdated = messagesForDataScrollerUpdated;
    }

    public long getMessagesForDataScrollerParentMessageId() {
        return messagesForDataScrollerParentMessageId;
    }

    public void setMessagesForDataScrollerParentMessageId(long messagesForDataScrollerParentMessageId) {
        this.messagesForDataScrollerParentMessageId = messagesForDataScrollerParentMessageId;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public boolean isEventsUpdated() {
        return eventsUpdated;
    }

    public void setEventsUpdated(boolean eventsUpdated) {
        this.eventsUpdated = eventsUpdated;
    }

    public void resetFriendCollections() {

        // Make dB calls only if collections have been updated
        if (isPendingUserFriendRequestsUpdated()) {
            LOGGER.log(Level.INFO, loggedInUser + " Getting user friend requests from the database");
            setPendingUserFriendRequests(userService.getUserFromFriendObject(
                    friendService.getPendingFriendRequestsReceived(loggedInUser.getId(), Boolean.FALSE), loggedInUser));
            setPendingUserFriendRequestsUpdated(Boolean.FALSE);
        } else {
            LOGGER.log(Level.INFO, loggedInUser + " Not getting user friend requests from the database");
        }
        if (isUserFriendsAsUsersUpdated()) {
            LOGGER.log(Level.INFO, loggedInUser + " Getting user friends from the database");
            setUserFriendsAsUsers(userService.getUserFromFriendObject(friendService.getApprovedFriends(
                    Boolean.TRUE, loggedInUser.getId(), loggedInUser.getId()), loggedInUser));
            //List<Friend> tempList = loggedInUser.getTheFriends();
            //setUserFriendsAsUsers(getUserFromFriendObject(tempList));
            setUserFriendsAsUsersUpdated(Boolean.FALSE);
        } else {
            LOGGER.log(Level.INFO, loggedInUser + " Not getting user friends from the database");
        }
        if (isCurrentUserFriendRequestsSentUpdated()) {
            LOGGER.log(Level.INFO, loggedInUser + " Getting user friend request sent from the database");
            setCurrentUserFriendRequestsSent(userService.getUserFromFriendObject(friendService.getPendingFriendRequestsSent(
                    loggedInUser.getId(), Boolean.FALSE), loggedInUser));
            setCurrentUserFriendRequestsSentUpdated(Boolean.FALSE);
        } else {
            LOGGER.log(Level.INFO, loggedInUser + " Not getting user friend request sent from the database");
        }
    }

    public void resetNetworkAdminCollections() {

        if (isPendingJoinRequestsUpdated()) {
            LOGGER.log(Level.DEBUG, "Getting network pendingJoinRequests from database");
            setPendingJoinRequests(userService.getPendingJoinRequests(currentNetwork.getId()));
            setPendingJoinRequestsUpdated(Boolean.FALSE);
        } else {
            LOGGER.log(Level.DEBUG, "Not getting pendingJoinRequests from the database");
        }
        if (isNetworkMembersUpdated()) {
            LOGGER.log(Level.DEBUG, "Network members updated, getting network members from database");
            setNetworkMembers(networkService.getNetworkMembersByNetworkId(currentNetwork.getId(), loggedInUser.getId()));
            setNetworkMembersUpdated(Boolean.FALSE);
        } else {
            LOGGER.log(Level.DEBUG, "Not getting networkMembers from database");
        }
    }

    @Transactional
    public void resetNetworkWallCollections() {

        if (isWallPostsUpdated()) {
            LOGGER.log(Level.DEBUG, "Getting network wall posts from database for user " + loggedInUser);
            setWallPosts(networkService.getWallPostsById(loggedInUser.getNetworkId()));
            setCachedWallPostObjects(cachingService.getCachedWallPostObjects(getWallPosts()));
            setWallPostsUpdated(Boolean.FALSE);
        } else {
            LOGGER.log(Level.DEBUG, "Not getting network wall posts from database");
        }
        /*
        if(isUserWallPostsUpdated()) {
            LOGGER.log(Level.DEBUG, "Getting network user wall posts from database");
            setUserWallPosts(networkService.getUserWallPostsById(loggedInUser.getNetworkId()));
            setUserWallPostsUpdated(Boolean.FALSE);
        } else {
            LOGGER.log(Level.DEBUG, "Not getting network user wall posts from database");
        }  
         */
    }

    @Transactional
    public void resetMessageCollections() {

        if (isCurrentUserMessagesUpdated()) {
            LOGGER.log(Level.DEBUG, "Getting current user messages from the database for user " + loggedInUser);
            setCurrentUserMessages(messageFormatterService.changeSenderReceiverForCurrentUserMessages(
                    messageService.getCurrentUserMessages(loggedInUser), loggedInUser.getId()));
            setCurrentUserMessagesUpdated(Boolean.FALSE);
            resetMessagesForDataScroller();
        } else {
            LOGGER.log(Level.DEBUG, "Not getting current user messages from the database for user " + loggedInUser);
        }
    }

    @Transactional
    public void resetMessagesForDataScroller() {
        if (isMessagesForDataScrollerUpdated()) {
            Message aMessage = messageService.getMessageById(messagesForDataScrollerParentMessageId);
            setMessagesForDataScroller(messageFormatterService.formatMessagesForView(aMessage,
                    messageService.getMessageById(messagesForDataScrollerParentMessageId).getMessageReplies()));
            // This method called if the loggedInUser is viewing a message that is updated by another user - mark the message as read
            aMessage.setHasBeenRead(Boolean.TRUE);
            aMessage.setStyling(messageService.getReadStyling());
            messageService.saveMessage(aMessage);
            setMessagesForDataScrollerUpdated(Boolean.FALSE);
        }
    }

    @Transactional
    public void resetEventCollections() {

        if (isEventsUpdated()) {
            LOGGER.log(Level.DEBUG, "Getting events from the database for user " + loggedInUser);
            setEvents(eventService.getEventsByNetworkId(loggedInUser.getNetworkId()));
            setEventsUpdated(Boolean.FALSE);
        } else {
            LOGGER.log(Level.DEBUG, "Not getting events from the database for user " + loggedInUser);
        }
    }
}
