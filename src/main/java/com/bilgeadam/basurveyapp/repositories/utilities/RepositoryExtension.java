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
public abstract class RepositoryExtension<T extends BaseEntity, Id> implements IRepository<T, Id> {
    private final JpaRepository<T, Id> repository;

    @Override
    public T save(T entity) {
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        entity.setState(State.ACTIVE);
        return repository.save(entity);
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> entities) {
        for (T entity : entities) {
            entity.setCreatedAt(new Date());
            entity.setUpdatedAt(new Date());
            entity.setState(State.ACTIVE);
        }
        return repository.saveAll(entities);
    }

    @Override
    public T update(T entity) {
        entity.setUpdatedAt(new Date());
        return repository.save(entity);
    }

    @Override
    public void delete(T entity) {
        entity.setUpdatedAt(new Date());
        entity.setState(State.PASSIVE);
        repository.save(entity);
    }

    @Override
    public void activate(T entity) {
        entity.setUpdatedAt(new Date());
        entity.setState(State.ACTIVE);
        repository.save(entity);
    }

    @Override
    public void deleteById(Id id) {
        T entity = repository.getReferenceById(id);
        entity.setUpdatedAt(new Date());
        entity.setState(State.PASSIVE);
        repository.save(entity);
    }

    @Override
    public void activateById(Id id) {
        T entity = repository.getReferenceById(id);
        entity.setUpdatedAt(new Date());
        entity.setState(State.ACTIVE);
        repository.save(entity);
    }

    @Override
    public Optional<T> findById(Id id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

}
