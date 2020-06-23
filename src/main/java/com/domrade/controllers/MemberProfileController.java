/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.jsf.common.SessionMB;
import com.domrade.jsf.common.UserSessionObject;
import com.domrade.service.interfaces.IUserService;
import java.io.Serializable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author ComradeBaz
 */
@Component("memberProfileController")
@Scope("request")
public class MemberProfileController implements Serializable {

    @Autowired
    private IUserService userService;
    
    private SessionMB sessionMB;
    private UploadedFile profilePicture;
    private UserSessionObject userSessionObject;

    public MemberProfileController() {
        // no arg constructor
    }
    
    @Autowired
    public void setUserSessionObject(UserSessionObject userSessionObject) {
        this.userSessionObject = userSessionObject;
    }

    public UserSessionObject getUserSessionObject() {
        return userSessionObject;
    }
    
    public SessionMB getSessionMB() {
        return sessionMB;
    }

    @Autowired
    public void setSessionMB(SessionMB sessionMB) {
        this.sessionMB = sessionMB;
    }

    public UploadedFile getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(UploadedFile profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    // Method is called automatically when the fileUpload dialog is closed on profileSettings.xhtml
    public void upload(FileUploadEvent event) {
        profilePicture = event.getFile();
        userService.saveUserProfilePicture(profilePicture, userService.getLoggedInUserId());
        // Update the loggedInUser in memory so the correct profiler is displayed in the app
        sessionMB.getLoggedInUser().setProfilePicture(userService.get(userService.getLoggedInUserId()).getProfilePicture());
    }
}
