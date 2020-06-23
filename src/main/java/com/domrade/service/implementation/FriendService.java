/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.service.interfaces.IFriendService;
import com.domrade.domain.Friend;
import com.domrade.domain.User;
import com.domrade.repository.IFriendRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class FriendService implements IFriendService {

    @Autowired
    private IFriendRepository friendRepository;
    
    @Override
    public List<Friend> getTest(long user) {
        return friendRepository.getTest(user);
    }

    // userId is the userId of the user that received the request in the Friend table
    @Override
    public List<Friend> getPendingFriendRequestsReceived(long userId, boolean enabled) {
        return friendRepository.getPendingFriendRequestsReceived(userId, enabled);
    }

    @Override
    public List<Friend> getApprovedFriends(boolean enabled, long userId, long requestSentById) {
        return friendRepository.getApprovedFriends(enabled, userId, requestSentById);
    }

    @Override
    public List<Friend> getPendingFriendRequestsSent(long userId, boolean enabled) {
        return friendRepository.getPendingFriendRequestsSent(userId, enabled);
    }
    
    @Override
    public Friend getAFriendRequestBySenderAndReceiver(long requestSentById, long userId, boolean enabled) {
        return friendRepository.getAFriendRequestBySenderAndReceiver(requestSentById, userId, enabled);
    }
    
    @Override
    public void saveFriend(Friend f) {
        friendRepository.save(f);
    }
    
    @Override
    public void deleteFriendRequest(Friend f) {
        friendRepository.delete(f);
    }
    
    @Override
    public Friend getAFriendRequestBySenderAndReceiver(long requestSentById, long userId) {
        Friend tempFriend = friendRepository.getAFriendRequestBySenderAndReceiver(requestSentById, userId);
        if(tempFriend == null) {
            tempFriend = friendRepository.getAFriendRequestBySenderAndReceiver(userId, requestSentById);
        }
        return tempFriend;
    }
}
