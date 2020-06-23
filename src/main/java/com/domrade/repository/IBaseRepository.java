package com.domrade.repository;

import com.domrade.domain.BaseEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IBaseRepository extends PagingAndSortingRepository<BaseEntity, Long> {
}
