package com.domrade.service.interfaces;

import com.domrade.domain.BaseEntity;
import com.domrade.repository.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public abstract class BaseService<T extends BaseEntity> implements IBaseService<T> {

    @Autowired
    IBaseRepository baseRepository;

    @Override
    @Transactional
    public T save(T t) {
        return baseRepository.save(t);
    }

    @Override
    @Transactional
    public void delete(T t) {
        baseRepository.delete(t);
    }

    @Override
    @Transactional(readOnly = true)
    public abstract T get(Long id);
}
