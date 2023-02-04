package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.EmailService;
import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.SurveyResponseQuestionRequestDto;
import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.enums.Role;
import com.bilgeadam.basurveyapp.exceptions.custom.AlreadyAnsweredSurveyException;
import com.bilgeadam.basurveyapp.exceptions.custom.UserInsufficientAnswerException;
import com.bilgeadam.basurveyapp.repositories.ClassroomRepository;
import com.bilgeadam.basurveyapp.repositories.ResponseRepository;
import com.bilgeadam.basurveyapp.repositories.SurveyRepository;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {

    @InjectMocks
    private SurveyService surveyService;
    @Mock
    private SurveyRepository surveyRepository;
    @Mock
    private ClassroomRepository classroomRepository;
    @Mock
    private ResponseRepository responseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private JwtService jwtService;
    @Spy
    private PasswordEncoder passwordEncoder;
    @Test
    void responseSurveyQuestions_ShouldSaveResponses_WhenAllQuestionsAreAnsweredAndSurveyIsAnswerOnlyOnce(){
        //given
        User can = User.builder()
            .firstName("Can")
            .lastName("Demirhan")
            .email("can.demirhan@bilgeadam.com")
            .role(Role.STUDENT)
            .password(passwordEncoder.encode("BilgeAdam1234**"))
            .surveys(new ArrayList<Survey>(Arrays.asList()))
            .build();
        can.setOid(1L);

        Classroom java = Classroom.builder()
            .name("Java")
            .users(List.of(can))
            .build();

        Response response1 = Response.builder()
            .user(can)
            .responseString("A")
            .build();
        Response response2 = Response.builder()
            .user(can)
            .responseString("B")
            .build();
        response1.setOid(1L);
        response2.setOid(2L);

        Question question1 = Question.builder()
            .order(1)
            .questionType(QuestionType.builder()
                .questionType("Java-Core")
                .build())
            .questionString("What is JVM")
            .responses(new ArrayList<Response>())
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>())
            .build();
        question2.setOid(2L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(new Date())
            .endDate(cal.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1,question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList(java)))
            .users(new ArrayList<User>(Arrays.asList()))
            .build();
        javaSurvey.setOid(1L);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);

        //when
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getCredentials()).thenReturn(1L);
        when(surveyRepository.findActiveById(any())).thenReturn(Optional.of(javaSurvey));
        when(userRepository.findActiveById(any())).thenReturn(Optional.of(can));
        when(surveyRepository.save(any())).thenReturn(javaSurvey);

        //then
        SurveyResponseQuestionRequestDto dto = new SurveyResponseQuestionRequestDto();
        dto.setResponses(Map.of(1L,response1.getResponseString(),2L,response2.getResponseString()));
        Survey survey = surveyService.responseSurveyQuestions(1L, dto);
        assertEquals("Questions",2, survey.getQuestions().size());
        assertEquals("Questions", 2L, survey.getQuestions().stream().count());
        assertEquals("Answers of Question-1", 1L, survey.getQuestions().stream().filter(question -> question.getOid().equals(1L)).flatMap(question -> question.getResponses().stream()).count());
        assertEquals("Answers of Question-2", 1L, survey.getQuestions().stream().filter(question -> question.getOid().equals(2L)).flatMap(question -> question.getResponses().stream()).count());
        assertEquals("Users", 1L, survey.getUsers().stream().filter(user -> user.getOid().equals(1L)).count());

        verify(surveyRepository).save(any());
    }
    @Test
    void responseSurveyQuestions_ShouldThrowUserInsufficientAnswerException_WhenAllQuestionsAreNotAnswered(){
        //given
        User can = User.builder()
            .firstName("Can")
            .lastName("Demirhan")
            .email("can.demirhan@bilgeadam.com")
            .role(Role.STUDENT)
            .password(passwordEncoder.encode("BilgeAdam1234**"))
            .surveys(new ArrayList<Survey>(Arrays.asList()))
            .build();
        can.setOid(1L);

        Classroom java = Classroom.builder()
            .name("Java")
            .users(List.of(can))
            .build();

        Response response1 = Response.builder()
            .user(can)
            .responseString("A")
            .build();
        response1.setOid(1L);

        Question question1 = Question.builder()
            .order(1)
            .questionType(QuestionType.builder()
                .questionType("Java-Core")
                .build())
            .questionString("What is JVM")
            .responses(new ArrayList<Response>())
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>())
            .build();
        question2.setOid(2L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(new Date())
            .endDate(cal.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1,question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList(java)))
            .users(new ArrayList<User>(Arrays.asList()))
            .build();
        javaSurvey.setOid(1L);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);

        //when
        when(surveyRepository.findActiveById(any())).thenReturn(Optional.of(javaSurvey));

        //then
        SurveyResponseQuestionRequestDto dto = new SurveyResponseQuestionRequestDto();
        dto.setResponses(Map.of(1L,response1.getResponseString()));

        Throwable throwable = Assertions.catchThrowable(() -> surveyService.responseSurveyQuestions(1L,dto));
        assertTrue("Exception",throwable instanceof UserInsufficientAnswerException);
    }
    @Test
    void responseSurveyQuestions_ShouldThrow_SurveyIsAnsweredMoreThanOnce(){
        //given
        User can = User.builder()
            .firstName("Can")
            .lastName("Demirhan")
            .email("can.demirhan@bilgeadam.com")
            .role(Role.STUDENT)
            .password(passwordEncoder.encode("BilgeAdam1234**"))
            .surveys(new ArrayList<Survey>(Arrays.asList()))
            .build();
        can.setOid(1L);

        Classroom java = Classroom.builder()
            .name("Java")
            .users(List.of(can))
            .build();

        Response response1 = Response.builder()
            .user(can)
            .responseString("A")
            .build();
        Response response2 = Response.builder()
            .user(can)
            .responseString("B")
            .build();
        response1.setOid(1L);
        response2.setOid(2L);

        Question question1 = Question.builder()
            .order(1)
            .questionType(QuestionType.builder()
                .questionType("Java-Core")
                .build())
            .questionString("What is JVM")
            .responses(new ArrayList<Response>())
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>())
            .build();
        question2.setOid(2L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(new Date())
            .endDate(cal.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1,question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList(java)))
            .users(new ArrayList<User>(Arrays.asList(can)))
            .build();
        javaSurvey.setOid(1L);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);

        can.getSurveys().add(javaSurvey);

        //when
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getCredentials()).thenReturn(1L);
        when(surveyRepository.findActiveById(any())).thenReturn(Optional.of(javaSurvey));
        when(userRepository.findActiveById(any())).thenReturn(Optional.of(can));

        //then
        SurveyResponseQuestionRequestDto dto = new SurveyResponseQuestionRequestDto();
        dto.setResponses(Map.of(1L,response1.getResponseString(),2L,response2.getResponseString()));

        Throwable throwable = Assertions.catchThrowable(() -> surveyService.responseSurveyQuestions(1L,dto));
        assertTrue("Exception",throwable instanceof AlreadyAnsweredSurveyException);
    }
}
