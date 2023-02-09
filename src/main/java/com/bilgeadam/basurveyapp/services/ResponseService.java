package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.FindAllResponsesOfUserRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestSaveDto;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.enums.Role;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.UserDoesNotExistsException;
import com.bilgeadam.basurveyapp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SurveyRepository surveyRepository;
    private final ClassroomRepository classroomRepository;
    public void createResponse(ResponseRequestSaveDto responseRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        userRepository.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        Response response = Response.builder()
                .responseString(responseRequestDto.getResponseString())
                .question(questionRepository
                        .findActiveById(responseRequestDto
                                .getQuestionOid()).orElseThrow(()->new ResourceNotFoundException("question not found")))
                .user(userRepository.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist")))
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
        Optional<Response> response = responseRepository.findActiveById(responseOid);
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

    public Boolean saveAll(String token, List<ResponseRequestSaveDto> responseRequestSaveDtoList) {
        User user = userRepository.findByEmail(jwtService.extractEmail(token)).orElseThrow(() -> new ResourceNotFoundException("No such user."));// tokendan gelen id var gibi kabul edildi.Mail üzerinden yapıdı.

        if (!responseRepository.isSurveyAnsweredByUser(user.getOid())) {
            responseRequestSaveDtoList.forEach(response -> { //gelenleri listeye kaydetmek için for each kullanıldı.
                responseRepository.save(Response.builder()
                        .user(user) //tokendan gelen userı, teker teker bütün cevaplara kaydetmiş oluyoruz(bu user bunu cevapladı.).
                        .responseString(response.getResponseString())
                        .question(questionRepository.findActiveById(response.getQuestionOid()).orElseThrow(() -> new QuestionNotFoundException("Question not found"))) //orElseThrow() get yapıyor.boşsa exception atıyor. içine kendi exeption atar. a
                        .build());
            });
            return true;
        } else {
            return false;
        }
    }

    public List<AnswerResponseDto> findAllResponsesOfUserFromSurvey(FindAllResponsesOfUserRequestDto dto) {
        userRepository.findByEmail(dto.getUserEmail()).orElseThrow(() -> new UserDoesNotExistsException("User does not exists or deleted."));
        surveyRepository.findActiveById(dto.getSurveyOid()).orElseThrow(() -> new ResourceNotFoundException("Survey does not exists or deleted."));
        return responseRepository
                .findAllResponsesOfUserFromSurvey(dto.getUserEmail(), questionRepository.findSurveyQuestionOidList(dto.getSurveyOid()))
                .stream()
                .map(response -> AnswerResponseDto.builder()
                        .responseString(response.getResponseString())
                        .questionOid(response.getQuestion().getOid())
                        .userOid(response.getUser().getOid())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<AnswerResponseDto> findResponseByClassroomOid(Long classroomOid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        User user = userRepository.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        if (user.getRole() == Role.ASSISTANT_TRAINER || user.getRole() == Role.MASTER_TRAINER) {
            Classroom classroom = classroomRepository.findActiveById(classroomOid).orElseThrow(() -> new ResourceNotFoundException("Classroom does not exist"));
            if (!classroom.getUsers().contains(user)) {
                throw new AccessDeniedException("authentication failure.");
            }
        }
        Optional<Classroom> classroomOptional = classroomRepository.findActiveById(classroomOid);
        if (classroomOptional.isEmpty()) {
            throw new ResourceNotFoundException("Classroom is not found.");
        }
        List<Survey> surveyList = surveyRepository.findAllActive();
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
