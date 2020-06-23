/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

import com.domrade.domain.Network;
import com.domrade.domain.User;
import com.domrade.domain.UserWallPost;
import com.domrade.domain.UserWallPostReply;
import com.domrade.domain.WallPost;
import com.domrade.domain.WallPostReply;
import java.util.List;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ComradeBaz
 */
public interface INetworkService {
    
    public Network findOneByName(@Param("name") String name);
    
    public String getNetworkNameById(long id);
    
    public String getNetworkJoinRequestByLoggedInUser(User aUser);
    
    public String logUserOutOfNetwork();
    
    public List<Network> findAllNetworks();
    
    public Network save(Network network);
    
    public void requestJoinNetwork(String networkName, Long userId);
    
    public long getNetworkIdByEmailAddress(String emailAddress);
    
    public Network findOneById(@Param("networkId") long networkId);
    
    public void save(WallPost wallPost);
    
    public void save(WallPostReply wallPostReply);
    
    public void save(UserWallPost wallPost);
    
    public void save(UserWallPostReply userWallPostReply);
    
    public List<WallPost> getWallPostsById(long id);
    
    public void leaveNetwork(String networkName, Long userId);  
    
    public List<WallPost> getWallPostById(long id);
    
    public List<UserWallPost> getUserWallPostById(long id);
    
    public List<UserWallPost> getUserWallPostsById(long tempId);
    
    public void cancelRequestJoinNetwork(long userId);
    
    public void acceptRequestJoinNetwork(String userIdString);
    
    public List<User> getNetworkMembersByNetworkId(long networkId, long userId);
    
    public List<WallPostReply> getWallPostRepliesByWallPost(WallPost wallPost);
    
    public Network getNetworkByName(String networkName);
    
    public void setMembersHaveNotification(long networkId, long userId);
    
    public User getNetworkAdminByNetworkId(long networkId);
}
