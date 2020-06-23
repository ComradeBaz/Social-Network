/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.service.implementation;

import com.domrade.service.interfaces.IUserService;
import com.domrade.service.interfaces.BaseService;
import com.domrade.service.interfaces.INetworkService;
import com.domrade.service.interfaces.ITimeAndDateService;
import com.domrade.domain.Network;
import com.domrade.domain.Role;
import com.domrade.domain.User;
import com.domrade.domain.UserWallPost;
import com.domrade.domain.UserWallPostReply;
import com.domrade.domain.WallPost;
import com.domrade.domain.WallPostReply;
import com.domrade.jsf.common.SessionMB;
import com.domrade.repository.INetworkRepository;
import com.domrade.repository.IUserWallPostReplyRepository;
import com.domrade.repository.IUserWallPostRepository;
import com.domrade.repository.IWallPostReplyRepository;
import com.domrade.repository.IWallPostRepository;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ComradeBaz
 */
@Service
public class NetworkService extends BaseService<Network> implements INetworkService {

    static final Logger LOGGER = Logger.getLogger(NetworkService.class.getName());
    private WallPost wallPost = new WallPost();

    @Autowired
    private INetworkRepository networkRepository;

    @Autowired
    private IWallPostRepository wallPostRepository;

    @Autowired
    private IUserWallPostRepository userWallPostRepository;

    @Autowired
    private IUserWallPostReplyRepository userWallPostReplyRepository;

    //@Autowired
    //private INetworkService networkService;
    @Autowired
    private IUserService userService;

    @Autowired
    private IWallPostReplyRepository wallPostReplyRepository;

    @Autowired
    private ITimeAndDateService timeAndDateService;

    @Override
    public Network get(Long id) {
        Optional<Network> aNetwork = networkRepository.findById(id);

        return aNetwork.get();
    }

    @Override
    public Network findOneByName(String name) {
        return networkRepository.findOneByName(name);
    }

    @Override
    public Network findOneById(long networkId) {
        return networkRepository.findOneById(networkId);
    }

    @Override
    public List<Network> findAllNetworks() {
        return networkRepository.findAllNetworks();
    }

    @Override
    public Network save(Network network) {
        return networkRepository.save(network);
    }

    @Override
    @Transactional
    public void save(WallPostReply wallPostReply) {
        wallPostReplyRepository.save(wallPostReply);
    }

    @Override
    public void save(UserWallPost userWallPost) {
        userWallPostRepository.save(userWallPost);
    }

    @Override
    public void save(UserWallPostReply userWallPostReply) {
        userWallPostReplyRepository.save(userWallPostReply);
    }

    @Override
    @Transactional
    public void requestJoinNetwork(String networkName, Long userId) {
        Network theNetwork = networkRepository.findOneByName(networkName);
        User user = userService.findById(userId);
        if (user != null) {

            user.setNetworkJoinRequest(theNetwork.getId());
            user.setRole(Role.ROLE_USER_JOIN_REQUEST_SENT);
            userService.save(user);

            LOGGER.log(Level.INFO, "User " + user.getEmail() + " reqesting to join Network " + networkName + ": network_id = " + theNetwork.getId());
        }
    }

    @Override
    @Transactional
    public void cancelRequestJoinNetwork(long userId) {
        User user = userService.findById(userId);
        user.setNetworkJoinRequest(0L);
        user.setRole(Role.ROLE_USER);
        userService.save(user);
    }

    @Override
    public void leaveNetwork(String networkName, Long userId) {
        Network theNetwork = networkRepository.findOneByName(networkName);
        User user = userService.findById(userId);
        if (user != null) {

            user.setNetworkId(0L);
            // The welcome page for a user depends on their role when they log in
            // ROLE_USER will see a page prompting them to join a network
            user.setRole(Role.ROLE_USER);
            userService.save(user);

            LOGGER.log(Level.INFO, "User " + user.getEmail() + " has left Network " + networkName + ": network_id = " + theNetwork.getId());
        }
    }

