package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.QuestionFindByIdResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTagResponseDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTypeNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
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
    private final QuestionTypeRepository questionTypeRepository;
    private final SurveyRepository surveyRepository;

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final JwtService jwtService;

    private final QuestionTagRepository questionTagRepository;


    public Boolean createQuestion(CreateQuestionDto createQuestionDto) {
        Set<QuestionTag> questionTagList = new HashSet<QuestionTag>();

        createQuestionDto.getTagOids().forEach(questTagOid ->
                   questionTagRepository.findActiveById(questTagOid).ifPresent(questionTagList::add));

            Question question = Question.builder()
                    .questionString(createQuestionDto.getQuestionString())
                    .questionType(questionTypeRepository.findActiveById(createQuestionDto.getQuestionTypeOid()).orElseThrow(
                            () -> new QuestionTypeNotFoundException("Question type is not found")))
                    .order(createQuestionDto.getOrder())
                    .questionTag(questionTagList)
                    .build();


            questionRepository.save(question);
            questionTagList.forEach(questionTag -> {
                questionTag.getTargetEntities().add(question);
                questionTagRepository.save(questionTag);
            });
        return true;
    }

// TODO questionTag ile target entitites
    public Boolean updateQuestion(UpdateQuestionDto updateQuestionDto) {
        Set<QuestionTag> questionTagList = new HashSet<QuestionTag>();
        Optional<Question> updateQuestion = questionRepository.findActiveById(updateQuestionDto.getQuestionOid());
        if (updateQuestion.isEmpty()) {
            throw new QuestionNotFoundException("Question is not found to update");
        } else {
            updateQuestion.get().setQuestionString(updateQuestionDto.getQuestionString());
            updateQuestion.get().setQuestionTag(updateQuestionDto.getTagOids().stream().map(questionTagOid->
                    questionTagRepository.findActiveById(questionTagOid).orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet()));
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

            QuestionFindByIdResponseDto questionFindByIdResponseDto = QuestionMapper.INSTANCE.toQuestionFindByIdResponseDto(optionalQuestion.get());
            return questionFindByIdResponseDto;
        }
    }

    public List<QuestionResponseDto> findAll() {
        List<Question> findAllList = questionRepository.findAllActive();

        List<QuestionResponseDto> responseDtoList = QuestionMapper.INSTANCE.toQuestionResponseDtos(findAllList);
        return responseDtoList;
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

    // TODO Metod doğru çalışmıyor düzenlenecek
    public List<QuestionResponseDto> findAllSurveyQuestions(String token) {
        if (!jwtService.isSurveyEmailTokenValid(token)) {
            throw new RuntimeException("Invalid token.");
        }
        Long surveyOid = jwtService.extractSurveyOid(token);
        Survey survey = surveyRepository.findActiveById(surveyOid).orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
        List<Question> questions = survey.getQuestions();
        List<QuestionResponseDto> questionsDto = new ArrayList<>();

        //TODO mapper kullanılacak
        for (Question question : questions) {
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
        return questionsDto;

//        List<QuestionResponseDto> questionsDto = QuestionMapper.INSTANCE.toQuestionResponseDtos(questions);
//        return questionsDto;


    }

    //TODO tag ler eklendiğinde test edilecektir.
    public List<QuestionResponseDto> filterSurveyQuestionsByKeyword(FilterSurveyQuestionsByKeywordRequestDto dto) {
        Survey survey = surveyRepository.findActiveById(dto.getSurveyOid())
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
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
        Survey survey = surveyRepository.findActiveById(dto.getSurveyOid())
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
        List<Question> allQuestions = questionRepository.findSurveyActiveQuestionList(survey.getOid());
        List<Question> tempQuestions = filterByTags(allQuestions, dto.getTagOids());
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
                .map(tag-> questionTagRepository.findById(tag).orElseThrow(() -> new ResourceNotFoundException("Tag not found.")))
                .collect(Collectors.toList());
    }


    public List<QuestionResponseDto> getQuestionByRole(GetQuestionByRoleIdRequestDto dto) {

        Optional<Trainer> trainer = trainerRepository.findActiveById(dto.getTrainerId());
        if(trainer.isEmpty()){
            throw new ResourceNotFoundException("Trainer is not found");
        }
        Optional<Survey> survey = surveyRepository.findActiveById(dto.getSurveyId());
        if(survey.isEmpty()){
            throw new ResourceNotFoundException("Survey is not found");
        }

        QuestionTag trainerQuestionTag;
        if(trainer.get().isMasterTrainer()){
           trainerQuestionTag=  questionRepository.findByTagString(ROLE_MASTER_TRAINER).orElseThrow(()->new ResourceNotFoundException("Tag not found"));
        }else {
           trainerQuestionTag= questionRepository.findByTagString(ROLE_ASSISTANT_TRAINER).orElseThrow(()->new ResourceNotFoundException("Tag not found"));
        }

        List<Question> questions = survey.get().getQuestions();
       List<Question> trainerQuestions = questions.stream().filter(question -> question.getQuestionTag().contains(trainerQuestionTag)).toList();


        List<QuestionResponseDto> questionResponseDto = QuestionMapper.INSTANCE.toQuestionResponseDtos(trainerQuestions);
        return questionResponseDto;

    }

}
