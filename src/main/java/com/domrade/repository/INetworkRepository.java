/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.repository;

import com.domrade.domain.Network;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ComradeBaz
 */
@Repository
public interface INetworkRepository extends PagingAndSortingRepository<Network, Long> {

    Network findOneByName(@Param("name") String name);
    
    Network findOneById(@Param("networkId") long networkId);
    
    @Query(value = "SELECT n.id FROM Network n where n.email = ?1")
    long getNetworkIdByEmailAddress(String emailAddress);
    
    List<Network> findAllNetworks();
    
}
