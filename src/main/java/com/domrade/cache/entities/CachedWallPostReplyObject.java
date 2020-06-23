/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.cache.entities;

import com.domrade.domain.WallPostReply;

/**
 *
 * @author David
 */
public class CachedWallPostReplyObject {
    
    private WallPostReply wallPostReply;
    private String profilePicture;
    
    public CachedWallPostReplyObject() {
        // no arg constructor
    }

    public WallPostReply getWallPostReply() {
        return wallPostReply;
    }

    public void setWallPostReply(WallPostReply wallPostReply) {
        this.wallPostReply = wallPostReply;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
}
