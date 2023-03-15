package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.dto.response.StudentTagResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import org.mapstruct.Mapper;
import com.bilgeadam.basurveyapp.entity.User;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface StudentMapper {
    public static final StudentMapper Instance = org.mapstruct.factory.Mappers.getMapper(StudentMapper.class);
    StudentResponseDto toStudentResponseDto(User user, List<StudentTagResponseDto> studentTags);
    List<StudentResponseDto> toStudentResponseDtoList(List<User> studentList);
    List<StudentTagResponseDto> toStudentTagResponseDto(Set<StudentTag> studentTags);
}
