/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

import java.util.Date;

/**
 *
 * @author ComradeBaz
 */
public interface ITimeAndDateService {
    
    public String getTimeAndDateForWallPost();
    
    public String getTimeAndDateForMessage(); 
    
    public Date getTimeAndDateForMessageTimeStamp();
}
