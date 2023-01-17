package com.bilgeadam.basurveyapp.repositories.utilities;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, Id> {
    T save(T entity);
    Iterable<T> saveAll(Iterable<T> entities);
    T update(T entity);
    void delete(T entity);
    void activate(T entity);
    void deleteById(Id id);
    void activateById(Id id);
    Optional<T> findById(Id id);
    List<T> findAll();
}
