/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

import com.domrade.domain.Event;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author ComradeBaz
 */
@Service
public interface IEventService {

    public List<Event> getEventsByNetworkId(long netoworkId);
    
    public Event save(Event event);
    
    public Event get(Long id);
}
