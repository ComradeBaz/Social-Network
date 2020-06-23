package com.domrade.jsf.common;

import com.domrade.domain.Event;
import com.domrade.domain.Friend;
import com.domrade.domain.Message;
import com.domrade.domain.MessageReply;
import com.domrade.domain.Network;
import com.domrade.domain.User;
import com.domrade.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import com.domrade.service.interfaces.IEventService;
import com.domrade.service.interfaces.IFriendService;
import com.domrade.service.interfaces.IMessageFormatterService;
import com.domrade.service.interfaces.IMessageService;
import com.domrade.service.interfaces.INavigationService;
import java.util.Map;
import java.util.Set;
import javax.faces.event.ActionEvent;
import org.springframework.transaction.annotation.Transactional;

@Component("sessionMB")
@Scope("session")
public class SessionMB implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SessionMB.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private INavigationService navigationService;

    @Autowired
    private IFriendService friendService;

    @Autowired
    private UserSessionObject userSessionObject;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IMessageFormatterService messageFormatterService;

    @Autowired
    private IEventService eventService;

    // Session variables will be used when a user wants to interact with another user
    // eg to send a message, add a friend, reply to wall post
    // Or a session variable needed to load data for a given view
    private User anotherUser;
    private boolean currentUserAnotherUserFriends;
    private boolean friendRequestSent;
    private boolean currentUserSentFriendRequest;
    private boolean anotherUserSentFriendRequest;
    private long currentPostId;
    private User loggedInUser;
    private Message currentMessage;
    private Event currentEvent;
    private Network currentNetwork = new Network();
    private User user = new User();
    /*
    End session variables
     */

    public SessionMB() {
        // no arg constructor
        LOGGER.log(Level.INFO, "Constructing SessionMB");
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public long getCurrentPostId() {
        return currentPostId;
    }

    public void setCurrentPostId(long currentPostId) {
        this.currentPostId = currentPostId;
    }

    public Network getCurrentNetwork() {
        return currentNetwork;
    }

    public void setCurrentNetwork(Network network) {
        this.currentNetwork = network;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public User getAnotherUser() {
        return anotherUser;
    }

    public void setAnotherUser(User anotherUser) {
        this.anotherUser = anotherUser;
    }

    public Message getCurrentMessage() {
        return currentMessage;
    }

    public void setCurrentMessage(Message currentMessage) {
        this.currentMessage = currentMessage;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public boolean isCurrentUserAnotherUserFriends() {
        return currentUserAnotherUserFriends;
    }

    public void setCurrentUserAnotherUserFriends(boolean currentUserAnotherUserFriends) {
        this.currentUserAnotherUserFriends = currentUserAnotherUserFriends;
    }

    public boolean isFriendRequestSent() {
        return friendRequestSent;
    }

    public void setFriendRequestSent(boolean friendRequestSent) {
        this.friendRequestSent = friendRequestSent;
    }

    public boolean isCurrentUserSentFriendRequest() {
        return currentUserSentFriendRequest;
    }

    public void setCurrentUserSentFriendRequest(boolean currentUserSentFriendRequest) {
        this.currentUserSentFriendRequest = currentUserSentFriendRequest;
    }

    public boolean isAnotherUserSentFriendRequest() {
        return anotherUserSentFriendRequest;
    }

    public void setAnotherUserSentFriendRequest(boolean anotherUserSentFriendRequest) {
        this.anotherUserSentFriendRequest = anotherUserSentFriendRequest;
    }

    // Used to set a user with whom the logged in user wishes to interact
    public void setTheUserForBean(ActionEvent ae) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String userIdString = params.get("userId");
        String source = params.get("fromSource");
        // Set the user who sent the friend request
        // Value is used on the next call to accept or delete the friend request
        anotherUser = userService.findById(Long.parseLong(userIdString));
        setFriendBooleans();

        //return navigationService.getDestination(source);
    }

    // Get the message a user has selected to view from messages.xhtml
    @Transactional
    public String setMessageToView() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String messageIdString = params.get("messageId");
        setTheMessage(Long.parseLong(messageIdString));

        return "";
    }

    public void setTheMessage(long messageId) {
        currentMessage = messageService.getMessageById(messageId);
        userSessionObject.setMessagesForDataScrollerParentMessageId(currentMessage.getId());
        setCurrentMessage(currentMessage);
        Set<MessageReply> messageReplies = currentMessage.getMessageReplies();
        userSessionObject.setMessagesForDataScroller(messageFormatterService.formatMessagesForView(currentMessage, messageReplies));
        // Mark the message as read if the current user was not the sender
        if (!(currentMessage.getLastUpdatedBy().equals(getLoggedInUser()))) {
            currentMessage.setHasBeenRead(Boolean.TRUE);
            // Update the styling so the user's inbox displayed the message as read
            messageFormatterService.setStylingForCurrentUserMessages(userSessionObject.getCurrentUserMessages());
            userSessionObject.setCurrentUserMessagesUpdated(Boolean.TRUE);
            userSessionObject.resetMessageCollections();
        }
    }

    // Display the the event detail and set this.currentEvent so the correct 
    // event is referenced when a user chooses to add themselves to the list
    // of attendees
    public String goToEventDetail() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String eventIdString = params.get("eventId");
        currentEvent = eventService.get(Long.parseLong(eventIdString));

        return navigationService.getEventDetail();
    }

    // When the user clicks reply a modal is popped so they can add a response
    // This method is called to set the posrtId for the post to which the user is replying
    public String initReply() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String postId = params.get("postId");
        String userWhoPostedId = params.get("userWhoPosted");
        setCurrentPostId(Long.parseLong(postId));
        setAnotherUser(userService.findById(Long.parseLong(userWhoPostedId)));

        return "";
    }

    // Method is called to determine what button (addFriend or deleteFriend) should be displayed when the loggedInUser 
    // navigates to the profile of another user
    public void setFriendBooleans() {

        long loggedInUserId = loggedInUser.getId();
        long anotherUserId = anotherUser.getId();
        setFriendRequestSent(Boolean.FALSE);
        setCurrentUserAnotherUserFriends(Boolean.FALSE);

        Friend friendObject = friendService.getAFriendRequestBySenderAndReceiver(loggedInUserId, anotherUserId);
        if (friendObject == null) {
            setFriendRequestSent(Boolean.FALSE);
            return;
        }
        if (friendObject.isEnabled()) {
            setCurrentUserAnotherUserFriends(Boolean.TRUE);
        } else {
            setFriendRequestSent(Boolean.TRUE);
            if (friendObject.getRequesterId() == loggedInUserId) {
                setCurrentUserSentFriendRequest(Boolean.TRUE);
            } else {
                setAnotherUserSentFriendRequest(Boolean.TRUE);
            }
        }
    }
}
