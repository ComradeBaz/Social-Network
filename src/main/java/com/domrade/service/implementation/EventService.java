/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.service.interfaces.IEventService;
import com.domrade.service.interfaces.BaseService;
import com.domrade.domain.Event;
import com.domrade.repository.IEventRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ComradeBaz
 */
@Service
public class EventService extends BaseService<Event> implements IEventService {
    
    @Autowired
    private IEventRepository eventRepository;

    @Override
    @Transactional
    public Event get(Long id) {
        Optional<Event> theEvent = eventRepository.findById(id);
        
        return theEvent.get();
    }

    @Override
    @Transactional
    public Event save(Event event) {
        return eventRepository.save(event);
    }
    
    @Override
    @Transactional
    public List<Event> getEventsByNetworkId(long netoworkId) {
        return eventRepository.getEventsByNetworkId(netoworkId);
    }
}
