package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.CourseGroup;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICourseGroupRepository extends BaseRepository<CourseGroup,Long> {
    boolean existsByApiId(String apiId);
}
