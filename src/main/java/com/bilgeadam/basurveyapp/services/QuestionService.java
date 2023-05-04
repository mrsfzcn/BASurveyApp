package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.exceptions.custom.*;
import com.bilgeadam.basurveyapp.mapper.QuestionMapper;
import com.bilgeadam.basurveyapp.mapper.QuestionTagMapper;
import com.bilgeadam.basurveyapp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS.ROLE_ASSISTANT_TRAINER;
import static com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS.ROLE_MASTER_TRAINER;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionTypeService questionTypeService;
    private final SurveyService surveyService;
    private final TrainerService trainerService;
    private final JwtService jwtService;
    private final QuestionTagService questionTagService;

    private final ResponseRepository responseRepository;


    public Boolean createQuestion(CreateQuestionDto createQuestionDto) {
        if (questionRepository.findByQuestionString(createQuestionDto.getQuestionString()).isPresent()) {
            throw new QuestionAlreadyExistsException("Question with the same question string already exists.");
        }


        Set<QuestionTag> questionTagList = new HashSet<>();

        List<Long> tagOids = createQuestionDto.getTagOids().stream().toList();
        tagOids.forEach(questTagOid -> questionTagService.findActiveById(questTagOid).ifPresent(questionTagList::add));

        Question question = Question.builder()
                .questionString(createQuestionDto.getQuestionString())
                .questionType(questionTypeService.findActiveById(createQuestionDto.getQuestionTypeOid()).orElseThrow(
                        () -> new QuestionTypeNotFoundException("Question type is not found")))
                .order(createQuestionDto.getOrder())
                .questionTag(questionTagList)
                .build();

        questionRepository.save(question);
        List<QuestionTag> questionTagsToAdd = new ArrayList<>(questionTagList);
        questionTagsToAdd.forEach(questionTag -> {
            questionTag.getTargetEntities().add(question);
            questionTagService.save(questionTag);
        });
        return true;
    }

// TODO questionTag ile target entitites
    public Boolean updateQuestion(UpdateQuestionDto updateQuestionDto) {

        Optional<Question> updateQuestion = questionRepository.findActiveById(updateQuestionDto.getQuestionOid());
        if (updateQuestion.isEmpty()) {
            throw new QuestionNotFoundException("Question is not found to update");
        } else {
            updateQuestion.get().setQuestionString(updateQuestionDto.getQuestionString());
            updateQuestion.get().setQuestionTag(updateQuestionDto.getTagOids().stream().map(questionTagOid->
                    questionTagService.findActiveById(questionTagOid).orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet()));
            Question question = updateQuestion.get();
            question.getQuestionTag().forEach(questionTag -> {
                questionTag.getTargetEntities().add(question);

            });
            questionRepository.save(question);
            return true;
        }
    }

    public QuestionFindByIdResponseDto findById(Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findActiveById(questionId);
        if (optionalQuestion.isEmpty()) {
            throw new QuestionNotFoundException("Question is not found");
        } else {

            return QuestionMapper.INSTANCE.toQuestionFindByIdResponseDto(optionalQuestion.get());
        }
    }

    public List<QuestionResponseDto> findAll() {
        List<Question> findAllList = questionRepository.findAllActive();

        return QuestionMapper.INSTANCE.toQuestionResponseDtos(findAllList);
    }

    public Boolean delete(Long questionId) {

        Optional<Question> deleteQuestion = questionRepository.findActiveById(questionId);
        if (deleteQuestion.isEmpty()) {
            throw new QuestionNotFoundException("Question not found to delete");
        } else {
            Question question = deleteQuestion.get();
           return questionRepository.softDeleteById(question.getOid());
        }
    }

    // TODO Metot doğru çalışmıyor düzenlenecek
    public List<QuestionResponseDto> findAllSurveyQuestions(String token) {
        if (jwtService.isSurveyEmailTokenValid(token)) {
            throw new UndefinedTokenException("Invalid token.");
        }
        Long surveyOid = jwtService.extractSurveyOid(token);
        Survey survey = surveyService.findActiveById(surveyOid).orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
        List<Question> questions = survey.getQuestions();
        Set<Long> uniqueQuestionIds = new HashSet<>();
        List<QuestionResponseDto> questionsDto = new ArrayList<>();

        //TODO mapper kullanılacak
        for (Question question : questions) {
            if (uniqueQuestionIds.add(question.getOid())) {
                questionsDto.add(QuestionResponseDto.builder()
                        .questionOid(question.getOid())
                        .questionString(question.getQuestionString())
                        .order(question.getOrder())
                        .questionTags(question.getQuestionTag().stream().map(
                                questionTag -> QuestionTagResponseDto.builder()
                                        .oid(questionTag.getOid())
                                        .tagString(questionTag.getTagString())
                                        .build()).collect(Collectors.toList()))
                        .questionTypeOid(question.getQuestionType().getOid())
                        .build());
            }
        }
        return questionsDto;
    }

    //TODO tag ler eklendiğinde test edilecektir.
    public List<QuestionResponseDto> filterSurveyQuestionsByKeyword(FilterSurveyQuestionsByKeywordRequestDto dto) {
        Survey survey = surveyService.findActiveById(dto.getSurveyOid())
                .orElseThrow(() -> new SurveyNotFoundException("Survey not found."));
        List<Question> allQuestions = questionRepository.findSurveyActiveQuestionList(survey.getOid());
        List<QuestionResponseDto> filteredList = allQuestions.stream()
                .filter(question -> question.getQuestionString().toLowerCase().contains(dto.getKeyword().trim().toLowerCase()))
                .map(question -> QuestionResponseDto.builder()
                        .questionOid(question.getOid())
                        .questionString(question.getQuestionString())
                        .order(question.getOrder())
                        .questionTags(question.getQuestionTag().stream().map(
                                questionTag -> QuestionTagResponseDto.builder()
                                        .oid(questionTag.getOid())
                                        .tagString(questionTag.getTagString())
                                        .build()).collect(Collectors.toList()))
//                        .questionTypeOid(question.getQuestionType().getOid())
                        .build())
                .collect(Collectors.toList());
        if (filteredList.size() != 0) {
            return filteredList;
        } else {
            throw new ResourceNotFoundException("There is no any result to be shown");
        }
    }

    //TODO birden fazla tag olduğunda nasıl çözülecek
    public List<QuestionResponseDto> filterSurveyQuestions(FilterSurveyQuestionsRequestDto dto) {
        Survey survey = surveyService.findActiveById(dto.getSurveyOid())
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
        List<Question> allQuestions = questionRepository.findSurveyActiveQuestionList(survey.getOid());
        List<Question> tempQuestions = filterByTags(allQuestions, dto.getQuestionTagOids());
//        if(dto.getSubTagOids().size()!=0){
//            tempQuestions = filterBySubTag(tempQuestions,dto.getSubTagOids());
//        }
        if (tempQuestions.size() != 0) {
            return tempQuestions.stream().map(question -> QuestionResponseDto.builder()
                    .questionOid(question.getOid())
                    .questionString(question.getQuestionString())
                    .order(question.getOrder())
                    .questionTags(QuestionTagMapper.INSTANCE.toQuestionTagResponseDtoList(question.getQuestionTag()))
                    .build())
                    .collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException("There is no any result to be shown");
        }
    }


    /**
     * This method provides filtered questions list according to 'Tags'
     * @param questions
     * @param tagOids
     * @return
     */
    public List<Question> filterByTags(List<Question> questions,List<Long>  tagOids){
        List<QuestionTag> questionTags = findAllTags(tagOids);
        return questions.stream()
                .filter(question -> question.getQuestionTag().containsAll(questionTags))
                .collect(Collectors.toList());
    }
    /**
     * This method provides filtered questions list according to 'SubTags'
     * @param questions
     * @param subTagOids
     * @return
     */
