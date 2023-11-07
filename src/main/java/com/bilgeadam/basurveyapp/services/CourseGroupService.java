package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseGroupRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponse;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.manager.IBranchManager;
import com.bilgeadam.basurveyapp.manager.ICourseGroupManager;
import com.bilgeadam.basurveyapp.mapper.ICourseGroupMapper;
import com.bilgeadam.basurveyapp.repositories.ICourseGroupRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseGroupService{
    private final ICourseGroupManager courseGroupManager;
    private final ICourseGroupRepository courseGroupRepository;

    public CourseGroupService(ICourseGroupManager courseGroupManager, ICourseGroupRepository courseGroupRepository) {
        super();
        this.courseGroupManager = courseGroupManager;
        this.courseGroupRepository = courseGroupRepository;
    }

    public List<CourseGroupModelResponse> getAllDataFromCourseGroup() {
        List<CourseGroupModelResponse> allData = courseGroupManager.findall().getBody();
        if (allData.isEmpty()) {
            throw new RuntimeException("Data boştur");
        }
        for (CourseGroupModelResponse groupCourses: allData){
            createCourseGroup(CreateCourseGroupRequestDto.builder()
                    .apiId("CourseGroup-"+groupCourses.getId())
                    .name(groupCourses.getName())
                    .courseId(groupCourses.getCourseId())
                    .branchId(groupCourses.getBranchId())
                    .trainers(groupCourses.getTrainers())
                    .startDate(groupCourses.getStartDate())
                    .endDate(groupCourses.getEndDate())
                    .build());
        }
        return allData;
    }

    public MessageResponseDto createCourseGroup(CreateCourseGroupRequestDto dto){
        courseGroupRepository.save(ICourseGroupMapper.INSTANCE.toCourseGroup(dto));
        return MessageResponseDto.builder()
                .message(dto.getName()+ "isim sınıf" + dto.getCourseId() + "kurs" + dto.getBranchId()+ "şubesine ait" + dto.getTrainers() + " sahip eğitmenleri" + dto.getStartDate() + "baslangic tarihi" + dto.getEndDate() + "bitis tarihli sınıf eklendi")
                .build();
    }



}