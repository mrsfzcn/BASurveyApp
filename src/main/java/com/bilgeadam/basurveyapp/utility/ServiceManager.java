package com.bilgeadam.basurveyapp.utility;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class ServiceManager <T,ID> implements IService<T,ID>{
    private final JpaRepository<T,ID> service;


    public ServiceManager(JpaRepository<T, ID> service) {
        this.service = service;
    }

    @Override
    public T save(T entity) {
        return service.save(entity);
    }


    @Override
    public T update(T entity) {

        return service.save(entity);
    }

    @Override
    public void deleteById(ID id) {
        T entity= service.getReferenceById(id);

    }

    @Override
    public T findById(ID id) {
        return service.getReferenceById(id);
    }

    @Override
    public List<T> findAll() {
        return service.findAll();
    }
}