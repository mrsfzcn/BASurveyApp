package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseGroupRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponse;
import com.bilgeadam.basurveyapp.entity.CourseGroup;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICourseGroupMapper {
    ICourseGroupMapper INSTANCE = Mappers.getMapper(ICourseGroupMapper.class);

    CourseGroup toCourseGroup(final CreateCourseGroupRequestDto dto);

}
