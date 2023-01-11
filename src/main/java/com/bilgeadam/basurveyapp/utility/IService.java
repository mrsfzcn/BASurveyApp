package com.bilgeadam.basurveyapp.utility;


import java.util.List;

public interface IService <T,ID>{
    T save(T entity);
    T update(T entity);
    void deleteById(ID id);
    T findById(ID id);
    List<T> findAll();
}