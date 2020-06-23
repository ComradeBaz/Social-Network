/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.service.interfaces.ILocationService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author ComradeBaz modified from https://ipapi.co/api/?java#complete-location
 * Format of the returned JSON object { "ip": "86.45.131.68", "city": "Arklow",
 * "region": "Leinster", "region_code": "L", "country": "IE", "country_name":
 * "Ireland", "continent_code": "EU", "in_eu": true, "postal": "Y14",
 * "latitude": 52.7931, "longitude": -6.1414, "timezone": "Europe/Dublin",
 * "utc_offset": "+0000", "country_calling_code": "+353", "currency": "EUR",
 * "languages": "en-IE,ga-IE", "asn": "AS5466", "org": "Eir Broadband" }
 *
 */
@Service
public class LocationService implements ILocationService {

    static final Logger LOGGER = Logger.getLogger(LocationService.class.getName());

    @Override
    public String getLocationInformationByIpAddress(String ipAddress) {
        URL ipapi;
        String line = "", retValue = "Dummy Info";
        //ipAddress = "86.45.131.68";
        /*
        try {
            String urlStart = "https://ipapi.co/";
            String urlEnd = "/json";
            ipapi = new URL(urlStart + ipAddress + urlEnd);
            LOGGER.log(Level.INFO, "URL for location service is " + ipapi);

            URLConnection c = ipapi.openConnection();
            c.setRequestProperty("User-Agent", "java-ipapi-client");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(c.getInputStream())
            );

            while ((line = reader.readLine()) != null) {
                retValue += line;
            }
            reader.close();
        } catch (MalformedURLException ex) {
            LOGGER.log(Level.ERROR, "Error accessing location service, malformed URL ");
        } catch (IOException io) {
            LOGGER.log(Level.ERROR, "Error accessing location service " + io.getMessage());
        }
        */
        return retValue;
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @param lat1
     * @param lat2
     * @param lon1
     * @param lon2
     * @param el1
     * @param el2
     * @return 
     * @returns Distance in Meters
     */
    
    @Override
    public double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
