package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IBranchMapper {

    IBranchMapper INSTANCE = Mappers.getMapper(IBranchMapper.class);

    Branch toBranch(final CreateBranchRequestDto dto);


}
