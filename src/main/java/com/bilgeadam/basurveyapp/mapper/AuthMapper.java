package com.bilgeadam.basurveyapp.mapper;


import com.bilgeadam.basurveyapp.dto.request.RegisterRequestDto;
import com.bilgeadam.basurveyapp.dto.response.UserRoleResponseDto;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.User;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

//    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
//    User ToUser(RegisterRequestDto registerRequestDto);
//
//    UserRoleResponseDto toUserRoleResponseDto( final Role role);
//    Set<UserRoleResponseDto> toUserRoleResponseDto(final Set<Role> roles);
//

}


