/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.repository;

import com.domrade.domain.Event;
import com.domrade.domain.Network;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ComradeBaz
 */
@Repository
public interface IEventRepository extends PagingAndSortingRepository<Event, Long> {
    
    @Query(value = "SELECT e FROM Event e where e.network.id = ?1")
    List<Event> getEventsByNetworkId(long netoworkId);
    
}
