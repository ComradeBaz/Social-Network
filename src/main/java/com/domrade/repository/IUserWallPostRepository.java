/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.repository;

import com.domrade.domain.UserWallPost;
import com.domrade.domain.WallPost;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author David
 */
@Repository
public interface IUserWallPostRepository extends PagingAndSortingRepository<UserWallPost, Long>{
    
    // Get all wall posts for a given network
    @Query(value = "SELECT w FROM UserWallPost w where w.network.id = ?1 ORDER BY w.id DESC")
    List<UserWallPost> getWallPostsByNetworkId(long id);
    
    @Query(value = "SELECT w FROM UserWallPost w where w.id = ?1")
    List<UserWallPost> getWallPostByNetworkId(long id);
}
