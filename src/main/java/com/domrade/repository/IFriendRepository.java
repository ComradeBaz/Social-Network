/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.repository;

import com.domrade.domain.Friend;
import com.domrade.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author David
 */
@Repository
public interface IFriendRepository extends PagingAndSortingRepository<Friend, Long> {
    
    // userId is the userId of the user that received a friend request
    // requestSentBy is the userId of the user that sent the request
    // When a request is approved the row is enabled
    @Query(value = "SELECT f FROM Friend f WHERE f.requesterId = ?1 AND f.enabled = ?2")
    List<Friend> getPendingFriendRequestsSent(long userId, boolean enabled);
    
    @Query(value = "SELECT f FROM Friend f WHERE f.userId = ?1 AND f.enabled = ?2")
    List<Friend> getPendingFriendRequestsReceived(long userId, boolean enabled);
    
    @Query(value = "Select f FROM Friend f WHERE f.enabled = ?1 AND (f.userId = ?2 OR f.requesterId = ?3)")
    List<Friend> getApprovedFriends(boolean enabled, long userId, long requestSentById);
    
    @Query(value = "Select f FROM Friend f WHERE f.requesterId = ?1")
    List<Friend> getTest(long userId);
    
    // Get a Friend object by who sent it (Friend.usersId) and to whom it was sent (Friend.userId)
    @Query(value = "SELECT f FROM Friend f WHERE f.requesterId = ?1 AND f.userId = ?2 AND f.enabled = ?3")
    Friend getAFriendRequestBySenderAndReceiver(long requestSentById, long userId, boolean enabled);
    
    @Query(value = "SELECT f FROM Friend f WHERE (f.requesterId = ?1 AND f.userId = ?2) OR (f.userId = ?1 AND f.requesterId =?2)")
    Friend getAFriendRequestBySenderAndReceiver(long requestSentById, long userId);
}
