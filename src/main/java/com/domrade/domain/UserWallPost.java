/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author David
 */
@Entity
@Table(name = "user_wall_post",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id"})
        })
public class UserWallPost extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userWhoWrotePost;
    
    @Column(name = "post_text", length = 240, nullable = false)
    private String postText;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    private Network network;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "userWallPost",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserWallPostReply> userWallPostReplies = new ArrayList<>();
    
    @Column(name = "time_stamp")
    private String timeStamp;

    public UserWallPost() {
        // No arg constructor
    }

    public User getUserWhoWrotePost() {
        return userWhoWrotePost;
    }

    public void setUserWhoWrotePost(User userWhoWrotePost) {
        this.userWhoWrotePost = userWhoWrotePost;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public List<UserWallPostReply> getUserWallPostReplies() {
        return userWallPostReplies;
    }

    public void setUserWallPostReplies(List<UserWallPostReply> userWallPostReplies) {
        this.userWallPostReplies = userWallPostReplies;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WallPost)) {
            return false;
        }
        if (getId() != 0L) {
            return getId() == (((WallPost) o).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.userWhoWrotePost.getFirstName());
        hash = 71 * hash + Objects.hashCode(this.userWhoWrotePost.getLastName());
        hash = 71 * hash + Objects.hashCode(this.postText);
        hash = 71 * hash + Objects.hashCode(this.network);
        return hash;
    }

    @Override
    public String toString() {
        return "UserWallPost{" + "userWhoWrotePost=" + userWhoWrotePost.getFirstName() + userWhoWrotePost.getLastName() 
                + ", postText=" + postText + ", timeStamp=" + timeStamp + '}';
    }

    
}
