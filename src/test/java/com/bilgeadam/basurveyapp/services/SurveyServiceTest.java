package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.EmailService;
import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.SurveyResponseQuestionRequestDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateResponseRequestDto;
import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.enums.Role;
import com.bilgeadam.basurveyapp.exceptions.custom.AlreadyAnsweredSurveyException;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionsAndResponsesDoesNotMatchException;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
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
import static org.mockito.ArgumentMatchers.anyList;
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
    @Spy
    private EmailService emailService;
    @Mock
    private JwtService jwtService;
    @Spy
    private PasswordEncoder passwordEncoder;

    @Test
    void responseSurveyQuestions_ShouldSaveResponses_WhenAllQuestionsAreAnsweredAndSurveyIsAnswerOnlyOnce() {
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
        java.setOid(1L);

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
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
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
        dto.setCreateResponses(Map.of(1L, response1.getResponseString(), 2L, response2.getResponseString()));
        Survey survey = surveyService.responseSurveyQuestions(1L, dto);
        assertEquals("Questions", 2, survey.getQuestions().size());
        assertEquals("Questions", 2L, survey.getQuestions().stream().count());
        assertEquals("Answers of Question-1", 1L, survey.getQuestions().stream().filter(question -> question.getOid().equals(1L)).flatMap(question -> question.getResponses().stream()).count());
        assertEquals("Answers of Question-2", 1L, survey.getQuestions().stream().filter(question -> question.getOid().equals(2L)).flatMap(question -> question.getResponses().stream()).count());
        assertEquals("Users", 1L, survey.getUsers().stream().filter(user -> user.getOid().equals(1L)).count());

        verify(surveyRepository).save(any());
    }

    @Test
    void responseSurveyQuestions_ShouldThrowResourceNotFoundException_WhenSurveyIsNotValid() {
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
        java.setOid(1L);

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
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList(java)))
            .users(new ArrayList<User>(Arrays.asList()))
            .build();
        javaSurvey.setOid(1L);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);

        //when
        when(surveyRepository.findActiveById(5L)).thenReturn(Optional.empty());

        //then
        SurveyResponseQuestionRequestDto dto = new SurveyResponseQuestionRequestDto();
        dto.setCreateResponses(Map.of(1L, response1.getResponseString(), 2L, response2.getResponseString()));

        Throwable throwable = Assertions.catchThrowable(() -> surveyService.responseSurveyQuestions(5L, dto));
        assertTrue("Exception", throwable instanceof ResourceNotFoundException);
    }

    @Test
    void responseSurveyQuestions_ShouldThrowUserInsufficientAnswerException_WhenAllQuestionsAreNotAnswered() {
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
        java.setOid(1L);

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
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
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
        dto.setCreateResponses(Map.of(1L, response1.getResponseString()));

        Throwable throwable = Assertions.catchThrowable(() -> surveyService.responseSurveyQuestions(1L, dto));
        assertTrue("Exception", throwable instanceof UserInsufficientAnswerException);
    }

    @Test
    void responseSurveyQuestions_ShouldThrowAlreadyAnsweredSurveyException_SurveyIsAnsweredMoreThanOnce() {
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
        java.setOid(1L);

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
            .responses(new ArrayList<Response>(Arrays.asList(response2)))
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>(Arrays.asList(response2)))
            .build();
        question2.setOid(2L);
        response1.setQuestion(question1);
        response2.setQuestion(question2);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(new Date())
            .endDate(cal.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
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
        dto.setCreateResponses(Map.of(1L, response1.getResponseString(), 2L, response2.getResponseString()));

        Throwable throwable = Assertions.catchThrowable(() -> surveyService.responseSurveyQuestions(1L, dto));
        assertTrue("Exception", throwable instanceof AlreadyAnsweredSurveyException);
    }

    @Test
    void updateSurveyAnswers_ShouldUpdateResponses_WhenSurveyIsValidAndSurveyQuestionsAndResponsesAreMatchedAndSurveyIsNotExpired() {
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
        java.setOid(1L);

        Response response1 = Response.builder()
            .user(can)
            .responseString("A")
            .build();
        response1.setOid(1L);
        Response response2 = Response.builder()
            .user(can)
            .responseString("B")
            .build();
        response2.setOid(2L);

        Question question1 = Question.builder()
            .order(1)
            .questionType(QuestionType.builder()
                .questionType("Java-Core")
                .build())
            .questionString("What is JVM")
            .responses(new ArrayList<Response>(Arrays.asList(response1)))
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>(Arrays.asList(response2)))
            .build();
        question2.setOid(2L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(new Date())
            .endDate(cal.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList(java)))
            .users(new ArrayList<User>(Arrays.asList(can)))
            .build();
        javaSurvey.setOid(1L);

        can.getSurveys().add(javaSurvey);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);

        //when
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getCredentials()).thenReturn(1L);
        when(surveyRepository.findActiveById(any())).thenReturn(Optional.of(javaSurvey));
        when(responseRepository.saveAll(any())).thenReturn(List.of(response1, response2));

        //then
        SurveyUpdateResponseRequestDto dto = new SurveyUpdateResponseRequestDto();
        dto.setUpdateResponseMap(Map.of(1L, "X", 2L, "Y"));
        Survey survey = surveyService.updateSurveyAnswers(1L, dto);
        assertEquals("Answers of Question-1", "X", survey.getQuestions().stream().filter(question -> question.getOid().equals(1L)).flatMap(question -> question.getResponses().stream()).filter((response -> response.getOid().equals(1L))).findFirst().get().getResponseString());
        assertEquals("Answers of Question-2", "Y", survey.getQuestions().stream().filter(question -> question.getOid().equals(2L)).flatMap(question -> question.getResponses().stream()).filter((response -> response.getOid().equals(2L))).findFirst().get().getResponseString());

        verify(responseRepository).saveAll(anyList());
    }

    @Test
    void updateSurveyAnswers_ShouldThrowResourceNotFoundException_SurveyIsNotValid() {
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
        java.setOid(1L);

        Response response1 = Response.builder()
            .user(can)
            .responseString("A")
            .build();
        response1.setOid(1L);
        Response response2 = Response.builder()
            .user(can)
            .responseString("B")
            .build();
        response2.setOid(2L);

        Question question1 = Question.builder()
            .order(1)
            .questionType(QuestionType.builder()
                .questionType("Java-Core")
                .build())
            .questionString("What is JVM")
            .responses(new ArrayList<Response>(Arrays.asList(response1)))
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>(Arrays.asList(response2)))
            .build();
        question2.setOid(2L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(new Date())
            .endDate(cal.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList(java)))
            .users(new ArrayList<User>(Arrays.asList(can)))
            .build();
        javaSurvey.setOid(1L);

        can.getSurveys().add(javaSurvey);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);

        //when
        when(surveyRepository.findActiveById(2L)).thenReturn(Optional.empty());

        //then
        SurveyUpdateResponseRequestDto dto = new SurveyUpdateResponseRequestDto();
        dto.setUpdateResponseMap(Map.of(1L, "X", 2L, "Y"));

        Throwable throwable = Assertions.catchThrowable(() -> surveyService.updateSurveyAnswers(2L, dto));
        assertTrue("Exception", throwable instanceof ResourceNotFoundException);
    }

    @Test
    void updateSurveyAnswers_ShouldThrowQuestionsAndResponsesDoesNotMatchException_WhenQuestionsAndResponsesDontMatch() {

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
        java.setOid(1L);

        Response response1 = Response.builder()
            .user(can)
            .responseString("A")
            .build();
        response1.setOid(1L);
        Response response2 = Response.builder()
            .user(can)
            .responseString("B")
            .build();
        response2.setOid(2L);

        Question question1 = Question.builder()
            .order(1)
            .questionType(QuestionType.builder()
                .questionType("Java-Core")
                .build())
            .questionString("What is JVM")
            .responses(new ArrayList<Response>(Arrays.asList(response1)))
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>(Arrays.asList(response2)))
            .build();
        question2.setOid(2L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(new Date())
            .endDate(cal.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList(java)))
            .users(new ArrayList<User>(Arrays.asList(can)))
            .build();
        javaSurvey.setOid(1L);

        can.getSurveys().add(javaSurvey);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);


        //when
        when(surveyRepository.findActiveById(any())).thenReturn(Optional.of(javaSurvey));

        //then
        SurveyUpdateResponseRequestDto dto = new SurveyUpdateResponseRequestDto();
        dto.setUpdateResponseMap(Map.of(1L, "X", 4L, "Y"));

        Throwable throwable = Assertions.catchThrowable(() -> surveyService.updateSurveyAnswers(1L, dto));
        assertTrue("Exception", throwable instanceof QuestionsAndResponsesDoesNotMatchException);
    }

    @Test
    void updateSurveyAnswers_ShouldThrowResourceNotFoundException_WhenSurveyIsExpired() {

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
        java.setOid(1L);

        Response response1 = Response.builder()
            .user(can)
            .responseString("A")
            .build();
        response1.setOid(1L);
        Response response2 = Response.builder()
            .user(can)
            .responseString("B")
            .build();
        response2.setOid(2L);

        Question question1 = Question.builder()
            .order(1)
            .questionType(QuestionType.builder()
                .questionType("Java-Core")
                .build())
            .questionString("What is JVM")
            .responses(new ArrayList<Response>(Arrays.asList(response1)))
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>(Arrays.asList(response2)))
            .build();
        question2.setOid(2L);

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.add(Calendar.DATE, -10);
        Calendar calendarEnd = Calendar.getInstance();
        calendarStart.add(Calendar.DATE, -2);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(calendarStart.getTime())
            .endDate(calendarEnd.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList(java)))
            .users(new ArrayList<User>(Arrays.asList(can)))
            .build();
        javaSurvey.setOid(1L);

        can.getSurveys().add(javaSurvey);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);

        //when
        when(surveyRepository.findActiveById(any())).thenReturn(Optional.of(javaSurvey));

        //then
        SurveyUpdateResponseRequestDto dto = new SurveyUpdateResponseRequestDto();
        dto.setUpdateResponseMap(Map.of(1L, "X", 4L, "Y"));

        Throwable throwable = Assertions.catchThrowable(() -> surveyService.updateSurveyAnswers(1L, dto));
        assertTrue("Exception", throwable instanceof ResourceNotFoundException);
    }

    @Test
    void assignSurveyToClassroom_ShouldAssign_WhenSurveyIsValidAndClassroomIsValidAndSurveyIsNotExpired() {
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
        java.setOid(1L);


        Question question1 = Question.builder()
            .order(1)
            .questionType(QuestionType.builder()
                .questionType("Java-Core")
                .build())
            .questionString("What is JVM")
            .responses(new ArrayList<Response>(Arrays.asList()))
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>(Arrays.asList()))
            .build();
        question2.setOid(2L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(new Date())
            .endDate(cal.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList()))
            .users(new ArrayList<User>(Arrays.asList(can)))
            .build();
        javaSurvey.setOid(1L);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);

        //when
        when(surveyRepository.findActiveById(any())).thenReturn(Optional.of(javaSurvey));
        when(classroomRepository.findActiveById(any())).thenReturn(Optional.of(java));
        when(surveyRepository.save(any())).thenReturn(javaSurvey);

        //then
        Survey survey = surveyService.assignSurveyToClassroom(1L, 1L);
        assertEquals("Classroom", "Java", survey.getClassrooms().stream().filter(classroom -> classroom.getOid().equals(1L)).findFirst().get().getName());

        verify(surveyRepository).save(any());
    }

    @Test
    void assignSurveyToClassroom_ShouldThrowResourceNotFoundException_SurveyIsNotValid() {
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
        java.setOid(1L);


        Question question1 = Question.builder()
            .order(1)
            .questionType(QuestionType.builder()
                .questionType("Java-Core")
                .build())
            .questionString("What is JVM")
            .responses(new ArrayList<Response>(Arrays.asList()))
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>(Arrays.asList()))
            .build();
        question2.setOid(2L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(new Date())
            .endDate(cal.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList()))
            .users(new ArrayList<User>(Arrays.asList(can)))
            .build();
        javaSurvey.setOid(1L);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);

        //when
        when(surveyRepository.findActiveById(2L)).thenReturn(Optional.empty());

        //then
        Throwable throwable = Assertions.catchThrowable(() -> surveyService.assignSurveyToClassroom(2L, 1L));
        assertTrue("Exception", throwable instanceof ResourceNotFoundException);
    }

    @Test
    void assignSurveyToClassroom_ShouldThrowResourceNotFoundException_ClassroomIsNotValid() {
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
        java.setOid(1L);


        Question question1 = Question.builder()
            .order(1)
            .questionType(QuestionType.builder()
                .questionType("Java-Core")
                .build())
            .questionString("What is JVM")
            .responses(new ArrayList<Response>(Arrays.asList()))
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>(Arrays.asList()))
            .build();
        question2.setOid(2L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(new Date())
            .endDate(cal.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList()))
            .users(new ArrayList<User>(Arrays.asList(can)))
            .build();
        javaSurvey.setOid(1L);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);

        //when
        when(surveyRepository.findActiveById(1L)).thenReturn(Optional.of(javaSurvey));
        when(classroomRepository.findActiveById(2L)).thenReturn(Optional.empty());

        //then
        Throwable throwable = Assertions.catchThrowable(() -> surveyService.assignSurveyToClassroom(1L, 2L));
        assertTrue("Exception", throwable instanceof ResourceNotFoundException);
    }

    @Test
    void assignSurveyToClassroom_ShouldThrowResourceNotFoundException_SurveyIsExpired() {
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
        java.setOid(1L);


        Question question1 = Question.builder()
            .order(1)
            .questionType(QuestionType.builder()
                .questionType("Java-Core")
                .build())
            .questionString("What is JVM")
            .responses(new ArrayList<Response>(Arrays.asList()))
            .build();
        question1.setOid(1L);
        Question question2 = Question.builder()
            .order(2)
            .questionType(QuestionType.builder()
                .questionType("Java-Effective")
                .build())
            .questionString("What is Stream Api")
            .responses(new ArrayList<Response>(Arrays.asList()))
            .build();
        question2.setOid(2L);

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.add(Calendar.DATE, -10);
        Calendar calendarEnd = Calendar.getInstance();
        calendarStart.add(Calendar.DATE, -2);

        Survey javaSurvey = Survey.builder()
            .surveyTitle("Java Survey")
            .courseTopic("Java")
            .startDate(calendarStart.getTime())
            .endDate(calendarEnd.getTime())
            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
            .classrooms(new ArrayList<Classroom>(Arrays.asList()))
            .users(new ArrayList<User>(Arrays.asList(can)))
            .build();
        javaSurvey.setOid(1L);

        question1.setSurvey(javaSurvey);
        question2.setSurvey(javaSurvey);

        //when
        when(surveyRepository.findActiveById(1L)).thenReturn(Optional.of(javaSurvey));

        //then
        Throwable throwable = Assertions.catchThrowable(() -> surveyService.assignSurveyToClassroom(1L, 2L));
        assertTrue("Exception", throwable instanceof ResourceNotFoundException);
    }
}
