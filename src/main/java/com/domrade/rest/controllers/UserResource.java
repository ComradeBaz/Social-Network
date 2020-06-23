/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.rest.controllers;

import com.domrade.domain.User;
import com.domrade.service.interfaces.IUserService;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author David
 */
@CrossOrigin(origins = "*")
@Service
@RestController
public class UserResource {

    @Autowired
    private IUserService userService;

    public UserResource() {
        // no arg constructor
    }

    @RequestMapping("users")
    public @ResponseBody List<String> getUsers() {
        List<User> userList = userService.getAllUsers();
        List<String> userNames = new ArrayList<>();
        for(User u: userList) {
            userNames.add(u.getFirstName() + " " + u.getLastName());
        }
        
        return userNames;
    }
    
    @RequestMapping("/test")
    public @ResponseBody String getTest() {
        return "Hello World";
    }

}
