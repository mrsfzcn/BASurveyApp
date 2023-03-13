

package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    List<StudentResponseDto> toStudentResponseDto(final List<User> users);
    List<StudentTagResponseDto> toStudentStudentTagResponseDto(final List<StudentTag> studentTags);

    List<MasterTrainerResponseDto> toMasterTrainerResponseDto(final List<User> users);
    List<MasterTrainerTagResponseDto> toMasterTrainerTagResponseDto(final List<TrainerTag> trainerTags);

    List<AssistantTrainerResponseDto> toAssistantTrainerResponseDto(final List<User> users);
    List<AssistantTrainerTagResponseDto> toAssistantTrainerTagResponseDto(final List<TrainerTag> trainerTags);

    List<ManagerResponseDto> toManagerResponseDto(final List<User> users);

    List<AdminResponseDto> toAdminResponseDto(final List<User> users);

    List<UserTrainersAndStudentsResponseDto> toUserTrainersAndStudentsResponseDto(final List<User> users);

    Set<UserTrainersAndStudentsRolesResponseDto> toUserTrainersAndStudentsRolesResponseDto(final Set<Role> roles);






    SurveyUserResponseDto toSurveyUserResponseDto(User user);
    List<SurveyUserResponseDto> toSurveyUserResponseDtos(List<User> users);
}