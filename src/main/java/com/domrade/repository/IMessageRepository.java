/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.repository;

import com.domrade.domain.Message;
import com.domrade.domain.User;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ComradeBaz
 */
@Repository
public interface IMessageRepository extends PagingAndSortingRepository<Message, Long>  {
        
    @Query(value = "SELECT m FROM Message m where m.receiver = ?1 ORDER BY m.id DESC")
    Set<Message> getReceivedMessagesByUser(User user);
    
    @Query(value = "SELECT m FROM Message m where m.sender = ?1 ORDER BY m.id DESC")
    Set<Message> getSentMessagesByUser(User user);
    
    @Query(value = "Select m FROM Message m WHERE (m.sender = ?1 AND m.receiver = ?2) OR (m.sender = ?2 AND m.receiver = ?1)"
            + "ORDER BY m.fullTimeStamp DESC")
    List<Message> getExistingMessages(User sender, User receiver);
    
    @Query(value = "SELECT m FROM Message m WHERE m.sender = ?1 OR m.receiver = ?1 ORDER BY m.fullTimeStamp DESC")
    List<Message> getCurrentUserMessages(User user);
}
