package com.domrade.service.interfaces;

import com.domrade.domain.Friend;
import com.domrade.domain.Role;
import com.domrade.domain.User;

import java.util.List;
import org.primefaces.model.UploadedFile;

public interface IUserService extends IBaseService<User>{

    User getByEmail(String email);
    
    public List<User> getAllUsers();

    List<Role> getAllRoles(User user);
    
    public void setUserRole(Role role, String emailAddress);
    
    public Long getLoggedInUserId();
    
    public User findById(long userId);
    
    public List<Role> getUserRole(String email);
    
    public List<User> getPendingJoinRequests(long id);
    
    public String getLoggedInEmailAddress();
    
    public String getUserIpAddress();
    
    public void saveUserProfilePicture(UploadedFile profilePicture, long userId);
    
    public List<User> getUsersListFromFriends(List<Friend> theFriends);
    
    public List<User> getNetworkMembersByNetworkId(long networkId, long userId);
    
    public List<User> getNetworkMembersByNetworkId(long networkId);
    
    public List<User> getUserFromFriendObject(List<Friend> sentList, User loggedInUser);
    
    public void setUserHasNotification(long userId);
}
