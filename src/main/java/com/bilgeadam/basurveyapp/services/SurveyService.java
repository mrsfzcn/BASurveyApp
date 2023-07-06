package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.EmailService;
import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS;
import com.bilgeadam.basurveyapp.converter.SurveyServiceConverter;
import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.entity.tags.SurveyTag;
import com.bilgeadam.basurveyapp.exceptions.custom.*;
import com.bilgeadam.basurveyapp.mapper.SurveyMapper;
import com.bilgeadam.basurveyapp.repositories.*;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.bilgeadam.basurveyapp.mapper.SurveyMapper.INSTANCE;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final ResponseRepository responseRepository;
    private final QuestionRepository questionRepository;
    private final SurveyTagRepository surveyTagRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final SurveyRegistrationRepository surveyRegistrationRepository;
    private final RoleService roleService;
    private final StudentTagService studentTagService;
    private final StudentService studentService;
    private final TrainerService trainerService;

    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    /**
     * cron = "0 30 9 * * MON-FRI"
     * cron = "*1 * * * * *" -> everySecond
     * cron = "0 *1 * * * *" -> everyMinute
     *
     * @throws MessagingException
     */
//    private List<Student> getStudentsByStudentTag(StudentTag studentTag) {
//
//        return studentTagService.getStudentsByStudentTag(studentTag);
//    }
    @Async
    @Scheduled(cron = "0 30 9 * * MON-FRI")
    public void initiateSurveys() throws MessagingException {
        List<SurveyRegistration> surveyRegistrations = surveyRegistrationRepository.findAllByEndDateAfter(LocalDateTime.now());

        if (surveyRegistrations.size() != 0) {

            Map<String, String> emailTokenMap = new HashMap<>();

//TODO student listesinden student tag classroom tage eşit olanları student listesi olarak dönecek
            surveyRegistrations
                    .parallelStream()
                    .filter(sR -> sR.getStartDate().toLocalDate().equals(LocalDate.now()))
                    .forEach(sR -> studentTagService.getStudentsByStudentTag(sR.getStudentTag())
                            .stream()
                            .forEach(student -> emailTokenMap.put(
                                    student.getUser().getEmail(),
                                    jwtService.generateSurveyEmailToken(
                                            sR.getSurvey().getOid(),
                                            sR.getStudentTag().getOid(),
                                            student.getUser().getEmail(),
                                            (int) ChronoUnit.DAYS.between(sR.getEndDate(), sR.getStartDate())))));

            emailService.sendSurveyMail(emailTokenMap);
        }
//        logger.info("Scheduled - " + Thread.currentThread().getId() + " - " + LocalDateTime.now());
    }

    public List<SurveySimpleResponseDto> getSurveyList() {
        List<Survey> surveys = surveyRepository.findAllActive();
        return INSTANCE.toSurveySimpleResponseDto(surveys);
    }

    public Page<Survey> getSurveyPage(Pageable pageable) {

        return surveyRepository.findAllActive(pageable);
    }

    public Boolean create(SurveyCreateRequestDto dto) {

        try {
            Survey survey = INSTANCE.toSurvey(dto);
            surveyRepository.save(survey);
        } catch (Exception e) {
            throw new SurveyTitleAlreadyExistException("This survey title already in use.");
        }
        return true;
    }

    public Survey update(Long surveyId, SurveyUpdateRequestDto dto) {

        Optional<Survey> surveyToBeUpdated = surveyRepository.findActiveById(surveyId);
        if (surveyToBeUpdated.isEmpty()) {
            throw new SurveyNotFoundException("Survey is not found");
        }
        surveyToBeUpdated.get().setSurveyTitle(dto.getSurveyTitle());
        return surveyRepository.save(surveyToBeUpdated.get());
    }

    public boolean delete(Long surveyId) {

        Optional<Survey> surveyToBeDeleted = surveyRepository.findActiveById(surveyId);
        if (surveyToBeDeleted.isEmpty()) {
            throw new SurveyNotFoundException("Survey is not found");
        }
        return surveyRepository.softDeleteById(surveyToBeDeleted.get().getOid());
    }

    public SurveyResponseDto findByOid(Long surveyId) {

        Optional<Survey> surveyById = surveyRepository.findActiveById(surveyId);
        Set<Question> surveyQuestions = surveyById.get().getQuestions().stream().collect(Collectors.toSet());
        surveyById.get().setQuestions(surveyQuestions.stream().collect(Collectors.toList()));
        if (surveyById.isEmpty()) {
            throw new SurveyNotFoundException("Survey is not found");
        }
        List<SurveyStudentResponseDto> studentsWhoNotAnswered = new ArrayList<>();
        Set<Student> whoAnsweredStudents = surveyById.get().getStudentsWhoAnswered();

        Map<Student, Integer> hashMap = new HashMap<>();
        for(Student key : whoAnsweredStudents){
            hashMap.put(key,0);
        }

        surveyById.get().getSurveyRegistrations().parallelStream()
                .forEach(sR -> studentTagService.getStudentsByStudentTag(sR.getStudentTag()).stream()
                        .forEach(student -> {
                            if (!hashMap.containsKey(student)){
                                studentsWhoNotAnswered.add(SurveyStudentResponseDto.builder()
                                        .email(student.getUser().getEmail())
                                        .firstName(student.getUser().getFirstName())
                                        .lastName(student.getUser().getLastName()).build()
                        );
                    }
                }));
        SurveyResponseDto surveyResponseDto = SurveyMapper.INSTANCE.toSurveyResponseDto(surveyById.get());
        surveyResponseDto.setStudentsWhoNotAnswered(studentsWhoNotAnswered);
        return surveyResponseDto;
    }

    //TODO Bakılması lazım. Gereksiz olabilir.
    public Boolean responseSurveyQuestions(String token, List<SurveyResponseQuestionRequestDto> dtoList, HttpServletRequest request) {

        if (jwtService.isSurveyEmailTokenValid(token)) {
            throw new AccessDeniedException("Invalid token");
        }
        Long studentTagOid = jwtService.extractStudentTagOid(token);
        Long surveyOid = jwtService.extractSurveyOid(token);
        Survey survey = surveyRepository.findActiveById(surveyOid)
                .orElseThrow(() -> new SurveyNotFoundException("Survey is not Found."));
        StudentTag studentTag = studentTagService.findActiveById(studentTagOid).orElseThrow(
                () -> new SurveyTagNotFoundException("StudentTag is not Found."));

        SurveyRegistration surveyRegistration = survey.getSurveyRegistrations()
                .parallelStream()
                .filter(sR -> sR.getSurvey().getOid().equals(survey.getOid()))
                .filter(sR -> sR.getStudentTag().getOid().equals(studentTagOid))
                .findAny()
                .orElseThrow(() -> new SurveyHasNotAssignedInToClassroomException("Survey has not assigned to the classroom."));

        LocalDate now = LocalDateTime.now().toLocalDate();
        LocalDate surveyStartDate = surveyRegistration.getStartDate().toLocalDate();
        LocalDate surveyEndDate = surveyRegistration.getEndDate().toLocalDate();

        if (now.isBefore(surveyStartDate)) {
            throw new SurveyNotInitiatedException("Survey has not initiated.");
        } else if (now.isAfter(surveyEndDate)) {
            throw new SurveyExpiredException("Survey is Expired");
        }

        if (Boolean.FALSE.equals(crossCheckSurveyQuestionsAndCreateResponses(survey, dtoList))) {
            throw new UserInsufficientAnswerException("User must response all the questions.");
        }

        String userEmail = jwtService.extractEmail(token);
        User currentUser = userService.findByEmail(userEmail)
                .orElseThrow(() -> new UserDoesNotExistsException("User is not found"));

        // authentication ******
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                currentUser,
                currentUser.getOid(),
                currentUser.getAuthorities()
        );
        authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // authentication ******
        List<Question> surveyQuestions = survey.getQuestions();
        Set<Response> surveyResponses = responseRepository.findResponsesByUserOidAndSurveyOid(currentUser.getOid(), surveyOid);
        List<Response> updatedResponses = new ArrayList<>();
        surveyQuestions.forEach(question -> {
            SurveyResponseQuestionRequestDto srqrDto = dtoList.stream().filter((dto) -> dto.getQuestionOid().equals(question.getOid())).findAny().orElse(null);
            if (srqrDto != null) {
                List<Response> surveySingleResponse = surveyResponses.stream().filter(response -> response.getQuestion().getOid().equals(question.getOid())).toList();
                if (surveySingleResponse.isEmpty()) {
                    updatedResponses.add(Response.builder()
                            .responseString(srqrDto.getResponseString())
                            .question(question)
                            .survey(survey)
                            .user(currentUser)
                            .build());
                } else {
                    Response response = surveySingleResponse.get(0);
                    response.setResponseString(srqrDto.getResponseString());
                    updatedResponses.add(response);
                }
            }
        });
        questionRepository.saveAll(surveyQuestions);
        responseRepository.saveAll(updatedResponses);
        surveyRepository.save(survey);

        return true;
    }

    public Survey updateSurveyResponses(Long surveyId, SurveyUpdateResponseRequestDto dto) {

        Survey survey = surveyRepository.findActiveById(surveyId)
                .orElseThrow(() -> new SurveyNotFoundException("Survey is not Found"));

        if (Boolean.FALSE.equals(crossCheckSurveyQuestionsAndUpdateResponses(survey, dto.getUpdateResponseMap()))) {
            throw new QuestionsAndResponsesDoesNotMatchException("Questions does not match with responses.");
        }
        Optional<Long> currentUserIdOptional = Optional.of((Long) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        Long currentUserId = currentUserIdOptional.orElseThrow(() -> new UndefinedTokenException("Token does not contain User Info"));
        List<Response> currentUserResponses = survey.getQuestions()
                .stream()
                .flatMap(question -> question.getResponses().stream())
                .filter(response -> response.getUser().getOid().equals(currentUserId))
                .collect(toList());

        currentUserResponses
                .parallelStream()
                .filter(response -> dto.getUpdateResponseMap().containsKey(response.getOid()))
                .forEach(response -> response.setResponseString(dto.getUpdateResponseMap().get(response.getOid())));
        responseRepository.saveAll(currentUserResponses);
        return survey;
    }

    @Transactional
    public Boolean assignSurveyToClassroom(SurveyAssignRequestDto dto) throws MessagingException {
//TODO student listesinden student tag classroom tage eşit olanları student listesi olarak dönecek
        Survey survey = surveyRepository.findActiveById(dto.getSurveyId())
                .orElseThrow(() -> new SurveyNotFoundException("Survey is not Found"));

        StudentTag studentTag = studentTagService.findByStudentTagName(dto.getStudentTag())
                .orElseThrow(() -> new StudentTagNotFoundException("Student Tag is not Found"));

        Optional<SurveyRegistration> surveyRegistrationOptional = survey.getSurveyRegistrations()
                .parallelStream()
                .filter(sR -> sR.getSurvey().getOid().equals(survey.getOid()) && sR.getStudentTag().getOid().equals(studentTag.getOid()))
                .findAny();

        if (surveyRegistrationOptional.isPresent()) {
            throw new SurveryAlreadyAssignToClassException("Survey has been already assigned to Classroom.");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        LocalDateTime startDate;
        try {
            startDate = dateFormat.parse(dto.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            startDate = LocalDateTime.now();
        }
        SurveyRegistration surveyRegistration = surveyRegistrationRepository.save(SurveyRegistration.builder()
                .survey(survey)
                .studentTag(studentTag)
                .startDate(startDate)
                .endDate(startDate.plusDays(dto.getDays()))
                .build());

        survey.getSurveyRegistrations().add(surveyRegistration);

        surveyRepository.save(survey);

        //TODO mail gönderme sıkıntı yaratıyor
        if (surveyRegistration.getStartDate().isBefore(LocalDateTime.now())) {
            sendEmail(surveyRegistration, dto.getDays());
        }
        return true;
    }

    private void sendEmail(SurveyRegistration surveyRegistration, int days) throws MessagingException {
        emailService.sendSurveyMail(generateMailTokenMap(surveyRegistration, days));
    }

    private Map<String, String> generateMailTokenMap(SurveyRegistration surveyRegistration, int days) {
        Map<String, String> emailTokenMap = new HashMap<>();
        Long studentTagOid = surveyRegistrationRepository.findStudentTagOfSurveyRegistration(surveyRegistration.getOid());
        List<Long> userOids = studentTagService.findUserOidByStudentTagOid(studentTagOid);
        List<User> users = userOids.stream()
                .map(oid -> userService.findActiveById(oid)
                        .orElseThrow(() -> new UserDoesNotExistsException("User not found!")))
                .toList();

        users
                .forEach(user -> emailTokenMap.put(
                        user.getEmail(),
                        jwtService.generateSurveyEmailToken(
                                surveyRegistration.getSurvey().getOid(),
                                studentTagOid,
                                user.getEmail(),
                                days)));

        return emailTokenMap;
    }
    public List<SurveyByStudentTagResponseDto> findSurveysByStudentTag(Long studentTagOid) {
        StudentTag studentTag = studentTagService.findActiveById(studentTagOid)
                .orElseThrow(() -> new StudentTagNotFoundException("Student tag not found with id: " + studentTagOid));


        List<Response> responses = getResponsesForStudentTag(studentTag);

        Map<Survey, List<Response>> responsesBySurvey = responses.stream()
                .collect(Collectors.groupingBy(Response::getSurvey));

        List<SurveyByStudentTagResponseDto> allSurveyResponses = responsesBySurvey.entrySet().stream()
                .map(entry -> {
                    Survey survey = entry.getKey();
                    List<Response> surveyResponses = entry.getValue();
                    Map<Question, List<Response>> responsesByQuestion = surveyResponses.stream()
                            .collect(Collectors.groupingBy(Response::getQuestion));
                    List<SurveyByStudentTagQuestionsResponseDto> questions = responsesByQuestion.entrySet().stream()
                            .map(questionEntry -> {
                                Question question = questionEntry.getKey();
                                List<Response> questionResponses = questionEntry.getValue();
                                List<SurveyByStudentTagQuestionAnswersResponseDto> responseDtos = questionResponses.stream()
                                        .map(response -> SurveyByStudentTagQuestionAnswersResponseDto.builder()
                                                .responseOid(response.getOid())
                                                .responseString(response.getResponseString())
                                                .build())
                                        .collect(Collectors.toList());
                                return SurveyByStudentTagQuestionsResponseDto.builder()
                                        .questionOid(question.getOid())
                                        .questionString(question.getQuestionString())
                                        .responses(responseDtos)
                                        .build();
                            })
                            .collect(Collectors.toList());
                    return SurveyByStudentTagResponseDto.builder()
                            .surveyOid(survey.getOid())
                            .surveyTitle(survey.getSurveyTitle())
                            .courseTopic(survey.getCourseTopic())
                            .questions(questions)
                            .build();
                })
                .collect(Collectors.toList());

        return allSurveyResponses;
    }


    private Boolean crossCheckSurveyQuestionsAndCreateResponses(Survey survey, List<SurveyResponseQuestionRequestDto> getCreateResponses) {

        Set<Long> surveyQuestionIdSet = survey.getQuestions()
                .parallelStream()
                .map(Question::getOid)
                .collect(Collectors.toSet());
        Set<Long> createResponseQuestionIdSet = getCreateResponses.stream().map(SurveyResponseQuestionRequestDto::getQuestionOid).collect(Collectors.toSet());

        return surveyQuestionIdSet.equals(createResponseQuestionIdSet);
    }

    private Boolean crossCheckSurveyQuestionsAndUpdateResponses(Survey survey, Map<Long, String> updateResponseMap) {

        Set<Long> surveyQuestionIdSet = survey.getQuestions()
                .parallelStream()
                .map(Question::getOid)
                .collect(Collectors.toSet());
        Set<Long> updateResponseQuestionIdSet = updateResponseMap.keySet();

        return surveyQuestionIdSet.containsAll(updateResponseQuestionIdSet);
    }

    public SurveyOfClassroomMaskedResponseDto findMaskedSurveyAnswersAsAdminOrManager(FindSurveyAnswersRequestDto dto) {
        Survey survey = getSurveyById(dto.getSurveyOid());
        StudentTag studentTag = getStudentTagById(dto.getStudentTagOid());
        List<Response> responses = getResponsesForSurveyAndStudentTag(survey, studentTag);
        Map<Question, List<Response>> responsesByQuestion = groupResponsesByQuestion(responses);

        List<QuestionWithAnswersMaskedResponseDto> questions = responsesByQuestion.entrySet().stream()
                .map(entry -> {
                    Question question = entry.getKey();
                    List<Response> questionResponses = entry.getValue();
                    List<SimpleResponseDto> responseDtos = questionResponses.stream()
                            .map(response -> SimpleResponseDto.builder()
                                    .oid(response.getOid())
                                    .responseString(response.getResponseString())
                                    .build())
                            .collect(Collectors.toList());
                    return SurveyServiceConverter.convertToQuestionWithAnswersMaskedResponseDto(question, responseDtos);
                })
                .collect(Collectors.toList());
        return SurveyServiceConverter.convertToSurveyOfClassroomMaskedResponseDto(survey, questions);
    }

    public SurveyOfClassroomMaskedResponseDto findMaskedSurveyAnswersAsTrainer(FindSurveyAnswersRequestDto dto) {
        User user = userService.findActiveById((Long)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getCredentials()
        ).orElseThrow(() -> new ResourceNotFoundException("No such user."));
        Survey survey = getSurveyById(dto.getSurveyOid());
        StudentTag studentTag = getStudentTagById(dto.getStudentTagOid());

        Trainer trainer = trainerService.findTrainerByUserOid(user.getOid()).orElseThrow(() -> new ResourceNotFoundException("No such trainer."));
        if (trainer.getTrainerTags().stream().anyMatch(tag -> tag.getTagString().equals(studentTag.getTagString()))) {
            List<Response> responses = new ArrayList<>();
            if (roleService.userHasRole(user, ROLE_CONSTANTS.ROLE_MASTER_TRAINER)) {
                for (Student student : studentTag.getTargetEntities()) {
                    Set<Response> studentResponses = responseRepository.findBySurveyAndUser(survey, student.getUser());
                    for (Response response : studentResponses) {
                        if (!response.getQuestion().getQuestionTag().stream().anyMatch(tag -> tag.getTagString().contains("ASSISTANT_TRAINER"))) {
                            responses.add(response);
                        }
                    }
                }
            } else {
                for (Student student : studentTag.getTargetEntities()) {
                    Set<Response> studentResponses = responseRepository.findBySurveyAndUser(survey, student.getUser());
                    for (Response response : studentResponses) {
                        if (!response.getQuestion().getQuestionTag().stream().anyMatch(tag -> tag.getTagString().contains("MASTER_TRAINER"))) {
                            responses.add(response);
                        }
                    }
                }
            }
            Map<Question, List<Response>> responsesByQuestion = groupResponsesByQuestion(responses);
            List<QuestionWithAnswersMaskedResponseDto> questions = responsesByQuestion.entrySet().stream()
                    .map(entry -> {
                        Question question = entry.getKey();
                        List<Response> questionResponses = entry.getValue();
                        List<SimpleResponseDto> responseDtos = questionResponses.stream()
                                .map(response -> SimpleResponseDto.builder()
                                        .oid(response.getOid())
                                        .responseString(response.getResponseString())
                                        .build())
                                .collect(Collectors.toList());
                        return SurveyServiceConverter.convertToQuestionWithAnswersMaskedResponseDto(question, responseDtos);
                    })
                    .collect(Collectors.toList());
            return SurveyServiceConverter.convertToSurveyOfClassroomMaskedResponseDto(survey, questions);
        } else {
            throw new ResourceNotFoundException("Something went wrong");
            //TODO Exception TrainerTag ve StudentTag eşleşmemesi sebebiyle yeni exception hazırlanmalı.
        }

    }

    public SurveyResponseWithAnswersDto findSurveyAnswersUnmasked(FindSurveyAnswersRequestDto dto) {
        Survey survey = getSurveyById(dto.getSurveyOid());
        StudentTag studentTag = getStudentTagById(dto.getStudentTagOid());
        List<Response> responses = getResponsesForSurveyAndStudentTag(survey, studentTag);
        Map<Question, List<Response>> responsesByQuestion = groupResponsesByQuestion(responses);


        List<QuestionWithAnswersResponseDto> questions = responsesByQuestion.entrySet().stream()
                .map(entry -> {
                    Question question = entry.getKey();
                    List<Response> questionResponses = entry.getValue();
                    List<ResponseUnmaskedDto> responseDtos = questionResponses.stream()
                            .map(response -> ResponseUnmaskedDto.builder()
                                    .userOid(response.getUser().getOid())
                                    .firstName(response.getUser().getFirstName())
                                    .lastName(response.getUser().getLastName())
                                    .email(response.getUser().getEmail())
                                    .responseOid(response.getOid())
                                    .response(response.getResponseString())
                                    .build())
                            .collect(Collectors.toList());
                    return SurveyServiceConverter.convertToQuestionWithAnswersResponseDto(question, responseDtos);
                })
                .collect(Collectors.toList());

        return SurveyServiceConverter.convertToSurveyResponseWithAnswersDto(survey, questions);
    }


    // TODO surveyler trainerlara atandığında test edilecek
    public TrainerClassroomSurveyResponseDto findTrainerSurveys() {
        Trainer trainer = trainerService.findTrainerByUserOid((Long)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getCredentials()
        ).orElseThrow(() -> new TrainerNotFoundException("No such trainer."));
        return INSTANCE.toTrainerClassroomSurveyResponseDto(trainer);
    }


    public List<SurveyByStudentTagResponseDto> findSurveysByStudentOid(Long studentOid) {
        User user = userService.findActiveById((Long)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getCredentials()
        ).orElseThrow(() -> new ResourceNotFoundException("No such user."));

        Student student = studentService.findByOid(studentOid)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentOid));

        if (roleService.userHasRole(user, ROLE_CONSTANTS.ROLE_STUDENT)) {
            if(student.getUser().getOid() != user.getOid()){
                throw new AccessDeniedException("Access denied");
            }
        }

        List<Response> responses = responseRepository.findListByUser(student.getUser());
        Map<Survey, List<Response>> responsesBySurvey = responses.stream()
                .collect(Collectors.groupingBy(Response::getSurvey));

        return responsesBySurvey.entrySet().stream()
                .map(entry -> {
                    Survey survey = entry.getKey();
                    List<Response> surveyResponses = entry.getValue();
                    Map<Question, List<Response>> responsesByQuestion = surveyResponses.stream()
                            .collect(Collectors.groupingBy(Response::getQuestion));

                    List<SurveyByStudentTagQuestionsResponseDto> questions = responsesByQuestion.entrySet().stream()
                            .map(questionEntry -> {
                                Question question = questionEntry.getKey();
                                List<Response> questionResponses = questionEntry.getValue();
                                List<SurveyByStudentTagQuestionAnswersResponseDto> responseDtos = questionResponses.stream()
                                        .map(response -> SurveyByStudentTagQuestionAnswersResponseDto.builder()
                                                .responseOid(response.getOid())
                                                .responseString(response.getResponseString())
                                                .build())
                                        .collect(Collectors.toList());

                                return SurveyByStudentTagQuestionsResponseDto.builder()
                                        .questionOid(question.getOid())
                                        .questionString(question.getQuestionString())
                                        .responses(responseDtos)
                                        .build();
                            })
                            .collect(Collectors.toList());

                    return SurveyByStudentTagResponseDto.builder()
                            .surveyOid(survey.getOid())
                            .surveyTitle(survey.getSurveyTitle())
                            .courseTopic(survey.getCourseTopic())
                            .questions(questions)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public Boolean addQuestionToSurvey(SurveyAddQuestionRequestDto dto) {
        Survey survey = surveyRepository.findActiveById(dto.getSurveyId()).orElseThrow(() -> new SurveyNotFoundException("Survey not found."));
        Question question = questionRepository.findActiveById(dto.getQuestionId()).orElseThrow(() -> new QuestionNotFoundException("Question not found."));
        if (survey.getQuestions().contains(question)) {
            throw new QuestionAlreadyExistsException("Question already exists");
        }
        question.getSurveys().add(survey);
        survey.getQuestions().add(question);
        surveyRepository.save(survey);
        return true;
    }

    public void addQuestionsToSurvey(SurveyAddQuestionsRequestDto dto) {
        Survey survey = surveyRepository.findActiveById(dto.getSurveyId()).orElseThrow(() -> new SurveyNotFoundException("Survey not found."));
        List<Question> questions = survey.getQuestions();
        dto.getQuestionIds().forEach(qId -> {
            Question question = questionRepository.findActiveById(qId).orElseThrow(() ->
                    new QuestionNotFoundException("Question not found."));
            question.getSurveys().add(survey);
            questions.add(question);
        });
        survey.setQuestions(questions);
        surveyRepository.save(survey);
    }

    // ###########  Simplifier methods to avoid repeating codes. ###########
    private Survey getSurveyById(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id " + id));
    }

    private StudentTag getStudentTagById(Long id) {
        return studentTagService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student tag not found with id: " + id));
    }

    private List<Response> getResponsesForStudentTag(StudentTag studentTag){
        List<Response> responses = new ArrayList<>();
        for (Student student : studentTag.getTargetEntities()) {
            Set<Response> studentResponses = responseRepository.findSetByUser(student.getUser());
            responses.addAll(studentResponses);
        }
        return responses;
    }

    private List<Response> getResponsesForSurveyAndStudentTag(Survey survey, StudentTag studentTag) {
        List<Response> responses = new ArrayList<>();
        for (Student student : studentTag.getTargetEntities()) {
            Set<Response> studentResponses = responseRepository.findBySurveyAndUser(survey, student.getUser());
            responses.addAll(studentResponses);
        }
        return responses;
    }

    private Map<Question, List<Response>> groupResponsesByQuestion(List<Response> responses) {
        Map<Question, List<Response>> responsesByQuestion = responses.stream()
                .collect(Collectors.groupingBy(Response::getQuestion));
        return responsesByQuestion;
    }

    public Optional<Survey> findActiveById(Long surveyOid) {
        return surveyRepository.findActiveById(surveyOid);
    }

    public void save(Survey survey) {
        surveyRepository.save(survey);
    }

    public List<Survey> findAllActive() {
        return surveyRepository.findAllActive();
    }

    //TODO SurveyTags kontrol edilecek.
    public SurveyParticipantResponseDto findSurveyParticipants(SurveyParticipantRequestDto dto) {
        Optional<StudentTag> studentTag = Optional.ofNullable(studentTagService.findById(dto.getStudentTagOid()).orElseThrow(() -> new StudentTagNotFoundException("Student tag not found")));
        Optional<SurveyRegistration> surveyRegistration = Optional.ofNullable(Optional.ofNullable(surveyRegistrationRepository
                .findByStudentTagAndSurveyOid(studentTag.get(), dto.getSurveyOid())).orElseThrow(() -> new SurveyNotFoundException("Survey registration not found.")));
        if (surveyRegistration.isPresent()) {
            Survey survey = surveyRegistration.get().getSurvey();
            List<Student> students = studentTagService.getStudentsByStudentTag(studentTag.get());
            List<ParticipantResponseDto> participantResponseDtos = students.stream().map(student -> ParticipantResponseDto.builder()
                    .firstName(student.getUser().getFirstName())
                    .lastName(student.getUser().getLastName())
                    .email(student.getUser().getEmail())
                    .build()).collect(Collectors.toList());

            return SurveyParticipantResponseDto.builder()
                    .surveyTags(survey.getSurveyTags())
                    .studentList(participantResponseDtos.stream().collect(Collectors.toSet()))
                    .surveyTitle(survey.getSurveyTitle())
                    .build();
        }
        return null;
    }

    public SurveySimpleResponseDto assignSurveyTag(SurveyTagAssignRequestDto dto) {
        Optional<Survey> survey = Optional.ofNullable(surveyRepository.findActiveById(dto.getSurveyOid()).orElseThrow(() -> new SurveyNotFoundException("Survey not found.")));
        Optional<SurveyTag> surveyTag = Optional.ofNullable(surveyTagRepository.findActiveById(dto.getSurveyTagOid()).orElseThrow(() -> new SurveyTagNotFoundException("SurveyTag not found")));
        if (!survey.get().getSurveyTags().contains(surveyTag.get())) {
            survey.get().getSurveyTags().add(surveyTag.get());
            surveyTag.get().getTargetEntities().add(survey.get());
            surveyTagRepository.save(surveyTag.get());
            surveyRepository.save(survey.get());
            List<SurveyTagResponseDto> surveyTagResponseDtos = INSTANCE.toSurveyTagResponseDto(survey.get().getSurveyTags().stream().collect(Collectors.toList()));
            return INSTANCE.toSurveySimpleResponseDto(survey.get(), surveyTagResponseDtos);
        } else {
            throw new SurveyTagExistException("SurveyTag already exists in the survey");
        }
    }


    public List<Long> findTotalStudentBySurveyOid(Long surveyid,Long studentTagOid) {
       return surveyRegistrationRepository.findTotalStudentBySurveyOid(surveyid,studentTagOid);
    }

    public List<String> findStudentNameBySurveyOid(Long surveyid,Long studentTagOid) {
        return surveyRegistrationRepository.findStudentNameBySurveyOid(surveyid,studentTagOid);
    }
}


