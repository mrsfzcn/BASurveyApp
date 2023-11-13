package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseRequestDto;
import com.bilgeadam.basurveyapp.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICourseMapper {
    ICourseMapper INSTANCE = Mappers.getMapper(ICourseMapper.class);
    Course toCourse(final CreateCourseRequestDto dto);
}
