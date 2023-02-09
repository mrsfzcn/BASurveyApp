package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.response.SurveyUserResponseDto;
import com.bilgeadam.basurveyapp.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface UserMapper {

    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    SurveyUserResponseDto toSurveyUserResponseDto(User user);
    List<SurveyUserResponseDto> toSurveyUserResponseDtos(List<User> users);
}