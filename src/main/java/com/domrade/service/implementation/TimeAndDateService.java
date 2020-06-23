/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.service.interfaces.ITimeAndDateService;
import com.domrade.jsf.common.SessionMB;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author ComradeBaz
 * Mon June 3rd 11:45am
 */
@Service
public class TimeAndDateService implements ITimeAndDateService {

    private static final Logger LOGGER = Logger.getLogger(SessionMB.class);
    
    @Override
    public String getTimeAndDateForWallPost() {
        Date date = new Date();
        String strDateFormat = "EEEEEEEEE @ HH:mm";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        LOGGER.log(Level.INFO, strDateFormat);
        
        return formattedDate;
    }

    @Override
    public String getTimeAndDateForMessage() {
        Date date = new Date();
        String strDateFormat = "EEEEEEEEE @ HH:mm";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        LOGGER.log(Level.INFO, strDateFormat);
        
        return formattedDate;
    }

    // Return the current date/time
    @Override
    public Date getTimeAndDateForMessageTimeStamp() {
        return new Date();
    }
}
