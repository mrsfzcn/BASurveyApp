package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.response.QuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTagResponseDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.repositories.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Muhammed Furkan Türkmen
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionServiceTest {

    private QuestionService questionService;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private QuestionTypeService questionTypeService;
    @Mock
    private SurveyService surveyService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private JwtService jwtService;
    @Mock
    private QuestionTagService questionTagService;


    @BeforeAll
    public void Init(){
        MockitoAnnotations.openMocks(this);
        questionService= new QuestionService(questionRepository,questionTypeService,surveyService
                ,trainerService,jwtService,questionTagService);
    }
    @Test
    public void findAllSurveyQuestions() {
        when(jwtService.isSurveyEmailTokenValid("token")).thenReturn(false);
        when(jwtService.extractSurveyOid("token")).thenReturn(1L);

        Survey survey = Survey.builder()
                .questions(List.of(Question.builder()
                        .questionString("soru1")
                                .questionTag(Set.of())
                                .questionType(QuestionType.builder().build())
                        .build()))

                .build();
        when(surveyService.findActiveById(1L)).thenReturn(Optional.ofNullable(survey));
        List<Question> questions = survey.getQuestions();
        Set<Long> uniqueQuestionIds = new HashSet<>();
        List<QuestionResponseDto> questionsDto = new ArrayList<>();

        for (Question question : questions) {
            if (uniqueQuestionIds.add(question.getOid())) {
                questionsDto.add(QuestionResponseDto.builder()
                        .questionOid(question.getOid())
                        .questionString(question.getQuestionString())
//                        .order(question.getOrder())
                        .questionTags(question.getQuestionTag().stream().map(
                                questionTag -> QuestionTagResponseDto.builder()
                                        .oid(questionTag.getOid())
                                        .tagString(questionTag.getTagString())
                                        .build()).collect(Collectors.toList()))
                        .questionTypeOid(question.getQuestionType().getOid())
                        .build());
            }
        }
        assertNotNull(questionService.findAllSurveyQuestions("token"));
    }


    }
