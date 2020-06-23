package com.domrade.repository;

import com.domrade.domain.Role;
import com.domrade.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends PagingAndSortingRepository<User, Long> {

    User findOneByEmail(String email);

    @Query(value = "SELECT u.role FROM User u WHERE u.id = ?1")
    List<Role> getUserRoles(long id);
    
    @Query(value = "SELECT u.role FROM User u where u.email = ?1")
    List<Role> getUserRoles(String email);
    
    @Query(value = "SELECT u FROM User u where u.networkJoinRequest = ?1")
    List<User> getPendingJoinRequests(long id);
    
    @Query(value = "SELECT u FROM User u where u.networkId = ?1 AND u.id <> ?2")
    List<User> getNetworkMembers(long networkId, long userId);
    
    @Query(value = "SELECT u FROM User u where u.networkId = ?1")
    List<User> getNetworkMembersByNetworkId(long networkId);
  
}
