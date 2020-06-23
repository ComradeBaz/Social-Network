/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

import org.springframework.stereotype.Service;

/**
 *
 * @author ComradeBaz
 */
@Service
public interface ILocationService {
    
    public String getLocationInformationByIpAddress(String ipAddress);
    
    public double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2);
    
}
