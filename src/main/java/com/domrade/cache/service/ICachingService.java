/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.cache.service;

import com.domrade.cache.entities.CachedWallPostObject;
import com.domrade.cache.entities.CachedWallPostReplyObject;
import com.domrade.domain.WallPost;
import com.domrade.domain.WallPostReply;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public interface ICachingService {
    
    public List<CachedWallPostObject> getCachedWallPostObjects(List<WallPost> wallPosts);
    
    public List<CachedWallPostReplyObject> getCachedWallPostReplyObjects(List<WallPostReply> wallPostReplies);
    
}