//    public List<Question> filterBySubTag(List<Question> questions,List<Long>  subTagOids){
//        List<SubTag> subTags = findAllSubTags(subTagOids);
//        return questions.stream()
//                .filter(question -> question.getSubtag().containsAll(subTags))
//                .collect(Collectors.toList());
//    }

    /**
     * it helps to bring all subtag list according to tag oids
     * @param tagOids
     * @return
     */
    public List<QuestionTag> findAllTags(List<Long> tagOids){
        return tagOids.stream()
                .map(tag-> questionTagService.findById(tag).orElseThrow(() -> new ResourceNotFoundException("Tag not found.")))
                .collect(Collectors.toList());
    }

    public List<QuestionsTrainerTypeResponseDto> questionByTrainerType(GetQuestionByRoleIdRequestDto dto) {

        Optional<Trainer> trainer = trainerService.findActiveById(dto.getTrainerId());
        if (trainer.isEmpty()) {
            throw new ResourceNotFoundException("Trainer is not found");
        }
        Optional<Survey> survey = surveyService.findActiveById(dto.getSurveyId());
        if (survey.isEmpty()) {
            throw new ResourceNotFoundException("Survey is not found");
        }

        QuestionTag trainerQuestionTag;
        if (trainer.get().isMasterTrainer()) {
            trainerQuestionTag = questionRepository.findByTagString(ROLE_MASTER_TRAINER).orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        } else {
            trainerQuestionTag = questionRepository.findByTagString(ROLE_ASSISTANT_TRAINER).orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        }

        List<Question> questions = survey.get().getQuestions();
        Set<Question> trainerQuestions = questions.stream()
                .filter(question -> question.getQuestionTag().contains(trainerQuestionTag))
                .collect(Collectors.toSet());


        return QuestionMapper.INSTANCE.toQuestionsTrainerTypeResponseDto(trainerQuestions);
    }

    public Optional<Question> findActiveById(Long questionOid) {

        return questionRepository.findActiveById(questionOid);
    }

    public void save(Question question) {
        questionRepository.save(question);
    }

    public List<Long> findSurveyQuestionOidList(Long oid) {
        return questionRepository.findSurveyQuestionOidList(oid);
    }

    public void saveAll(List<Question> surveyQuestions) {
        questionRepository.saveAll(surveyQuestions);
    }
}

