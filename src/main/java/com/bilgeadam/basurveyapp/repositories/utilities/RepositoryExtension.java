package com.bilgeadam.basurveyapp.repositories.utilities;

import com.bilgeadam.basurveyapp.entity.baseentity.BaseEntity;
import com.bilgeadam.basurveyapp.entity.enums.State;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class RepositoryExtension<T extends BaseEntity, Oid> implements IRepository<T, Oid> {
    private final JpaRepository<T, Oid> repository;

    @Override
    public T save(T entity, Long userOid) {
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        entity.setCreatedBy(userOid);
        entity.setUpdatedBy(userOid);
        entity.setState(State.ACTIVE);
        return repository.save(entity);
    }

    @Override
    public List<T> saveAll(List<T> entities, Long userOid) {
        for (T entity : entities) {
            entity.setCreatedAt(new Date());
            entity.setUpdatedAt(new Date());
            entity.setCreatedBy(userOid);
            entity.setUpdatedBy(userOid);
            entity.setState(State.ACTIVE);
        }
        return repository.saveAll(entities);
    }

    @Override
    public T update(T entity, Long userOid) {
        entity.setUpdatedAt(new Date());
        entity.setUpdatedBy(userOid);
        return repository.save(entity);
    }

    @Override
    public void delete(T entity, Long userOid) {
        entity.setUpdatedAt(new Date());
        entity.setUpdatedBy(userOid);
        entity.setState(State.PASSIVE);
        repository.save(entity);
    }

    @Override
    public void activate(T entity, Long userOid) {
        entity.setUpdatedAt(new Date());
        entity.setUpdatedBy(userOid);
        entity.setState(State.ACTIVE);
        repository.save(entity);
    }

    @Override
    public void deleteById(Oid oid, Long userOid) {
        T entity = repository.getReferenceById(oid);
        entity.setUpdatedAt(new Date());
        entity.setUpdatedBy(userOid);
        entity.setState(State.PASSIVE);
        repository.save(entity);
    }

    @Override
    public void activateById(Oid oid, Long userOid) {
        T entity = repository.getReferenceById(oid);
        entity.setUpdatedAt(new Date());
        entity.setUpdatedBy(userOid);
        entity.setState(State.ACTIVE);
        repository.save(entity);
    }

    @Override
    public Optional<T> findById(Oid oid) {
        return repository.findById(oid);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

}
