package com.bilgeadam.basurveyapp.services.jobservices;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseModalResponse;
import com.bilgeadam.basurveyapp.entity.Course;
import com.bilgeadam.basurveyapp.services.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseJob {

    private final CourseService courseService;

    public CourseJob(CourseService courseService) {
        this.courseService = courseService;
    }


    public void checkCourseData(List<CourseModalResponse> baseApiCourses){
        if (baseApiCourses.isEmpty()){
            throw new RuntimeException("Kurs ile ilgili herhangi bir veri bulunamamıştır.");
        }
        List<Course> currentCourses = courseService.findAllCourses();
        List<Course> deletedCourses = new ArrayList<>();

        currentCourses.forEach(cCourse -> {
            Optional<CourseModalResponse> optCourse = baseApiCourses.stream().filter(course -> ("Course-" + course.getId()).equals(cCourse.getApiId())).findFirst();
            if (optCourse.isEmpty()){
                deletedCourses.add(cCourse);
            }
        });
        if (!deletedCourses.isEmpty()){
            deletedCourses.forEach(dCourse -> courseService.deleteCourseByOid(dCourse.getOid()));
        }
        for(CourseModalResponse baseApiCourse : baseApiCourses){
            boolean existsByApiId = courseService.existByApiId("Course-" + baseApiCourse.getId());
            if (!existsByApiId){
                courseService.create(CreateCourseRequestDto.builder()
                        .apiId("Course-"+baseApiCourse.getId()).name(baseApiCourse.getName())
                        .build());
            }
        }

    }
}
