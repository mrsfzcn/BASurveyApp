

package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    List<StudentResponseDto> toStudentResponseDto(final List<User> users);
    List<StudentClassroomResponseDto> toStudentClassroomResponseDto(final List<Classroom> classroom);

    List<MasterTrainerResponseDto> toMasterTrainerResponseDto(final List<User> users);
    List<MasterTrainerClassroomResponseDto> toMasterTrainerClassroomResponseDto(final List<Classroom> classroom);

    List<AssistantTrainerResponseDto> toAssistantTrainerResponseDto(final List<User> users);
    List<AssistantTrainerClassroomResponseDto> toAssistantTrainerClassroomResponseDto(final List<Classroom> classroom);

    List<ManagerResponseDto> toManagerResponseDto(final List<User> users);

    List<AdminResponseDto> toAdminResponseDto(final List<User> users);

    List<UserTrainersAndStudentsResponseDto> toUserTrainersAndStudentsResponseDto(final List<User> users);

    Set<UserTrainersAndStudentsRolesResponseDto> toUserTrainersAndStudentsRolesResponseDto(final Set<Role> roles);






    SurveyUserResponseDto toSurveyUserResponseDto(User user);
    List<SurveyUserResponseDto> toSurveyUserResponseDtos(List<User> users);
}