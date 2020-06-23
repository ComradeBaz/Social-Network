/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.sse;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author David
 */
@Scope("singleton")
@Component
public final class SseClientConfig {
    private static final Logger LOGGER = Logger.getLogger(SseClientConfig.class);
    private final String defaultBaseUrl = "http://192.168.1.128:8080/services/api/stats/";
    private String baseUrl;
    private String broadcastUrl;
    private String broadcastUrlForBrowser;
    private final String browserUri = "broadcastToBrowser";
    private final String broadcastUri = "broadcast";
    
    public SseClientConfig() {
        // no arg constructor
        setBaseBroadcastUrl(defaultBaseUrl);
    }
    
    public String getBroadcastUrl() {
        return broadcastUrl;
    }
    
    public String getBroadcastUrlForBrowser() {
        return broadcastUrlForBrowser;
    }

    public void setBaseBroadcastUrl(String broadcastUrl) {
        this.baseUrl = broadcastUrl;
        setBroadcastUrl();
        setBrowserBroadcastUrl();
    }
    
    public void setBrowserBroadcastUrl() {
        this.broadcastUrlForBrowser = baseUrl + browserUri;
    }
    
    public void setBroadcastUrl() {
        this.broadcastUrl = baseUrl + broadcastUri;
    }
}
