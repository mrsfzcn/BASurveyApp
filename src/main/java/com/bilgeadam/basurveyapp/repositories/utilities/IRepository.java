package com.bilgeadam.basurveyapp.repositories.utilities;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, Id> {
    T save(T entity, Long userOid);
    T update(T entity, Long userOid);
    void delete(T entity, Long userOid);
    void activate(T entity, Long userOid);
    void deleteById(Id oid, Long userOid);
    void activateById(Id oid, Long userOid);
    Optional<T> findById(Id oid);
    List<T> findAll();
}
