/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.repository;

import com.domrade.domain.WallPost;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ComradeBaz
 */
@Repository
public interface IWallPostRepository extends PagingAndSortingRepository<WallPost, Long> {
    
    // Get all wall posts for a given network
    @Query(value = "SELECT w FROM WallPost w where w.network.id = ?1 ORDER BY w.id DESC")
    List<WallPost> getWallPostsByNetworkId(long id);
    
    @Query(value = "SELECT w FROM WallPost w where w.id = ?1")
    List<WallPost> getWallPostByNetworkId(long id);
}
