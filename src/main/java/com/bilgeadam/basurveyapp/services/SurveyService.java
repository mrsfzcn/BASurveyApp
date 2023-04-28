package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.EmailService;
import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS;
import com.bilgeadam.basurveyapp.converter.SurveyServiceConverter;
import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
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
import org.springframework.security.core.Authentication;
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
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final SurveyMapper surveyMapper;
    private final SurveyRegistrationRepository surveyRegistrationRepository;
    private final RoleService roleService;
    private final StudentTagService studentTagService;
    private final StudentService studentService;
    private final TrainerService trainerService;
    private final TrainerTagService trainerTagService;
    private final StudentTagRepository studentTagRepository;
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

    public List<SurveyResponseDto> getSurveyList() {
        List<Survey> surveys = surveyRepository.findAllActive();
        return INSTANCE.toSurveyResponseDtoList(surveys);
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

    public SurveySimpleResponseDto findByOid(Long surveyId) {

        Optional<Survey> surveyById = surveyRepository.findActiveById(surveyId);
        if (surveyById.isEmpty()) {
            throw new SurveyNotFoundException("Survey is not found");
        }
        return SurveyMapper.INSTANCE.toSurveySimpleResponseDto(surveyById.get());
    }

    public Boolean responseSurveyQuestions(String token, List<SurveyResponseQuestionRequestDto> dtoList, HttpServletRequest request) {

        if (jwtService.isSurveyEmailTokenValid(token)) {
            throw new AccessDeniedException("Invalid token");
        }
        Long studentTagOid = jwtService.extractStudentTagOid(token);
        Long surveyOid = jwtService.extractSurveyOid(token);
        Survey survey = surveyRepository.findActiveById(surveyOid)
                .orElseThrow(() -> new SurveyNotFoundException("Survey is not Found."));
        StudentTag studentTag = studentTagRepository.findActiveById(studentTagOid).orElseThrow(
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
        User currentUser = userRepository.findByEmail(userEmail)
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
            throw new EntityNotFoundException("Survey has been already assigned to Classroom.");
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
        List<Long> userOids = studentTagRepository.findUserOidByStudentTagOid(studentTagOid);
        List<User> users = userOids.stream()
                .map(oid -> userRepository.findActiveById(oid)
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


    public List<SurveyByStudentTagResponseDto> findByStudentTagOid(Long studentTagOid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        User user = userRepository.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        Student student = studentService.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Student does not exist"));
        if (roleService.userHasRole(user, ROLE_CONSTANTS.ROLE_ASSISTANT_TRAINER) ||
                roleService.userHasRole(user, ROLE_CONSTANTS.ROLE_MASTER_TRAINER)) {
            // Classroom classroom = classroomRepository.findActiveById(classroomOid).orElseThrow(() -> new ResourceNotFoundException("Classroom does not exist"));
            List<Student> students = studentService.findByStudentTagOid(studentTagOid);
            if (!students.contains(student)) {
                throw new AccessDeniedException("authentication failure.");
            }
        }
        List<Survey> surveyList = surveyRepository.findAllActive();
        List<Survey> surveysWithTheOidsOfTheClasses = surveyList
                .stream()
                .filter(survey -> survey.getSurveyRegistrations()
                        .stream()
                        .map(sR -> sR.getStudentTag().getOid())
                        .toList().contains(studentTagOid))
                .toList();

        return INSTANCE.toSurveyByStudentTagResponseDtoList(surveysWithTheOidsOfTheClasses);
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
        User user = userRepository.findActiveById((Long)
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
//        Set<TrainerTag> trainerTags = trainerTagService.getTrainerTags(trainer);
//        User user = trainer.getUser();

        return INSTANCE.toTrainerClassroomSurveyResponseDto(trainer);
    }


    public List<SurveyByStudentTagResponseDto> findStudentSurveys() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("authentication failure.");
        }
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("authentication failure.");
        }
        Long userOid = (Long) authentication.getCredentials();
        User user = userRepository.findActiveById(userOid).orElseThrow(() -> new UserDoesNotExistsException("User does not exist"));

        List<Survey> surveyList = surveyRepository.findAllActive();

        return INSTANCE.toSurveyByStudentTagResponseDtoList(surveyList
                .stream()
                .filter(survey -> survey.getStudentsWhoAnswered().stream().map(Student::getUser)
                        .map(BaseEntity::getOid).toList().contains(user.getOid())).toList());
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
        return studentTagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student tag not found with id: " + id));
    }

    private List<Response> getResponsesForSurveyAndStudentTag(Survey survey, StudentTag studentTag) {
        List<Response> responses = new ArrayList<>();
        for (Student student : studentTag.getTargetEntities()) {
            Set<Response> studentResponses = responseRepository.findBySurveyAndUser(survey, student.getUser());
            responses.addAll(studentResponses);
        }
        return responses;
    }

    private List<Response> getFilteredResponsesForSurveyAndStudentTagByTrainerTag(Survey survey, StudentTag studentTag, TrainerTag trainerTag) {
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
}


