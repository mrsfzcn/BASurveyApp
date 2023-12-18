package com.bilgeadam.basurveyapp.services.jobservices;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseGroupRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponseDto;
import com.bilgeadam.basurveyapp.entity.CourseGroup;
import com.bilgeadam.basurveyapp.services.CourseGroupService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseGroupJob {

    private final CourseGroupService courseGroupService;

    public CourseGroupJob(CourseGroupService courseGroupService) {
        this.courseGroupService = courseGroupService;
    }

    public void checkCourseGroupData(List<CourseGroupModelResponseDto> baseApiCourseGroup) {
        if (baseApiCourseGroup.isEmpty())
            throw new RuntimeException("Sınıf ile ilgili herhangi bir veri bulunamamıştır.");
        List<CourseGroup> currentCourseGroups = courseGroupService.findAllCourseGroup();
        List<CourseGroup> deletedCourseGroups = new ArrayList<>();

        currentCourseGroups.forEach(cCourseGroup -> {
            Optional<CourseGroupModelResponseDto> optCourseGroup = baseApiCourseGroup.stream().filter(courseGroup -> ("CourseGroup-" + courseGroup.getId()).equals(cCourseGroup.getApiId())).findFirst();
            if (optCourseGroup.isEmpty()) {
                deletedCourseGroups.add(cCourseGroup);
            }
        });
        if (!deletedCourseGroups.isEmpty()) {
            deletedCourseGroups.forEach(dCourseGroup -> courseGroupService.deleteCourseGroupByOid(dCourseGroup.getOid()));
        }
        for (CourseGroupModelResponseDto courseGroupApi : baseApiCourseGroup) {
            boolean existsByApiId = courseGroupService.existsByApiId("CourseGroup-" + courseGroupApi.getId());
            if (!existsByApiId) {
                courseGroupService.createCourseGroup(CreateCourseGroupRequestDto.builder()

                        .apiId("CourseGroup-" + courseGroupApi.getId())
                        .name(courseGroupApi.getName())
                        .courseId(courseGroupApi.getCourseId())
                        .branchId(courseGroupApi.getCourseId())
                        .trainers(courseGroupApi.getTrainers())
                        .startDate(courseGroupApi.getStartDate())
                        .endDate(courseGroupApi.getEndDate())
                        .build());
            }
        }
    }
}
