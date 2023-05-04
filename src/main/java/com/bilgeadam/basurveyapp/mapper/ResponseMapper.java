package com.bilgeadam.basurveyapp.mapper;

import com.bilgeadam.basurveyapp.dto.request.ResponseRequestSaveDto;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDataObject;
import com.bilgeadam.basurveyapp.dto.response.ResponseDto;
import com.bilgeadam.basurveyapp.dto.response.ResponseUnmaskedDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResponseMapper {
    ResponseMapper INSTANCE = Mappers.getMapper(ResponseMapper.class);

    List<AnswerResponseDto> toAnswerResponseDto(final List<Response> responseDtoList);

    @Mapping(target = "userOid", source = "response.user.oid")
    @Mapping(target = "questionOid", source = "response.question.oid")
    @Mapping(target = "surveyOid", source = "response.survey.oid")
    AnswerResponseDto toAnswerResponseDto(final Response response);

    Response toResponse(final ResponseRequestSaveDto responseRequestSaveDto, final Question question, final User user);

    Response toResponse(final ResponseRequestDataObject responseDto);

    List<Response> toResponseList(final List<ResponseRequestDataObject> responseDtoList);

    @Mapping(target = "responseOid", source = "oid")
    @Mapping(target = "questionOid", source = "response.question.oid")
    @Mapping(target = "userOid", source = "response.user.oid")
    @Mapping(target = "surveyOid", source = "response.survey.oid")
    ResponseDto toResponseDto(final Response response);

    List<ResponseDto> toResponseDto(final List<Response> responses);


}
