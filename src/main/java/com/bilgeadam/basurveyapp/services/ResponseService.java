package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.repositories.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResponseService {
    private final ResponseRepository responseRepository;

    public void createResponse(ResponseRequestDto responseRequestDto){
        // TODO check if exists
        Response response = Response.builder()
                .responseString(responseRequestDto.getResponseString())
                .build();
        responseRepository.save(response);
    }

    public void updateResponse(ResponseRequestDto responseRequestDto){
        Optional<Response>updatedResponse = responseRepository.findActiveById(responseRequestDto.getResponseOid());
        if(updatedResponse.isEmpty()){
            // TODO exception
           throw new RuntimeException("Response not found");
        }
        else {
            updatedResponse.get().setResponseString(responseRequestDto.getResponseString());
            responseRepository.save(updatedResponse.get());
        }
    }

    public AnswerResponseDto findByIdResponse(Long responseOid){
        // TODO exception
        Response response = responseRepository.findById(responseOid).orElseThrow();
        return AnswerResponseDto.builder()
                .responseString(response.getResponseString())
                .userOid(response.getUser().getOid())
                .questionOid(response.getQuestion().getOid())
                .build();
    }

    public List<AnswerResponseDto> findAll() {
        List<Response> findAllList = responseRepository.findAllActive();
        List<AnswerResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(response ->
                responseDtoList.add(AnswerResponseDto.builder()
                .responseString(response.getResponseString())
                .userOid(response.getUser().getOid())
                .questionOid(response.getQuestion().getOid())
                .build()));
        return responseDtoList;
    }

    public Boolean deleteResponseById(Long responseOid){
      Optional<Response> response= responseRepository.findActiveById(responseOid);
        if (response.isEmpty()) {
            // TODO exception
            throw new RuntimeException("Response is not found");
        } else {
            responseRepository.softDeleteById(response.get().getOid());
            return true;
        }
    }
}
