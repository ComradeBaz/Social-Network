/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.repository;

import com.domrade.domain.BaseEntity;
import com.domrade.domain.UserWallPost;
import com.domrade.domain.UserWallPostReply;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author David
 */
@Repository
public interface IUserWallPostReplyRepository extends PagingAndSortingRepository<BaseEntity, Long> {
    
    @Query(value = "SELECT w FROM UserWallPostReply w WHERE w.userWallPost = ?1 ORDER BY w.id DESC")
    List<UserWallPostReply> getWallPostRepliesByWallPost(UserWallPost wallPost);
}
