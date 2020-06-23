/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.service.implementation.SseClientConfigService;
import com.domrade.sse.MySseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author David
 */
@Component
@Scope("request")
public class AppConfigController {
    
    @Autowired
    private SseClientConfigService sseClientConfigService;
    
    @Autowired
    private MySseClient mySseClient;
    
    private String sseServerUrl;
    private String sseServerUrlForBrowser;
    
    public AppConfigController() {
        // no arg constructor
    }

    public String getSseServerUrl() {
        return sseServerUrl;
    }

    public void setSseServerUrl(String sseServerUrl) {
        this.sseServerUrl = sseServerUrl;
    }
    
    public String setSseServerUrlForApp() {
        sseClientConfigService.setBaseBroadcastUrl(sseServerUrl);
        
        return "";
    }
    
    public String getSseServerUrlForApp() {
        return sseClientConfigService.getBroadcastUrl();
    }
    
    public void setServerUrlForBrowser() {
        this.sseServerUrl = sseClientConfigService.getBroadcastUrlForBrowser();
    }
    
    public String getSseServerUrlForBrowser() {
        return sseServerUrlForBrowser;
    }
    
    public String startSseClient() {
        mySseClient.startSseClient();
        
        return "";
    }
    
    public String stopSseClient() {
        mySseClient.stopSseClient();
        
        return "";
    }
    
    public void restartSseClient() {
        stopSseClient();
        startSseClient();
    }
}
