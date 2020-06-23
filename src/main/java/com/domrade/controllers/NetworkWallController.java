/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.controllers;

import com.domrade.domain.User;
import com.domrade.domain.WallPost;
import com.domrade.domain.WallPostReply;
import com.domrade.jms.config.JmsMessageCategories;
import com.domrade.jms.config.JmsMessageTypes;
import com.domrade.jsf.common.SessionMB;
import com.domrade.service.interfaces.IJmsService;
import com.domrade.service.interfaces.INetworkService;
import com.domrade.service.interfaces.ITimeAndDateService;
import com.domrade.service.interfaces.IUserService;
import com.domrade.jsf.common.UserSessionObject;
import java.util.Map;
import javax.annotation.PostConstruct;
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
@Component ("networkWallController")
@Scope ("request")
public class NetworkWallController {
    
    private static final Logger LOGGER = Logger.getLogger(NetworkWallController.class);
    
    @Autowired
    private INetworkService networkService;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private ITimeAndDateService timeAndDateService;
    
    @Autowired
    private IJmsService jmsService;    
        
    //private List<WallPost> wallPosts;
    private SessionMB sessionMB;
    private WallPost wallPost = new WallPost();
    private WallPostReply wallPostReply = new WallPostReply();
    private UserSessionObject userSessionObject;
    //private List<WallPostReply> listWallPostReplies;
    
    /*
    Testing new domain entity
    
    private List<UserWallPost> userWallPosts;
    private UserWallPost userWallPost = new UserWallPost();
    private UserWallPostReply userWallPostReply = new UserWallPostReply();
    private List<UserWallPostReply> listUserWallPostReplies;
    /*
    ...
    */
    
    public NetworkWallController() {
        // no arg constructor
    }

    @PostConstruct
    public void initBean() {
        //setWallPosts(networkService.getWallPostsById(userService.findById(userService.getLoggedInUserId()).getNetworkId()));
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

    public WallPost getWallPost() {
        return wallPost;
    }

    public void setWallPost(WallPost wallPost) {
        this.wallPost = wallPost;
    }

    public WallPostReply getWallPostReply() {
        return wallPostReply;
    }

    public void setWallPostReply(WallPostReply wallPostReply) {
        this.wallPostReply = wallPostReply;
    }
    
    /*
    Testing new domain entity
    

    public List<UserWallPost> getUserWallPosts() {
        return userWallPosts;
    }

    public void setUserWallPosts(List<UserWallPost> userWallPosts) {
        this.userWallPosts = userWallPosts;
    }

    public UserWallPost getUserWallPost() {
        return userWallPost;
    }

    public void setUserWallPost(UserWallPost userWallPost) {
        this.userWallPost = userWallPost;
    }

    public List<UserWallPostReply> getListUserWallPostReplies() {
        return listUserWallPostReplies;
    }

    public void setListUserWallPostReplies(List<UserWallPostReply> listUserWallPostReplies) {
        this.listUserWallPostReplies = listUserWallPostReplies;
    }

    public UserWallPostReply getUserWallPostReply() {
        return userWallPostReply;
    }

    public void setUserWallPostReply(UserWallPostReply userWallPostReply) {
        this.userWallPostReply = userWallPostReply;
    }
    */    
    // Save a post to the network wall - can be a user or an admin
    @Transactional
    public String savePost() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String userPostText = params.get("userPost");
        
        User theUser = userService.findById(userService.getLoggedInUserId());
        wallPost.setAuthorFirstName(theUser.getFirstName());
        wallPost.setAuthorLastName(theUser.getLastName());
        wallPost.setTimeStamp(timeAndDateService.getTimeAndDateForWallPost());
        wallPost.setAuthorId(theUser.getId());
        wallPost.setPostText(userPostText);
        wallPost.setNetwork(networkService.findOneById(theUser.getNetworkId()));
        networkService.save(wallPost);
        /*
        Testing new domain entity        
        userWallPost.setUserWhoWrotePost(theUser);
        userWallPost.setTimeStamp(timeAndDateService.getTimeAndDateForWallPost());
        userWallPost.setNetwork(networkService.findOneById(theUser.getNetworkId()));
        networkService.save(userWallPost);
        */
        // Add the new post to the list so it can be rendered 
        //getUserWallPosts().add(0, userWallPost);
        wallPost = new WallPost();
        //userWallPost = new UserWallPost();
        // Update collections for other logged in network members
        jmsService.formatAndSendData(JmsMessageTypes.NETWORK_WALL_UPDATED.toString(),
                JmsMessageCategories.FOR_NETWORK_MEMBERS.toString(), theUser.getId(), theUser.getNetworkId(), sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());
        // Reset collections for logged in users
        userSessionObject.setWallPostsUpdated(Boolean.TRUE);
        userSessionObject.resetNetworkWallCollections();
        
        return "";
    }
    
    // Save a reply to a post on the network wall
    @Transactional
    public String saveReply() {
        
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String postId = params.get("postId");
        long currentPostId = Long.parseLong(postId);
        String replyText = params.get("uReply");

        User theUser = userService.findById(userService.getLoggedInUserId());
        wallPostReply.setAuthorFirstName(theUser.getFirstName());
        wallPostReply.setAuthorLastName(theUser.getLastName());
        wallPostReply.setUserId(theUser.getId());
        wallPostReply.setPostText(replyText);
        wallPostReply.setTimeStamp(timeAndDateService.getTimeAndDateForWallPost());
        WallPost tempWallPost = networkService.getWallPostById(currentPostId).get(0);
        wallPostReply.setWallPost(tempWallPost);
        LOGGER.log(Level.DEBUG, "About to save new wallPostReply");
        networkService.save(wallPostReply);
        
        /*
        Testing new domain entity
        
        userWallPostReply.setUserWhoWroteWallPostReply(theUser);
        userWallPostReply.setTimeStamp(timeAndDateService.getTimeAndDateForWallPost());
        UserWallPost tempUserWallPost = networkService.getUserWallPostById(currentPostId).get(0);
        userWallPostReply.setUserWallPost(tempUserWallPost);
        networkService.save(userWallPostReply);
        */
        // Update the wall with the new post
        long tempId = userService.findById(userService.getLoggedInUserId()).getNetworkId();
        //setUserWallPosts(networkService.getUserWallPostsById(tempId));

        wallPostReply = new WallPostReply();
        
        // Reset collections
        
        jmsService.formatAndSendData(JmsMessageTypes.NETWORK_WALL_UPDATED.toString(),
                JmsMessageCategories.FOR_NETWORK_MEMBERS.toString(), theUser.getId(), tempId, sessionMB.getLoggedInUser().getFirstName() + " " + sessionMB.getLoggedInUser().getLastName());
        userSessionObject.setWallPostsUpdated(Boolean.TRUE);       
        userSessionObject.resetNetworkWallCollections();
        
        LOGGER.log(Level.DEBUG, "Network wall collections updated");
        
        return "";
    }
}
