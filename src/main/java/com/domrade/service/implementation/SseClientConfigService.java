/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.service.interfaces.ISseClientConfigService;
import com.domrade.sse.SseClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class SseClientConfigService implements ISseClientConfigService {
    
    @Autowired
    private SseClientConfig sseClientConfig;

    @Override
    public void setBaseBroadcastUrl(String broadcastUrl) {
        sseClientConfig.setBaseBroadcastUrl(broadcastUrl);
    }    

    @Override
    public String getBroadcastUrl() {
        return sseClientConfig.getBroadcastUrl();
    }
    
    @Override
    public String getBroadcastUrlForBrowser() {
        return sseClientConfig.getBroadcastUrlForBrowser();
    }
}
