package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.repositories.IResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResponseService {
    private final IResponseRepository responseRepository;

    public void createResponse(ResponseRequestDto responseRequestDto, Long userOid){
        Response response = Response.builder()
                .responseString(responseRequestDto.getResponseString())

                .build();
        responseRepository.save(response);
    }

    public void updateResponse(ResponseRequestDto responseRequestDto,Long userOid){
        Optional<Response>updatedResponse = responseRepository.findById(responseRequestDto.getResponseOid());
        if(updatedResponse.isEmpty()){
           throw new RuntimeException("Response not found");
        }
        else {
            updatedResponse.get().setResponseString(responseRequestDto.getResponseString());
            responseRepository.save(updatedResponse.get());
        }
    }

    public AnswerResponseDto findByIdResponse(Long responseOid){
        Response response = responseRepository.findById(responseOid).get();
        AnswerResponseDto dto =  AnswerResponseDto.builder()
                .responseString(response.getResponseString())
                .userOid(response.getUser().getOid())
                .questionOid(response.getQuestion().getOid())
                .build();
        return dto;
    }

    public List<AnswerResponseDto> findAll() {
        List<Response> findAllList = responseRepository.findAll();
        List<AnswerResponseDto> responseDtos = new ArrayList<>();
        findAllList.forEach(response -> {
            responseDtos.add(AnswerResponseDto.builder()
                    .responseString(response.getResponseString())
                    .userOid(response.getUser().getOid())
                    .questionOid(response.getQuestion().getOid())
                    .build());
        });
        return responseDtos;
    }

    public Boolean deleteResponseById(Long responseOid){
      Optional<Response> response= responseRepository.findById(responseOid);
        if (response.isEmpty()) {
            return false;
        } else {
            responseRepository.softDeleteById(response.get().getOid());
            return true;
        }
    }
}
