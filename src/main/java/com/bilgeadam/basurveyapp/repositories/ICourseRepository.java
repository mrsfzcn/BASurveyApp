package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.entity.Course;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICourseRepository extends BaseRepository<Course,Long> {
    boolean existsByApiId(String apiId);
    List<Course> findByNameIgnoreCase(String name);
    Optional<Course> findByApiId(String apiId);
    boolean existsByName(String name);
    Optional<Course> findByName(String name);
    List<Course> findAllByState(State state);

}