/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.repository;

import com.domrade.domain.BaseEntity;
import com.domrade.domain.WallPost;
import com.domrade.domain.WallPostReply;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ComradeBaz
 */
@Repository
public interface IWallPostReplyRepository extends PagingAndSortingRepository<BaseEntity, Long> {
    
    @Query(value = "SELECT w FROM WallPostReply w WHERE w.wallPost = ?1 ORDER BY w.id ASC")
    List<WallPostReply> getWallPostRepliesByWallPost(WallPost wallPost);
}
