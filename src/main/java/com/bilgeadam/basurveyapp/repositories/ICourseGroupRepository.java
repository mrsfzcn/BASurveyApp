package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.CourseGroup;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICourseGroupRepository extends BaseRepository<CourseGroup, Long> {
    boolean existsByApiId(String apiId);

    List<CourseGroup> findByName(String name);

    List<CourseGroup> findCourseGroupByCourseId(Long courseId);

    List<CourseGroup> findCourseGroupByBranchId(Long branchId);

    Optional<CourseGroup> findByApiIdAndState(String apiId, State state);




}
