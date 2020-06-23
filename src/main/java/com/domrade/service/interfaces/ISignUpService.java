/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.interfaces;

import com.domrade.domain.User;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public interface ISignUpService {
    
    public User saveUserDetails(String password, String confirmPassword, String firstName, String lastName, String emailAddress);
    
    public void saveUserRole(User user, String userRole);
    
}
