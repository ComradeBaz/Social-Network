/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.cache.entities;

import com.domrade.domain.WallPost;
import java.util.List;

/**
 *
 * @author David
 */
public class CachedWallPostObject {
    
    private WallPost wallPost;
    private List<CachedWallPostReplyObject> wallPostReplies;
    private String profilePicture;
    
    public CachedWallPostObject() {
        // no arg constructor
    }
    
    public CachedWallPostObject(WallPost wallPost, String profilePicture) {
        this.wallPost = wallPost;
        this.profilePicture = profilePicture;
    }

    public WallPost getWallPost() {
        return wallPost;
    }

    public void setWallPost(WallPost wallPost) {
        this.wallPost = wallPost;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<CachedWallPostReplyObject> getWallPostReplies() {
        return wallPostReplies;
    }

    public void setWallPostReplies(List<CachedWallPostReplyObject> wallPostReplies) {
        this.wallPostReplies = wallPostReplies;
    }
    
}
