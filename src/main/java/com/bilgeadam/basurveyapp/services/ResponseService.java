package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDto;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.repositories.IResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Response findByIdResponse(Long responseOid){
        Response response = responseRepository.findById(responseOid).get();
        return response;
    }

    public void deleteResponseById(ResponseRequestDto responseRequestDto,Long userOid){
        responseRepository.softDeleteById(responseRequestDto.getResponseOid());
    }


}
