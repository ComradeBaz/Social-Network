/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.domain.Network;
import com.domrade.domain.Role;
import com.domrade.domain.User;
import com.domrade.jsf.common.SessionMB;
import com.domrade.jsf.common.UserSessionObject;
import com.domrade.service.interfaces.ILocationService;
import com.domrade.service.interfaces.INavigationService;
import com.domrade.service.interfaces.INetworkService;
import com.domrade.service.interfaces.IUserService;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author David
 */
@Component("logInController")
@Scope("request")
public class LogInController {
    
    private final static Logger LOGGER = Logger.getLogger(LogInController.class);
    
    @Autowired
    private SessionMB sessionMB;
    
    @Autowired
    private UserSessionObject userSessionObject;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private INetworkService networkService;

    @Autowired
    private INavigationService navigationService;

    @Autowired
    private ILocationService locationService;
    
    private String email;
    private String password;
    
    public LogInController() {
        // no arg constructor
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
        
    @Transactional
    public String login() {
        FacesMessage message = null;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(this.email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            List<Role> userRoles = userService.getUserRole(userService.getLoggedInEmailAddress());
            String oneRole = userRoles.get(0).toString();
            User loggedInUser = userService.getByEmail(this.email);
            sessionMB.setLoggedInUser(loggedInUser);
            userSessionObject.setLoggedInUser(loggedInUser);
            Network currentNetwork;
            //sseClient.startSseClient();

            switch (oneRole) {
                case "ROLE_ADMIN":
                    //LOGGER.log(Level.INFO, this.email + " network location details by IP Address "
                    //+ locationService.getLocationInformationByIpAddress(userService.getUserIpAddress()));
                    return navigationService.getProfileSettings();
                case "ROLE_ADMIN_MEMBER":
                    currentNetwork = networkService.findOneById(loggedInUser.getNetworkId());
                    sessionMB.setCurrentNetwork(currentNetwork);
                    userSessionObject.setCurrentNetwork(currentNetwork);
                    LOGGER.log(Level.INFO, this.email + " network location details by IP Address "
                            + locationService.getLocationInformationByIpAddress(userService.getUserIpAddress()));
                    // Set the wall post collection for the user
                    userSessionObject.resetNetworkWallCollections();
                    return navigationService.getTheNetwork();
                case "ROLE_USER":
                    LOGGER.log(Level.INFO, this.email + " network location details by IP Address "
                            + locationService.getLocationInformationByIpAddress(userService.getUserIpAddress()));
                    return navigationService.getProfileSettings();
                case "ROLE_USER_JOIN_REQUEST_SENT":
                    LOGGER.log(Level.INFO, this.email + " network location details by IP Address "
                            + locationService.getLocationInformationByIpAddress(userService.getUserIpAddress()));
                    return navigationService.getWaitingConfirmation();
                case "ROLE_MEMBER":
                    currentNetwork = networkService.findOneById(loggedInUser.getNetworkId());
                    sessionMB.setCurrentNetwork(currentNetwork);
                    userSessionObject.setCurrentNetwork(currentNetwork);
                    // Set the wall post collection for the user
                    userSessionObject.resetNetworkWallCollections();
                    LOGGER.log(Level.INFO, this.email + " network location details by IP Address "
                            + locationService.getLocationInformationByIpAddress(userService.getUserIpAddress()));
                    return navigationService.getTheNetwork();
            }
        } catch (BadCredentialsException e) {
            LOGGER.log(Level.ERROR, "Bad credentials for user " + email);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bad credentials", null);
        } catch (DisabledException e) {
            LOGGER.log(Level.ERROR, email + " is disabled");
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User is disabled", null);
        } catch (AuthenticationException e) {
            LOGGER.log(Level.ERROR, "Exception in authentication for user " + email);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to authenticate", null);
        }
        // If message != null a message has been created indicating an error condition
        if (message != null) {
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().addMessage("loginFormMessages", message);
        }
        return "";
    }
    
    public String logout() {
        LOGGER.log(Level.INFO, "logout");
        SecurityContextHolder.clearContext();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        //sseClient.stopSseClient();
        return "/login.xhtml?faces-redirect=true";
    }
}
