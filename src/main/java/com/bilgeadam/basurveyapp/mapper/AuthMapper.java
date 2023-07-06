package com.bilgeadam.basurveyapp.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

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