    @Override
    public long getNetworkIdByEmailAddress(String emailAddress) {
        long noNetwork = -1L;
        long result = networkRepository.getNetworkIdByEmailAddress(emailAddress);
        if (result > 0) {
            return result;
        } else {
            return noNetwork;
        }
    }

    @Override
    public void acceptRequestJoinNetwork(String userIdString) {
        User aUser = userService.findById(Long.parseLong(userIdString));
        aUser.setNetworkJoinRequest(null);
        aUser.setNetworkId(getNetworkIdByEmailAddress(userService.getLoggedInEmailAddress()));
        LOGGER.log(Level.INFO, "Accepted request from " + userService.findById(Long.parseLong(userIdString)).getEmail());
        aUser.setRole(Role.ROLE_MEMBER);
        userService.save(aUser);
        saveUserHasJoinedWallPost(userIdString);
    }

    @Override
    public void save(WallPost wallPost) {
        wallPostRepository.save(wallPost);
    }

    @Override
    public List<WallPost> getWallPostsById(long id) {
        return wallPostRepository.getWallPostsByNetworkId(id);
    }

    @Override
    public List<WallPost> getWallPostById(long id) {
        return wallPostRepository.getWallPostByNetworkId(id);
    }

    @Override
    public List<UserWallPost> getUserWallPostsById(long id) {
        return userWallPostRepository.getWallPostsByNetworkId(id);
    }

    @Override
    public List<UserWallPost> getUserWallPostById(long id) {
        return userWallPostRepository.getWallPostByNetworkId(id);
    }

    private void saveUserHasJoinedWallPost(String userIdString) {
        User aUser = userService.findById(Long.parseLong(userIdString));
        long networkId = getNetworkIdByEmailAddress(userService.getLoggedInEmailAddress());
        wallPost.setNetwork(findOneById(networkId));
        wallPost.setAuthorFirstName(aUser.getFirstName());
        wallPost.setAuthorLastName(aUser.getLastName());
        wallPost.setAuthorId(aUser.getId());
        wallPost.setTimeStamp(timeAndDateService.getTimeAndDateForWallPost());
        wallPost.setPostText("New Member!");
        save(wallPost);
        wallPost = new WallPost();
    }

    // get network members, not including the admin for the network
    @Override
    public List<User> getNetworkMembersByNetworkId(long networkId, long userId) {
        return userService.getNetworkMembersByNetworkId(networkId, userId);
    }

    @Override
    public String getNetworkNameById(long id) {
        Network theNetwork = findOneById(id);
        return theNetwork.getName();
    }

    @Override
    public String getNetworkJoinRequestByLoggedInUser(User aUser) {
        User theUser = userService.findById(userService.getLoggedInUserId());
        return getNetworkNameById(theUser.getNetworkJoinRequest());
    }

    @Override
    public List<WallPostReply> getWallPostRepliesByWallPost(WallPost wallPost) {
        return wallPostReplyRepository.getWallPostRepliesByWallPost(wallPost);
    }

    @Override
    public String logUserOutOfNetwork() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Network getNetworkByName(String networkName) {
        return networkRepository.findOneByName(networkName);
    }

    @Override
    @Transactional
    public void setMembersHaveNotification(long networkId, long userId) {
        List<User> networkMembers = userService.getNetworkMembersByNetworkId(networkId);
        for (User u : networkMembers) {
            if (u.getId() != userId) {
                User aUser = userService.findById(u.getId());
                aUser.setHasNotification(Boolean.TRUE);
                userService.save(aUser);
            }
        }
    }

    @Override
    public User getNetworkAdminByNetworkId(long networkId) {
        String networkAdminEmailAddress = networkRepository.findOneById(networkId).getEmail();
        return userService.getByEmail(networkAdminEmailAddress);
    }
}
