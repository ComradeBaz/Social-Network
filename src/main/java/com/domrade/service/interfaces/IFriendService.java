/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

import com.domrade.domain.Friend;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public interface IFriendService {
    
    List<Friend> getPendingFriendRequestsReceived(long userId, boolean enabled);
    
    public List<Friend> getPendingFriendRequestsSent(long userId, boolean enabled);
    
    public List<Friend> getApprovedFriends(boolean enabled, long userId, long requestSentById);
    
    public Friend getAFriendRequestBySenderAndReceiver(long requestSentById, long userId, boolean enabled);
    
    public void saveFriend(Friend f);
    
    public void deleteFriendRequest(Friend f);
    
    public List<Friend> getTest(long userId);
    
    public Friend getAFriendRequestBySenderAndReceiver(long requestSentById, long userId);
    
}
