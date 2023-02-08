//package com.bilgeadam.basurveyapp.dto.response;
//
//import com.bilgeadam.basurveyapp.entity.*;
//import com.bilgeadam.basurveyapp.entity.enums.Role;
//import com.bilgeadam.basurveyapp.mapper.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Spy;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {
//    ClassroomMapperImpl.class,
//    QuestionMapperImpl.class,
//    UserMapperImpl.class})
//class SurveyResponseDtoTest {
//
//    @InjectMocks
//    private SurveyMapperImpl surveyMapperImpl;
//    @Spy
//    private ClassroomMapper classroomMapper = ClassroomMapper.CLASSROOM_MAPPER;
//    @Spy
//    private QuestionMapper questionMapper = QuestionMapper.QUESTION_MAPPER;
//    @Spy
//    private UserMapper userMapper = UserMapper.USER_MAPPER;
//
//
//    @Test
//    void convertSurveyToSurveyResponse() {
//        User can = User.builder()
//            .firstName("Can")
//            .lastName("Demirhan")
//            .email("can.demirhan@bilgeadam.com")
//            .role(Role.STUDENT)
//            .password("BilgeAdam1234**")
//            .surveys(new ArrayList<Survey>(Arrays.asList()))
//            .build();
//        can.setOid(1L);
//
//        Classroom java = Classroom.builder()
//            .name("Java")
//            .users(List.of(can))
//            .build();
//        java.setOid(1L);
//
//        Response response1 = Response.builder()
//            .user(can)
//            .responseString("A")
//            .build();
//        Response response2 = Response.builder()
//            .user(can)
//            .responseString("B")
//            .build();
//        response1.setOid(1L);
//        response2.setOid(2L);
//
//        Question question1 = Question.builder()
//            .order(1)
//            .questionType(QuestionType.builder()
//                .questionType("Java-Core")
//                .build())
//            .questionString("What is JVM")
//            .responses(new ArrayList<Response>(Arrays.asList(response2)))
//            .build();
//        question1.setOid(1L);
//        Question question2 = Question.builder()
//            .order(2)
//            .questionType(QuestionType.builder()
//                .questionType("Java-Effective")
//                .build())
//            .questionString("What is Stream Api")
//            .responses(new ArrayList<Response>(Arrays.asList(response2)))
//            .build();
//        question2.setOid(2L);
//        response1.setQuestion(question1);
//        response2.setQuestion(question2);
//
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, 7);
//
//        Survey javaSurvey = Survey.builder()
//            .surveyTitle("Java Survey")
//            .courseTopic("Java")
//            .questions(new ArrayList<Question>(Arrays.asList(question1, question2)))
//            .classrooms(new ArrayList<Classroom>(Arrays.asList(java)))
//            .users(new ArrayList<User>(Arrays.asList(can)))
//            .build();
//        javaSurvey.setOid(1L);
//
//        question1.setSurvey(javaSurvey);
//        question2.setSurvey(javaSurvey);
//
//        can.getSurveys().add(javaSurvey);
//
//        SurveyResponseDto surveyResponseDto = surveyMapperImpl.toSurveyResponseDto(javaSurvey);
//        assertEquals(1, surveyResponseDto.getSurveyClassroomResponseDtoList().size());
//    }
//}