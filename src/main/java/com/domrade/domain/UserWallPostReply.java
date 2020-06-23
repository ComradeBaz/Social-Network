/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author David
 */
@Entity
@Table(name = "user_wall_post_reply",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id"})
        })
public class UserWallPostReply extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User userWhoWroteWallPostReply;
    
    @Column(name = "post_text", length = 240, nullable = false)
    private String postText;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_wall_post_id")
    private UserWallPost userWallPost;
    
    //@Column(name = "user_id")
    //private long userId;
    
    @Column(name = "time_stamp")
    private String timeStamp;

    public UserWallPostReply() {
        // no arg constructor
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public UserWallPost getUserWallPost() {
        return userWallPost;
    }

    public void setUserWallPost(UserWallPost userWallPost) {
        this.userWallPost = userWallPost;
    }
/*
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
*/    
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public User getUserWhoWroteWallPostReply() {
        return userWhoWroteWallPostReply;
    }

    public void setUserWhoWroteWallPostReply(User userWhoWroteWallPostReply) {
        this.userWhoWroteWallPostReply = userWhoWroteWallPostReply;
    }
}
