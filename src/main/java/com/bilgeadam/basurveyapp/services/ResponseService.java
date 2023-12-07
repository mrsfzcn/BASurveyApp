package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.FindAllResponsesOfUserFromSurveyRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.request.ResponseRequestSaveDto;
import com.bilgeadam.basurveyapp.dto.request.SurveyUpdateResponseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.dto.response.WhoDidntAnswerSurveyStudentDto;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.exceptions.custom.*;
import com.bilgeadam.basurveyapp.mapper.ResponseMapper;
import com.bilgeadam.basurveyapp.repositories.ResponseRepository;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.stream.Collectors;

import static com.bilgeadam.basurveyapp.exceptions.ExceptionType.*;


@Service
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final QuestionService questionService;
    private final UserService userService;
    private final JwtService jwtService;
    private final StudentService studentService;
    private final SurveyService surveyService;
    private final StudentTagService studentTagService;

    public ResponseService(ResponseRepository responseRepository, QuestionService questionService, UserService userService
            , JwtService jwtService, StudentService studentService, @Lazy SurveyService surveyService, StudentTagService studentTagService) {
        this.responseRepository = responseRepository;
        this.questionService = questionService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.studentService = studentService;
        this.surveyService = surveyService;
        this.studentTagService = studentTagService;
    }

    //TODO BURASI AÇILACAK DATABASE'DE VERİYE İHTİYAÇ OLDUĞU İÇİN HERŞEY MANUEL ŞEKİLDE ATANDI !! DTO'NUN FIELDLARI Database'e gönderilecek cevaplara göre düzenlenecek !!
    public void  createResponse(ResponseRequestSaveDto responseRequestDto) {
//        User user = getAuthenticatedUser();
        User user = new User();
        user.setOid(responseRequestDto.getUserOid());
        Student byUserOid = studentService.findByUserOid(user.getOid());
        List<StudentResponseDto> studentList = studentService.getStudentList();
        StudentResponseDto studentResponseDto = new StudentResponseDto();
        for (StudentResponseDto students : studentList) {
            if (students.getOid() == byUserOid.getOid()){
                studentResponseDto.setStudentTags(students.getStudentTags());
                studentResponseDto.setOid(students.getOid());
                break;
            }
        }
        StudentTag studentTag = studentTagService.findByStudentTagName(studentResponseDto.getStudentTags().get(0).getTagString()).get();
        Question question = getActiveQuestionById(responseRequestDto.getQuestionOid());
        Response response = buildResponse(responseRequestDto, question, user);
        Survey survey = new Survey();
        survey.setOid(responseRequestDto.getSurveyOid());
        response.setSurvey(survey);
        response.setStudentTagOid(studentTag.getOid());
        responseRepository.save(response);
    }

    public void updateResponse(ResponseRequestDto responseRequestDto,Long id) {
        Response response = responseRepository.findActiveById(id)
                .orElseThrow(() -> new ResponseNotFoundException("There's an error while finding response"));

        response.setResponseString(responseRequestDto.getResponseString());
        responseRepository.save(response);
    }

    public AnswerResponseDto findById(Long responseOid) {
        Response response = responseRepository.findActiveById(responseOid)
                .orElseThrow(() -> new ResponseNotFoundException("There's an error while finding response"));
        return ResponseMapper.INSTANCE.toAnswerResponseDto(response);
    }

    public List<AnswerResponseDto> findAll() {
        ResponseMapper responseMapper = ResponseMapper.INSTANCE;
        List<Response> findAllList = responseRepository.findAllActive();
        return findAllList.stream()
                .map(responseMapper::toAnswerResponseDto).collect(Collectors.toList());
    }

    public Boolean deleteResponseById(Long responseOid) {
        Response response = responseRepository.findActiveById(responseOid)
                .orElseThrow(() -> new ResponseNotFoundException("There's an error while finding response"));
        return responseRepository.softDeleteById(response.getOid());
    }

    public Boolean saveAll(String token, List<ResponseRequestSaveDto> responseRequestSaveDtoList) {
        User user = getUserByEmailFromToken(token);
        Survey survey = getActiveSurveyByIdFromToken(token);
        if (responseRepository.isSurveyAnsweredByUser(user.getOid(), survey.getOid())) {
            return false;
        }
        Map<Long, Question> questionMap = new HashMap<>();
        responseRequestSaveDtoList.forEach(responseDto -> {
            Question question = questionMap.computeIfAbsent(responseDto.getQuestionOid(), this::getActiveQuestionById);
            Response response = createResponse(responseDto, question, survey, user);
            updateStudentAndSurvey(response, user, survey);
            saveResponse(response, question, survey);
        });
        return true;
    }

    public List<AnswerResponseDto> findAllResponsesOfUserFromSurvey(FindAllResponsesOfUserFromSurveyRequestDto dto) {
        ResponseMapper responseMapper = ResponseMapper.INSTANCE;
        User user = userService.findByEmail(dto.getUserEmail()).orElseThrow(() -> new UserDoesNotExistsException("User does not exist or deleted."));
        Survey survey = surveyService.findActiveById(dto.getSurveyOid()).orElseThrow(() -> new ResourceNotFoundException("Survey does not exist or deleted."));

        List<Long> surveyQuestionOidList = questionService.findSurveyQuestionOidList(survey.getOid());
        System.out.println(surveyQuestionOidList);

        List<Response> responses = responseRepository.findAllResponsesOfUserFromSurvey(dto.getUserEmail(), dto.getSurveyOid(), surveyQuestionOidList);
        List<AnswerResponseDto> answerResponseDtos = responses.stream()
                .map(responseMapper::toAnswerResponseDto)
                .collect(Collectors.toList());

        return answerResponseDtos;
    }

    public List<AnswerResponseDto> findAllResponsesOfUser(String userEmail) {
        ResponseMapper responseMapper = ResponseMapper.INSTANCE;
        User user = userService.findByEmail(userEmail).orElseThrow(() -> new UserDoesNotExistsException("User does not exist or has been deleted."));

        List<Response> responses = responseRepository.findAllResponsesOfUser(userEmail);
        List<AnswerResponseDto> answerResponseDtos = responses.stream()
                .map(responseMapper::toAnswerResponseDto)
                .collect(Collectors.toList());

        return answerResponseDtos;
    }

    public List<AnswerResponseDto> findResponseByStudentTag(Long studentTagOid) {
        User user = getAuthenticatedUser();

        List<Response> responseList = getAllResponsesFromActiveSurveys();
        if (responseList.isEmpty()) {
            throw new ResponseNotFoundException("No responses found.");
        }

        return responseList.stream()
                .distinct()
                .map(this::mapToAnswerResponseDto)
                .toList();
    }

    public Boolean updateStudentAnswers(Long surveyOid, SurveyUpdateResponseRequestDto dto) {
        User user = getAuthenticatedUser();
        Survey survey = getActiveSurveyById(surveyOid);
        List<Response> responseList = getAllResponsesForSurvey(survey);
        if (responseList.isEmpty()) {
            throw new ResponseNotFoundException("No responses found for the survey.");
        }

        responseList.stream()
                .filter(r -> r.getUser().getOid().equals(user.getOid()))
                .forEach(response -> response.setResponseString(dto.getUpdateResponseMap().get(response.getOid())));
        responseRepository.saveAll(responseList);
        return true;
    }
    private Response buildResponse(ResponseRequestSaveDto responseRequestDto, Question question, User user) {
        return Response.builder()
                .responseString(responseRequestDto.getResponseString())
                .question(question)
                .user(user)
                .build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("Authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        return userService.findActiveById(userOid)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist."));
    }

    private List<Response> getAllResponsesFromActiveSurveys() {
        List<Survey> surveyList = surveyService.findAllActive();
        if (surveyList.isEmpty()) {
            throw new SurveyNotFoundException("No active surveys found.");
        }
        return surveyList.stream()
                .flatMap(s -> s.getQuestions().stream())
                .flatMap(q -> q.getResponses().stream())
                .collect(Collectors.toList());
    }

    private AnswerResponseDto mapToAnswerResponseDto(Response response) {
        return AnswerResponseDto.builder()
                .responseString(response.getResponseString())
                .userOid(response.getUser().getOid())
                .questionOid(response.getQuestion().getOid())
                .surveyOid(response.getSurvey().getOid())
                .build();
    }

    private Survey getActiveSurveyById(Long surveyId) {
        return surveyService.findActiveById(surveyId)
                .orElseThrow(() -> new SurveyNotFoundException("Survey not found."));
    }

    private List<Response> getAllResponsesForSurvey(Survey survey) {
        return survey.getQuestions().stream()
                .flatMap(q -> q.getResponses().stream())
                .collect(Collectors.toList());
    }

    private User getUserByEmailFromToken(String token) {
        String userEmail = jwtService.extractEmail(token);
        return userService.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("No such user."));
    }

    private Survey getActiveSurveyByIdFromToken(String token) {
        Long surveyId = jwtService.extractSurveyOid(token);
        return surveyService.findActiveById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("No such survey."));
    }

    private Question getActiveQuestionById(Long questionId) {
        return questionService.findActiveById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found"));
    }

    private Response createResponse(ResponseRequestSaveDto responseDto, Question question, Survey survey, User user) {
        return Response.builder()
                .responseString(responseDto.getResponseString())
                .question(question)
                .survey(survey)
                .user(user)
                .build();
    }

    private void updateStudentAndSurvey(Response response, User user, Survey survey) {
        Optional<Student> student = studentService.findByUser(user);
        student.get().getSurveysAnswered().add(survey);
        survey.getStudentsWhoAnswered().add(student.get());
        survey.getStudentsWhoDidntAnswered().remove(student.get());
        studentService.save(student.get());
        surveyService.save(survey);
    }

    private void saveResponse(Response response, Question question, Survey survey) {
        responseRepository.save(response);
        question.getResponses().add(response);
        questionService.save(question);
        survey.getResponses().add(response);
        surveyService.save(survey);
    }




    // KULLANICIYA ATANAN ANKETLERİN CEVAPLARININ EXCELE EXPORT EDİLMESİNE YARAYAN METOD BAK !!
    public byte[] exportToExcel(Long studentTag) throws IOException {
//        List<Response> responses = responseRepository.findAll();

        List<Response> byStudentTagOid = responseRepository.findByStudentTagOid(studentTag);

        List<Long> studentTagCount = studentTagService.studentTagCount(studentTag);
        SurveyRegistration registrations = surveyService.findSurveyRegistrationByStudentTagOid(studentTag);

        String anketBaslangic = registrations.getStartDate().getDayOfMonth() + "/" +
                registrations.getStartDate().getMonthValue() + "/" +
                registrations.getStartDate().getYear() + " - " +
                registrations.getStartDate().getHour() + ":" +
                registrations.getStartDate().getMinute() + ":" +
                registrations.getStartDate().getSecond();

        String anketBitis = registrations.getEndDate().getDayOfMonth() + "/" +
                registrations.getEndDate().getMonthValue() + "/" +
                registrations.getEndDate().getYear() + " - " +
                registrations.getEndDate().getHour() + ":" +
                registrations.getEndDate().getMinute() + ":" +
                registrations.getEndDate().getSecond();

        if(studentTagCount.isEmpty()) throw new StudentTagNotFoundException(STUDENT_TAG_NOT_FOUND.getMessage());
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Öğrenci Cevapları");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("ANKET SIRASI");
        headerRow.createCell(2).setCellValue("BAŞLAMA TARİHİ");
        headerRow.createCell(3).setCellValue("BİTİŞ TARİHİ");
        headerRow.createCell(4).setCellValue("ÖĞRENCİ EMAIL");
        headerRow.createCell(5).setCellValue("ÖĞRENCİ İSİM");
        headerRow.createCell(6).setCellValue("ÖĞRENCİ SOYİSİM");
        headerRow.createCell(7).setCellValue("SORU");
        headerRow.createCell(8).setCellValue("CEVAP");
        int count = 1;
        int rowNum = 1;
        Map<Long,Integer>map = new HashMap<>();
        for(Long a : studentTagCount){
            map.put(a,count++);
        }
        String surveyTitle = "";

            for (Response response : byStudentTagOid) {
                System.out.println(response);
                surveyTitle = response.getSurvey().getSurveyTitle();
                if (!surveyTitle.equals("")){
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(rowNum-1);
                    row.createCell(1).setCellValue("Sıra Düzenlenecek");
                    row.createCell(2).setCellValue(anketBaslangic);
                    row.createCell(3).setCellValue(anketBitis);
                    row.createCell(4).setCellValue(response.getUser().getEmail());
                    row.createCell(5).setCellValue(response.getUser().getFirstName());
                    row.createCell(6).setCellValue(response.getUser().getLastName());
                    row.createCell(7).setCellValue(response.getQuestion().getQuestionString());
                    row.createCell(8).setCellValue(response.getResponseString());
                    count++;
                }
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return outputStream.toByteArray();


    }


    // studenTag'e atanan anketlerin öğrenciler tarafından cevaplanma oranını gösteren metod
    public Double  surveyResponseRate(Long surveyid,Long studentTagOid) {
        List<Long> studentTags= surveyService.findTotalStudentBySurveyOid(surveyid,studentTagOid);
        if (studentTags.isEmpty()) throw new StudentTagNotFoundException(STUDENT_TAG_NOT_FOUND.getMessage());
        Integer students = responseRepository.findByStudentAnsweredSurvey(surveyid,studentTagOid);

        System.out.println(studentTags.size());
        System.out.println(studentTags+ " " + students);
        Double result = (double) (students*100)/studentTags.size();
        return result;
    }

    public List<WhoDidntAnswerSurveyStudentDto> whoNeedToComplete(Long surveyid){
        Optional<Survey>surveyOptional = surveyService.findActiveById(surveyid);
        List<WhoDidntAnswerSurveyStudentDto>whoDidntAnswerSurveyStudentDtos = new ArrayList<>();
        surveyOptional.get().getStudentsWhoDidntAnswered().forEach(x->{
            whoDidntAnswerSurveyStudentDtos.add(WhoDidntAnswerSurveyStudentDto.builder()
                            .firstname(x.getUser().getFirstName())
                            .lastname(x.getUser().getLastName())
                    .build());
        });
        return whoDidntAnswerSurveyStudentDtos;

    }

    public List<String>surveyResponseRateName(Long surveyid,Long studentTagOid){
        List<String> studentName = surveyService.findStudentNameBySurveyOid(surveyid,studentTagOid);

        return studentName;
    }
    public void surveyIsAnswered(Long survey0id){
        int count = responseRepository.findByStudentAnsweredSurvey(survey0id);
        if(count > 0){
            throw new SurveyAnsweredException(SURVEY_ANSWERED.getMessage());
        }
    }

    public void saveAll(List<Response> updatedResponses) {
        responseRepository.saveAll(updatedResponses);
    }

    public Set<Response> findResponsesByUserOidAndSurveyOid(Long oid, Long surveyOid) {
        return responseRepository.findResponsesByUserOidAndSurveyOid(oid,surveyOid);
    }

    public Set<Response> findBySurveyAndUser(Survey survey, User user) {
        return responseRepository.findBySurveyAndUser(survey,user);
    }

    public List<Response> findListByUser(User user) {
        return responseRepository.findListByUser(user);
    }

    public Set<Response> findSetByUser(User user) {
        return responseRepository.findSetByUser(user);
    }



}
