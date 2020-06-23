/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

/**
 *
 * @author David
 */
public interface ISseClientConfigService {
    
    public void setBaseBroadcastUrl(String broadcastUrl);
    
    public String getBroadcastUrl();
    
    public String getBroadcastUrlForBrowser();

}
