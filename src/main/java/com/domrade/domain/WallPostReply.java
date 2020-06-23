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
 * @author ComradeBaz
 */
@Entity
@Table(name = "wall_post_reply",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id"})
        })
public class WallPostReply extends BaseEntity {

    @Column(name = "author_first_name", length = 50, nullable = false)
    private String authorFirstName;

    @Column(name = "author_last_name", length = 50, nullable = false)
    private String authorLastName;

    @Column(name = "post_text", length = 240, nullable = false)
    private String postText;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wall_posts_id")
    private WallPost wallPost;
    
    @Column(name = "user_id")
    private long userId;
    
    @Column(name = "time_stamp")
    private String timeStamp;

    public WallPostReply() {
        // no arg constructor
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public WallPost getWallPost() {
        return wallPost;
    }

    public void setWallPost(WallPost wallPost) {
        this.wallPost = wallPost;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
