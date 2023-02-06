package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
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
    private final SurveyService surveyService;

    public void createResponse(ResponseRequestDto responseRequestDto) {
        // The same answer can be recreated over and over again. There not will be existed checking
        Response response = Response.builder()
                .responseString(responseRequestDto.getResponseString())
                .build();
        responseRepository.save(response);
    }

    public void updateResponse(ResponseRequestDto responseRequestDto) {
        Optional<Response> updatedResponse = responseRepository.findActiveById(responseRequestDto.getResponseOid());
        if (updatedResponse.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding response");
        } else {
            updatedResponse.get().setResponseString(responseRequestDto.getResponseString());
            responseRepository.save(updatedResponse.get());
        }
    }

    public AnswerResponseDto findByIdResponse(Long responseOid) {
        Optional<Response> response = responseRepository.findById(responseOid);
        if (response.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding response");
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

    public Boolean deleteResponseById(Long responseOid) {
        Optional<Response> response = responseRepository.findActiveById(responseOid);
        if (response.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding response");
        } else {
            responseRepository.softDeleteById(response.get().getOid());
            return true;
        }
    }

    public List<AnswerResponseDto> findResponseByClassroomOid(Long classroomOid) {
        List<Survey> surveyList = surveyService.findByClassroomOid(classroomOid);
        if (surveyList.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding survey list");
        }
        List<Question> questionList = surveyList.stream().flatMap(s -> s.getQuestions().stream()
        ).toList();
        if (questionList.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding questions");
        }
        List<Response> responseList = questionList.stream().flatMap(q -> q.getResponses().stream()).toList();
        if (responseList.isEmpty()) {
            throw new ResourceNotFoundException("There's a error while finding response");
        }

        List<AnswerResponseDto> answerResponseDtoList = new ArrayList<>();
        responseList.forEach(r -> answerResponseDtoList.add(AnswerResponseDto.builder()
                .responseString(r.getResponseString())
                .userOid(r.getUser().getOid())
                .questionOid(r.getQuestion().getOid())
                .build()));
        return answerResponseDtoList;
    }
}
