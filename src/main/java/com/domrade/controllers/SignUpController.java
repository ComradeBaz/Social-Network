/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.domain.User;
import com.domrade.jsf.common.SessionMB;
import com.domrade.service.interfaces.INavigationService;
import com.domrade.service.interfaces.ISignUpService;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author David
 */
@Component("signUpController")
@Scope("request")
public class SignUpController {

    private final static Logger LOGGER = Logger.getLogger(SignUpController.class);

    @Autowired
    private SessionMB sessionMB;

    @Autowired
    private INavigationService navigationService;

    @Autowired
    private ISignUpService signUpService;

    private String firstName;
    private String lastName;
    private String password;
    private String emailAddress;
    private String confirmPassword;
    private String userRole;
    private User user = new User();

    public SignUpController() {
        // no arg constructor
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User aUser) {
        this.user = aUser;
    }

    // Two part sign up for users
    // Capture user details from the first view
    // User makes decision on being an admin or standard user from second view
    @Transactional
    public String signUpPageOne() {
        FacesMessage message = null;

        // signUpService returns null if there is an error creating the user
        user = signUpService.saveUserDetails(password, confirmPassword, firstName, lastName, emailAddress);
        if (user != null) {
            sessionMB.setUser(user);
            return navigationService.getSignUpPageTwo();
        } else {
            LOGGER.log(Level.ERROR, "Error creating user - check password and confirm password match");
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", null);
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().addMessage("signUpFormMessages", message);
        }

        return "";
    }

    @Transactional
    public String signUpPageTwo() {
        user = sessionMB.getUser();
        signUpService.saveUserRole(user, userRole);
        sessionMB.setUser(new User());

        return navigationService.getLoginPage();
    }
}
