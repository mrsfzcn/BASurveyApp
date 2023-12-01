package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);
}
