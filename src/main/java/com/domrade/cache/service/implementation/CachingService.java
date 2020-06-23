/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.cache.service.implementation;

import com.domrade.cache.entities.CachedWallPostObject;
import com.domrade.cache.entities.CachedWallPostReplyObject;
import com.domrade.cache.service.ICachingService;
import com.domrade.domain.WallPost;
import com.domrade.domain.WallPostReply;
import com.domrade.service.interfaces.INetworkService;
import com.domrade.service.interfaces.IUserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 * 
 * WallPosts are stored along with the profile picture of the user to avoid
 * making db calls when loading data that hasn't been updated
 */
@Service
public class CachingService implements ICachingService {
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private INetworkService networkService;

    @Override
    public List<CachedWallPostObject> getCachedWallPostObjects(List<WallPost> wallPosts) {
        
        CachedWallPostObject cachedObject = new CachedWallPostObject();
        List<CachedWallPostObject> listToReturn = new ArrayList<>();
        
        for(WallPost wp: wallPosts) {
            cachedObject.setWallPost(wp);
            // Check the db for new wallPostReplies
            cachedObject.setWallPostReplies(getCachedWallPostReplyObjects(networkService.getWallPostRepliesByWallPost(wp)));
            cachedObject.setProfilePicture(userService.findById(wp.getAuthorId()).getProfilePicture());
            listToReturn.add(cachedObject);
            cachedObject = new CachedWallPostObject();
        }
        return listToReturn;
    }

    @Override
    public List<CachedWallPostReplyObject> getCachedWallPostReplyObjects(List<WallPostReply> wallPostReplies) {
        
        CachedWallPostReplyObject cachedObject = new CachedWallPostReplyObject();
        List<CachedWallPostReplyObject> listToReturn = new ArrayList<>();
        
        for(WallPostReply wpr: wallPostReplies) {
            cachedObject.setWallPostReply(wpr);
            cachedObject.setProfilePicture(userService.findById(wpr.getUserId()).getProfilePicture());
            listToReturn.add(cachedObject);
            cachedObject = new CachedWallPostReplyObject();
        }
        return listToReturn;
    }
    
}
