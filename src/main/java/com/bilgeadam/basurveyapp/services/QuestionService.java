package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.CreateQuestionDto;
import com.bilgeadam.basurveyapp.dto.request.FilterSurveyQuestionsByKeywordRequestDto;
import com.bilgeadam.basurveyapp.dto.request.FilterSurveyQuestionsRequestDto;
import com.bilgeadam.basurveyapp.dto.request.CreateQuestionUserRoleRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionFindByIdResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.SurveySimpleResponseDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.SubTag;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.Tag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTypeNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyNotFoundException;
import com.bilgeadam.basurveyapp.repositories.*;
import com.bilgeadam.basurveyapp.repositories.QuestionRepository;
import com.bilgeadam.basurveyapp.repositories.QuestionTypeRepository;
import com.bilgeadam.basurveyapp.repositories.SurveyRepository;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final SurveyRepository surveyRepository;

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final TagRepository tagRepository;
    private final SubTagRepository subTagRepository;


    public Boolean createQuestion(CreateQuestionDto createQuestionDto) {
        Question question = Question.builder()
                .questionString(createQuestionDto.getQuestionString())
                .questionType(questionTypeRepository.findActiveById(createQuestionDto.getQuestionTypeOid()).orElseThrow(
                        () -> new QuestionTypeNotFoundException("Question type is not found")))
                .order(createQuestionDto.getOrder())
                .tag(tagRepository.findById(createQuestionDto.getTagOid()).get())
                .subTags(createQuestionDto.getSubTagOids().stream().map(x-> subTagRepository.findById(x).get()).collect(Collectors.toList()))
                .build();
        questionRepository.save(question);
        return true;
    }


    public Boolean updateQuestion(UpdateQuestionDto updateQuestionDto) {

        Optional<Question> updateQuestion = questionRepository.findActiveById(updateQuestionDto.getQuestionOid());
        if (updateQuestion.isEmpty()) {
            throw new QuestionNotFoundException("Question is not found to update");
        } else {
            updateQuestion.get().setQuestionString(updateQuestionDto.getQuestionString());
            Question question = updateQuestion.get();
            questionRepository.save(question);
            return true;
        }
    }

    public QuestionFindByIdResponseDto findById(Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findActiveById(questionId);
        if (optionalQuestion.isEmpty()) {
            throw new QuestionNotFoundException("Question is not found");
        } else {

            return QuestionFindByIdResponseDto.builder()
                    .questionString(optionalQuestion.get().getQuestionString())
                    .surveys(optionalQuestion.get().getSurveys().stream().map(survey -> SurveySimpleResponseDto.builder()
                            .surveyOid(survey.getOid())
                            .surveyTitle(survey.getSurveyTitle())
                            .courseTopic(survey.getCourseTopic())
                            .build()).toList())
                    .questionType(questionTypeRepository.findActiveById(optionalQuestion.get().getQuestionType().getOid()).get().getQuestionType()
                            .describeConstable().orElseThrow(() -> new QuestionTypeNotFoundException("Question type not found")))
                    .order(optionalQuestion.get().getOrder())
                    .build();

        }

    }

    public List<QuestionResponseDto> findAll() {
        List<Question> findAllList = questionRepository.findAllActive();
        List<QuestionResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(question ->
                responseDtoList.add(QuestionResponseDto.builder()
                        .questionOid(question.getOid())
                        .questionString(question.getQuestionString())
                        .order(question.getOrder())
                       // .tagOid(question.getTag().getOid())
                       // .subTagOids(question.getSubTags().stream().map(x-> x.getOid()).collect(Collectors.toList()))
                        .build()));
        return responseDtoList;
    }

    public Boolean delete(Long questionId) {

        Optional<Question> deleteQuestion = questionRepository.findActiveById(questionId);
        if (deleteQuestion.isEmpty()) {
            throw new QuestionNotFoundException("Question not found to delete");
        } else {
            Question question = deleteQuestion.get();
            questionRepository.softDelete(question);
            return true;
        }
    }

    public List<QuestionResponseDto> findAllSurveyQuestions(String token) {
        if (!jwtService.isSurveyEmailTokenValid(token)) {
            throw new RuntimeException("Invalid token.");
        }
        Long surveyOid = jwtService.extractSurveyOid(token);
        Survey survey = surveyRepository.findActiveById(surveyOid).orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
        List<Question> questions = survey.getQuestions();
        List<QuestionResponseDto> questionsDto = new ArrayList<>();
        for (Question question : questions) {
            questionsDto.add(QuestionResponseDto.builder()
                    .questionOid(question.getOid())
                    .questionString(question.getQuestionString())
                    .order(question.getOrder())
                    //.tagOid(question.getTag().getOid())
                    //.subTagOids(question.getSubTags().stream().map(x-> x.getOid()).collect(Collectors.toList()))
                    .build());
        }
        return questionsDto;
    }

    //TODO tag ler eklendiğinde test edilecektir.
    public List<QuestionResponseDto> filterSurveyQuestionsByKeyword(FilterSurveyQuestionsByKeywordRequestDto dto) {
        Survey survey = surveyRepository.findActiveById(dto.getSurveyOid())
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
        //List<Question> allQuestions = questionRepository.findSurveyActiveQuestionList(survey);
        List<Question> allQuestions = questionRepository.findAllActive();
        System.out.println(allQuestions);
        List<QuestionResponseDto> filteredList = allQuestions.stream()
                .filter(filtered -> filtered.getQuestionString().toLowerCase().contains(dto.getKeyword().trim().toLowerCase()))
                .map(question -> QuestionResponseDto.builder()
                        .questionOid(question.getOid())
                        .questionString(question.getQuestionString())
                        .order(question.getOrder())
                        //.tagOid(question.getTag().getOid())
                        //.subTagOids(question.getSubTags().stream().map(x-> x.getOid()).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
        if (filteredList.size() != 0) {
            return filteredList;
        } else {
            throw new ResourceNotFoundException("There is no any result to be shown");
        }
    }

    //TODO tag ler eklendiğinde test edilecektir.
    public List<QuestionResponseDto> filterSurveyQuestions(FilterSurveyQuestionsRequestDto dto) {
        Survey survey = surveyRepository.findActiveById(dto.getSurveyOid())
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found."));
        //List<Question> allQuestions = questionRepository.findSurveyActiveQuestionList(survey);
        List<Question> allQuestions = questionRepository.findAllActive();
        List<Question> tempQuestions = filterByTag(allQuestions, dto.getTagOid());;

        if(dto.getSubTagOids().size()!=0){
            tempQuestions = filterBySubTag(tempQuestions,dto.getSubTagOids());
        }

        if (tempQuestions.size() != 0) {
            return tempQuestions.stream().map(question -> QuestionResponseDto.builder()
                    .questionOid(question.getOid())
                    .questionString(question.getQuestionString())
                    .order(question.getOrder())
                    //.tagOid(question.getTag().getOid())
                    //.subTagOids(question.getSubTags().stream().map(x-> x.getOid()).collect(Collectors.toList()))
                    .build())
                    .collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException("There is no any result to be shown");
        }
    }

    /**
     * This method provides filtered questions list according to 'Tag'
     * @param questions
     * @param tagOid
     * @return
     */
    public List<Question> filterByTag(List<Question> questions, Long tagOid){
            Tag tag = tagRepository.findById(tagOid).orElseThrow(() -> new ResourceNotFoundException("Tag not found."));
            return questions.stream()
                    .filter(filtered -> filtered.getTag().getOid().equals(tagOid))
                    .collect(Collectors.toList());
    }

    /**
     * This method provides filtered questions list according to 'SubTags'
     * @param questions
     * @param subTagOids
     * @return
     */
    public List<Question> filterBySubTag(List<Question> questions,List<Long>  subTagOids){
        List<SubTag> subTags = findAllSubTags(subTagOids);
        return questions.stream()
                .filter(filtered -> filtered.getSubTags().containsAll(subTags))
                .collect(Collectors.toList());
    }

    /**
     * it helps to bring all subtag list according to subtag oids
     * @param subTagOids
     * @return
     */
    public List<SubTag> findAllSubTags(List<Long> subTagOids){
       return subTagOids.stream()
               .map(x-> subTagRepository.findById(x).orElseThrow(() -> new ResourceNotFoundException("SubTag not found.")))
               .collect(Collectors.toList());
    }


    public Boolean save(CreateQuestionUserRoleRequestDto dto) {

        Question question = Question.builder()
                .questionString(dto.getQuestionString())
                .questionType(questionTypeRepository.findActiveById(dto.getQuestionTypeOid()).orElseThrow(
                        () -> new QuestionTypeNotFoundException("Question type is not found")))
                .survey(surveyRepository.findActiveById(dto.getSurveyOid()).orElseThrow(
                        () -> new SurveyNotFoundException("Survey is not found.")))
                .order(dto.getOrder())
                .role(dto.getRole())
                .build();
        questionRepository.save(question);
        return true;
    }
}
