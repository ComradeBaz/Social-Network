package com.domrade.domain;

import static com.domrade.domain.Network.LOGGER;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;
import org.apache.log4j.Level;

@Entity
@Table(name = "users",
        //catalog = "testapp",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id"}),
            @UniqueConstraint(columnNames = {"email"}, name = "uidx_user_email"),
            @UniqueConstraint(columnNames = {"verification_key"}, name = "uidx_verification_key")
        })
public class User extends BaseEntity {

    @Size(min = 1, max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Size(min = 1, max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "{user.invalid.email.format}")
    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 80)
    private String password;

    @Column(name = "is_enabled", columnDefinition = "INT(1) default 0")
    private boolean enabled;

    @Column(name = "is_locked", columnDefinition = "INT(1) default 0")
    private boolean locked;

    @Column(name = "is_subscribed", columnDefinition = "INT(1) default 0")
    private boolean subscribed;

    @Column(name = "verification_key", length = 64)
    private String verificationKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 30)
    private Role role;

    @Column(name = "network_id")
    private Long networkId;

    @Column(name = "network_join_request")
    private Long networkJoinRequest;

    @Column(name = "profile_picture")
    private String profilePicture;

    // A given event can only have one owner(com.domrade.domain.User), but an owner can own multiple events
    @OneToMany(
            mappedBy = "owner", // owner is the property name in com.domrade.domain.Event
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Event> events = new ArrayList<>();

    // Any given user can join multiple events and events can have multiple users
    @ManyToMany(
            mappedBy = "theUsers")
    private Set<Event> theEvents = new HashSet<>();
  
    // Creates a table that lists the current user's id with a friend_id that is 
    // a reference to a friend object from the 'friend' table
    // Changed fetch type to lazy from eager 08/01/2020
    @ManyToMany(cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_friends",
            joinColumns = {
                @JoinColumn(name = "users_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "friend_id")}
    )            
    Set<Friend> theFriends = new HashSet<>();
    
    @OneToMany(
            mappedBy = "receiver", // receiver is the property name in com.domrade.domain.Message
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
            //orphanRemoval = true
    )
    private Set<Message> receivedMessages = new HashSet<>();
    
    @OneToMany(
            mappedBy = "sender", // sender is the property name in com.domrade.domain.Message
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
            //orphanRemoval = true
    )
    private Set<Message> sentMessages = new HashSet<>();
    
    @OneToMany(
            mappedBy = "receiver", // receiver is the property name in com.domrade.domain.Message
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
            //orphanRemoval = true
    )
    private Set<MessageReply> receivedMessageReplies = new HashSet<>();
    
    @OneToMany(
            mappedBy = "sender", // sender is the property name in com.domrade.domain.Message
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
            //orphanRemoval = true
    )
    private Set<MessageReply> sentMessageReplies = new HashSet<>();
    
    @OneToMany(
    mappedBy = "lastUpdatedBy",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
            //orphanRemoval = true
    )
    private Set<Message> messageUpdatedBy = new HashSet<>();
    
    @Column (name = "has_notification", columnDefinition = "INT(1) default 0")
    private boolean hasNotification;
/*    
    @OneToMany(
    mappedBy = "userWhoWrotePost",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<UserWallPost> userWallPosts = new ArrayList<>();
    
    @OneToMany(
    mappedBy = "userWhoWroteWallPostReply",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<UserWallPostReply> userWallPostReplies = new ArrayList<>();
*/
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getVerificationKey() {
        return verificationKey;
    }

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    public Long getNetworkJoinRequest() {
        return networkJoinRequest;
    }

    public void setNetworkJoinRequest(Long networkJoinRequest) {
        this.networkJoinRequest = networkJoinRequest;
    }

    public void setVerificationKey(String verificationKey) {
        this.verificationKey = verificationKey;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        LOGGER.log(Level.INFO, "Adding event to user");
        events.add(event);
        event.setOwner(this);
    }

    public void removeEvent(Event event) {
        LOGGER.log(Level.INFO, "Removing event from user");
        events.remove(event);
        event.setOwner(null);
    }

    public Set<Event> getTheEvents() {
        return theEvents;
    }

    public void setTheEvents(Set<Event> theEvents) {
        this.theEvents = theEvents;
    }

    public Set<Friend> getTheFriends() {
        return theFriends;
    }

    public void setTheFriends(Set<Friend> theFriends) {
        this.theFriends = theFriends;
    }

    public Set<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(Set<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public Set<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(Set<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public Set<MessageReply> getReceivedMessageReplies() {
        return receivedMessageReplies;
    }

    public void setReceivedMessageReplies(Set<MessageReply> receivedMessageReplies) {
        this.receivedMessageReplies = receivedMessageReplies;
    }

    public Set<MessageReply> getSentMessageReplies() {
        return sentMessageReplies;
    }

    public void setSentMessageReplies(Set<MessageReply> sentMessageReplies) {
        this.sentMessageReplies = sentMessageReplies;
    }

    public Set<Message> getMessageUpdatedBy() {
        return messageUpdatedBy;
    }

    public void setMessageUpdatedBy(Set<Message> messageUpdatedBy) {
        this.messageUpdatedBy = messageUpdatedBy;
    }
/*
    public List<UserWallPost> getUserWallPosts() {
        return userWallPosts;
    }

    public void setUserWallPosts(List<UserWallPost> userWallPosts) {
        this.userWallPosts = userWallPosts;
    }

    public List<UserWallPostReply> getUserWallPostReplies() {
        return userWallPostReplies;
    }

    public void setUserWallPostReplies(List<UserWallPostReply> userWallPostReplies) {
        this.userWallPostReplies = userWallPostReplies;
    }
*/   

    public boolean isHasNotification() {
    return hasNotification;
    }
    
    public void setHasNotification(boolean hasNotification) {
    this.hasNotification = hasNotification;
    }
    
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hash(firstName, lastName, email, password, enabled, locked, subscribed, verificationKey, role);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.firstName, other.firstName)
                && Objects.equals(this.lastName, other.lastName)
                && Objects.equals(this.email, other.email)
                && Objects.equals(this.password, other.password)
                && Objects.equals(this.enabled, other.enabled)
                && Objects.equals(this.locked, other.locked)
                && Objects.equals(this.subscribed, other.subscribed)
                && Objects.equals(this.verificationKey, other.verificationKey)
                && Objects.equals(this.role, other.role);
    }

    @Override
    public String toString() {
        return "User{"
                + "firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + "} ";
    }
}