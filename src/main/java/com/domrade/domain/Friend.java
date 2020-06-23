/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author ComradeBaz
 */
@Entity
@Table(name = "friend",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id"})
        })
public class Friend extends BaseEntity {

    // This is the user that sent the friend request
    /*    @ManyToOne
    @JoinColumn(name = "users_id")
    @NotNull
    private User requestSentBy;*/
    @Column(name = "requester_id")
    private long requesterId;
    
    // userId is the user that received the request
    @Column(name = "user_id")
    private Long userId;

    @ManyToMany( fetch = FetchType.EAGER,
            mappedBy = "theFriends")
    private Set<User> theUsers = new HashSet<>();

    @Column(nullable = false, 
            columnDefinition = "TINYINT(1) default 0")
    private boolean enabled;

    public Friend() {
        // no arg constructor
    }
    
    // New friend object is created when a friend request is sent
    // This constructor is used to create the object
    public Friend(long userWhoSentRequest, long userIdWhoReceivedRequest, boolean enabled) {
        this.requesterId = userWhoSentRequest;
        this.userId = userIdWhoReceivedRequest;
        this.enabled = enabled;
    }
    /*public User getRequestSentBy() {
    return requestSentBy;
    }
    
    public void setRequestSentBy(User requestSentBy) {
    this.requestSentBy = requestSentBy;
    }*/

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<User> getTheUsers() {
        return theUsers;
    }

    public void setTheUsers(Set<User> theUsers) {
        this.theUsers = theUsers;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.userId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Friend other = (Friend) obj;
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "Requested by userId " + requesterId + ", sent to userId " + userId;
    }
}
