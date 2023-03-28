package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.EmailService;
import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS;
import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.base.BaseEntity;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.exceptions.custom.*;
import com.bilgeadam.basurveyapp.mapper.ResponseMapper;
import com.bilgeadam.basurveyapp.mapper.SurveyMapper;
import com.bilgeadam.basurveyapp.repositories.*;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
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
import static java.util.stream.Collectors.groupingBy;
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
            throw new EntityExistsException("This survey title already in use.");
        }
        return true;
    }

    public Survey update(Long surveyId, SurveyUpdateRequestDto dto) {

        Optional<Survey> surveyToBeUpdated = surveyRepository.findActiveById(surveyId);
        if (surveyToBeUpdated.isEmpty()) {
            throw new ResourceNotFoundException("Survey is not found");
        }
        surveyToBeUpdated.get().setSurveyTitle(dto.getSurveyTitle());
        return surveyRepository.save(surveyToBeUpdated.get());
    }

    public boolean delete(Long surveyId) {

        Optional<Survey> surveyToBeDeleted = surveyRepository.findActiveById(surveyId);
        if (surveyToBeDeleted.isEmpty()) {
            throw new ResourceNotFoundException("Survey is not found");
        }
        return surveyRepository.softDeleteById(surveyToBeDeleted.get().getOid());
    }

    public SurveySimpleResponseDto findByOid(Long surveyId) {

        Optional<Survey> surveyById = surveyRepository.findActiveById(surveyId);
        if (surveyById.isEmpty()) {
            throw new ResourceNotFoundException("Survey is not found");
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
                .orElseThrow(() -> new ResourceNotFoundException("Survey is not Found."));
        StudentTag studentTag = studentTagRepository.findActiveById(studentTagOid).orElseThrow(
                () -> new ResourceNotFoundException("StudentTag is not Found."));

        SurveyRegistration surveyRegistration = survey.getSurveyRegistrations()
                .parallelStream()
                .filter(sR -> sR.getSurvey().getOid().equals(survey.getOid()))
                .filter(sR -> sR.getStudentTag().getOid().equals(studentTagOid))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("Survey has not assigned to the classroom."));

        LocalDate now = LocalDateTime.now().toLocalDate();
        LocalDate surveyStartDate = surveyRegistration.getStartDate().toLocalDate();
        LocalDate surveyEndDate = surveyRegistration.getEndDate().toLocalDate();

        if (now.isBefore(surveyStartDate)) {
            throw new ResourceNotFoundException("Survey has not initiated.");
        } else if (now.isAfter(surveyEndDate)) {
            throw new ResourceNotFoundException("Survey is Expired");
        }

        if (Boolean.FALSE.equals(crossCheckSurveyQuestionsAndCreateResponses(survey, dtoList))) {
            throw new UserInsufficientAnswerException("User must response all the questions.");
        }

        String userEmail = jwtService.extractEmail(token);
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User is not found"));

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
        responseRepository.saveAll(updatedResponses);
        return true;
    }

    public Survey updateSurveyResponses(Long surveyId, SurveyUpdateResponseRequestDto dto) {

        Survey survey = surveyRepository.findActiveById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey is not Found"));

        if (Boolean.FALSE.equals(crossCheckSurveyQuestionsAndUpdateResponses(survey, dto.getUpdateResponseMap()))) {
            throw new QuestionsAndResponsesDoesNotMatchException("Questions does not match with responses.");
        }
        Optional<Long> currentUserIdOptional = Optional.of((Long) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        Long currentUserId = currentUserIdOptional.orElseThrow(() -> new ResourceNotFoundException("Token does not contain User Info"));
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
                .orElseThrow(() -> new ResourceNotFoundException("Survey is not Found"));

        StudentTag studentTag = studentTagService.findByStudentTagName(dto.getStudentTag())
                .orElseThrow(() -> new ResourceNotFoundException("Student Tag is not Found"));
        // List<Student> classroom = getStudentsByStudentTag(studentTag);

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
                        .orElseThrow(() -> new ResourceNotFoundException("User not found!")))
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

    // Warning : studentTag ve trainerTag aynı sınıf için aynı olmalı!

    public SurveyOfClassroomMaskedResponseDto findSurveyAnswers(FindSurveyAnswersRequestDto dto) { // 1 survey id ve 1 trainertag id
        User user = userRepository.findActiveById((Long)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getCredentials()
        ).orElseThrow(() -> new ResourceNotFoundException("No such user."));
        if (roleService.userHasRole(user, ROLE_CONSTANTS.ROLE_ASSISTANT_TRAINER) || roleService.userHasRole(user, ROLE_CONSTANTS.ROLE_MASTER_TRAINER)) {
            Trainer trainer = trainerService.findTrainerByUserOid(user.getOid()).orElseThrow(() -> new ResourceNotFoundException("No such trainer."));
            if (trainerTagService.getTrainerTagsOids(trainer).contains(dto.getStudentTagOid())) {
                throw new AccessDeniedException("You dont have access to target class data.");
            }
//            if (user.getClassrooms().stream().map(Classroom::getOid).noneMatch(oid -> dto.getClassroomOid().equals(oid))) {
//                throw new AccessDeniedException("You dont have access to target class data.");
//            }
        }
//        Classroom classroom = classroomRepository.findActiveById(dto.getClassroomOid()).orElseThrow(()
//        -> new ResourceNotFoundException("Classroom not found."));
        StudentTag studentTag = studentTagService.findActiveById(dto.getStudentTagOid()).orElseThrow(()
                -> new ResourceNotFoundException("TrainerTag not found."));
        Survey survey = surveyRepository.findActiveById(dto.getSurveyOid()).orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
        List<Question> questions = survey.getQuestions();
//        List<Long> usersInClassroom = classroom.getUsers().stream().map(User::getOid).toList();
        List<Long> usersInClassroom = studentService.findByStudentTagOid(studentTag.getOid()).stream().map(Student::getUser).map(User::getOid).toList();

        List<QuestionWithAnswersMaskedResponseDto> questionsMaskedDto = questions.parallelStream()
                .map(question -> INSTANCE.toQuestionWithAnswersMaskedResponseDto(question, usersInClassroom))
                .collect(toList());

// Assuming you have a SurveyOfClassroomMaskedResponseDto and corresponding mapper
        SurveyOfClassroomMaskedResponseDto result = INSTANCE.toSurveyOfClassroomMaskedResponseDto(survey);
        return result;
    }

    // TODO surveyler trainerlara atandığında test edilecek
    public TrainerClassroomSurveyResponseDto findTrainerSurveys() {
        Trainer trainer = trainerService.findTrainerByUserOid((Long)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getCredentials()
        ).orElseThrow(() -> new ResourceNotFoundException("No such trainer."));
//        Set<TrainerTag> trainerTags = trainerTagService.getTrainerTags(trainer);
//        User user = trainer.getUser();

        return INSTANCE.toTrainerClassroomSurveyResponseDto(trainer);
    }

    //TODO method tag yapısına göre refactor edilecek
    public SurveyResponseWithAnswersDto findSurveyAnswersUnmasked(FindSurveyAnswersRequestDto dto) {
        List<Student> students = studentService.findByStudentTagOid(dto.getStudentTagOid());
        Survey survey = surveyRepository.findActiveById(dto.getSurveyOid()).orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
        List<Question> questions = survey.getQuestions();
        List<User> userList = students.stream().map(Student::getUser).toList();
        List<Long> userOidList = userList.stream().map(User::getOid).toList();
        boolean isManagerAndTrainer = false;

        User user = userRepository.findActiveById((Long)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getCredentials()
        ).orElseThrow(() -> new ResourceNotFoundException("No such user."));
        if (roleService.userHasRole(user, "MANAGER") && roleService.userHasRole(user, "MASTER_TRAINER")) {
            if (trainerTagService.getTrainerTagsOids(trainerService.findTrainerByUserOid(user.getOid()).orElseThrow(() ->
                    new ResourceNotFoundException("No such trainer."))).contains(dto.getStudentTagOid())) {
                throw new AccessDeniedException("You dont have access to target class data.");
            }
            isManagerAndTrainer = true;
        }
        boolean finalIsManagerAndTrainer = isManagerAndTrainer;
        List<QuestionWithAnswersResponseDto> questionsDto = questions.parallelStream()
                .map(question -> {
                    List<ResponseUnmaskedDto> responses = INSTANCE.mapResponses(question, userOidList);
                    return INSTANCE.toQuestionWithAnswersResponseDto(question);
                })
                .collect(toList());
        return INSTANCE.toSurveyResponseWithAnswersDto(survey);
    }

    private ResponseUnmaskedDto getUnmaskedDto(User user, String responseString, Boolean isManagerAndTrainer) {
        // Intentionally keep builder pattern to keep masking user details
        ResponseUnmaskedDto responseUnmaskedDto = ResponseUnmaskedDto.builder()
                .userOid(user.getOid())
                .responseOid(user.getOid())
                .response(responseString)
                .firstName("****")
                .lastName("****")
                .email("****")
                .build();
        if (!isManagerAndTrainer) {
            responseUnmaskedDto.setFirstName(user.getFirstName());
            responseUnmaskedDto.setLastName(user.getLastName());
            responseUnmaskedDto.setEmail(user.getEmail());
        }
        return responseUnmaskedDto;
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
        User user = userRepository.findActiveById(userOid).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        List<Survey> surveyList = surveyRepository.findAllActive();

        return INSTANCE.toSurveyByStudentTagResponseDtoList(surveyList
                .stream()
                .filter(survey -> survey.getStudentsWhoAnswered().stream().map(Student::getUser)
                        .map(BaseEntity::getOid).toList().contains(user.getOid())).toList());
    }

    public Boolean addQuestionToSurvey(SurveyAddQuestionRequestDto dto) {
        Survey survey = surveyRepository.findActiveById(dto.getSurveyId()).orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
        Question question = questionRepository.findActiveById(dto.getQuestionId()).orElseThrow(() -> new ResourceNotFoundException("Question not found."));
        question.getSurveys().add(survey);
        survey.getQuestions().add(question);
        surveyRepository.save(survey);
        return true;
    }

    public void addQuestionsToSurvey(SurveyAddQuestionsRequestDto dto) {
        Survey survey = surveyRepository.findActiveById(dto.getSurveyId()).orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
        List<Question> questions = survey.getQuestions();
        dto.getQuestionIds().forEach(qId -> {
            Question question = questionRepository.findActiveById(qId).orElseThrow(() ->
                    new ResourceNotFoundException("Question not found."));
            question.getSurveys().add(survey);
            questions.add(question);
        });
        survey.setQuestions(questions);
        surveyRepository.save(survey);
    }
}