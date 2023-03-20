package com.bilgeadam.basurveyapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);


}