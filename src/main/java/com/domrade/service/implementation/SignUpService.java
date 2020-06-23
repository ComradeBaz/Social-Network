/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.domain.Role;
import com.domrade.domain.User;
import com.domrade.security.PBKDF2Hasher;
import com.domrade.service.interfaces.ISignUpService;
import com.domrade.service.interfaces.IUserService;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author David
 */
@Service
public class SignUpService implements ISignUpService {

    private final static Logger LOGGER = Logger.getLogger(SignUpService.class);
    
    @Autowired
    private PBKDF2Hasher epb;
    
    @Autowired
    private IUserService userService;
    
    private User user;
    
    @Override
    public User saveUserDetails(String password, String confirmPassword, String firstName, String lastName, String emailAddress) {
        user = new User();
        LOGGER.log(Level.DEBUG, "Signup - get user details");

        if (password.equals(confirmPassword)) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(emailAddress);
            user.setPassword(epb.encode(String.valueOf(password)));
            user.setEnabled(Boolean.TRUE);

            return user;
        }

        return null;
    }

    @Override
    @Transactional
    public void saveUserRole(User user, String userRole) {
        LOGGER.log(Level.DEBUG, "Signup - choose admin or user");
        if (userRole.equals("networkUser")) {
            user.setRole(Role.ROLE_USER);
        } else if (userRole.equals("networkAdministrator")) {
            user.setRole(Role.ROLE_ADMIN);
        }
        // Add a default profile picture
        user.setProfilePicture("src\\main\\resources\\static\\D\\defaultProfiler.jpg");
        userService.save(user);
    }    
}
