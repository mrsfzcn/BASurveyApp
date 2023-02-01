package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.exceptions.custom.ResponseNotFoundException;
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
        // The same answer can be recreated over and over again. There not will be exist checking
        Response response = Response.builder()
                .responseString(responseRequestDto.getResponseString())
                .build();
        responseRepository.save(response);
    }

    public void updateResponse(ResponseRequestDto responseRequestDto){
        Optional<Response>updatedResponse = responseRepository.findActiveById(responseRequestDto.getResponseOid());
        if(updatedResponse.isEmpty()){
           throw new ResponseNotFoundException("There's a error while finding response");
        }
        else {
            updatedResponse.get().setResponseString(responseRequestDto.getResponseString());
            responseRepository.save(updatedResponse.get());
        }
    }

    public AnswerResponseDto findByIdResponse(Long responseOid){
        Optional <Response> response = responseRepository.findById(responseOid);
        if(response.isEmpty()){
            throw new ResponseNotFoundException("There's a error while finding response");
        }
        return AnswerResponseDto.builder()
                .responseString(response.get().getResponseString())
                .userOid(response.get().getUser().getOid())
                .questionOid(response.get().getQuestion().getOid())
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
            throw new ResponseNotFoundException("There's a error while finding response");
        } else {
            responseRepository.softDeleteById(response.get().getOid());
            return true;
        }
    }
}
