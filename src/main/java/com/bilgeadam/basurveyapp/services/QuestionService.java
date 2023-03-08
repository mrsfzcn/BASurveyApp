package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.exceptions.custom.*;
import com.bilgeadam.basurveyapp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final SurveyRepository surveyRepository;

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final QuestionTagRepository questionTagRepository;
//    private final SubTagRepository subTagRepository;


    public Boolean createQuestion(CreateQuestionDto createQuestionDto) {
        List<QuestionTag> questionTagList = new ArrayList<>();
//        List<SubTag> subTagList = new ArrayList<>();
        for (int i = 0; i < createQuestionDto.getTagOids().size(); i++) {
            Optional<QuestionTag> tagTemp = questionTagRepository.findActiveById(createQuestionDto.getTagOids().get(i));
            tagTemp.ifPresent(questionTagList::add);
        }
//        for (int i = 0; i < createQuestionDto.getSubTagOids().size(); i++) {
//            Optional<SubTag> subTagTemp = subTagRepository.findActiveById(createQuestionDto.getSubTagOids().get(i));
//            subTagTemp.ifPresent(subTagList::add);
//        }
            Question question = Question.builder()
                    .questionString(createQuestionDto.getQuestionString())
                    .questionType(questionTypeRepository.findActiveById(createQuestionDto.getQuestionTypeOid()).orElseThrow(
                            () -> new QuestionTypeNotFoundException("Question type is not found")))
                    .order(createQuestionDto.getOrder())
                    .questionTag(questionTagList)
//                    .subtag(subTagList)
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
            updateQuestion.get().setQuestionTag(updateQuestionDto.getTagOids().stream().map(x-> questionTagRepository.findById(x).get()).collect(Collectors.toList()));
//            updateQuestion.get().setSubtag(updateQuestionDto.getSubTagOids().stream().map(x-> subTagRepository.findById(x).get()).collect(Collectors.toList()));
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
                        .tagOids(question.getQuestionTag().stream().map(QuestionTag::getOid).collect(Collectors.toList()))
//                        .subTagOids(question.getSubtag().stream().map(SubTag::getOid).collect(Collectors.toList()))
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
                    .tagOids(question.getQuestionTag().stream().map(QuestionTag::getOid).collect(Collectors.toList()))
//                    .subTagOids(question.getSubtag().stream().map(SubTag::getOid).collect(Collectors.toList()))
                    .build());
        }
        return questionsDto;
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
                        .tagOids(question.getQuestionTag().stream().map(QuestionTag::getOid).collect(Collectors.toList()))
//                        .subTagOids(question.getSubtag().stream().map(SubTag::getOid).collect(Collectors.toList()))
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
                    .tagOids(question.getQuestionTag().stream().map(QuestionTag::getOid).collect(Collectors.toList()))
//                    .subTagOids(question.getSubtag().stream().map(SubTag::getOid).collect(Collectors.toList()))
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

//    /**
//     * it helps to bring all subtag list according to subtag oids
//     * @param subTagOids
//     * @return
//     */
//    public List<SubTag> findAllSubTags(List<Long> subTagOids){
//       return subTagOids.stream()
//               .map(subtag-> subTagRepository.findById(subtag).orElseThrow(() -> new ResourceNotFoundException("SubTag not found.")))
//               .collect(Collectors.toList());
//    }



    public List<QuestionResponseDto> getQuestionByRole(GetQuestionByRoleRequestDto dto) {

        Set<Role> roles = userRepository.findActiveById(dto.getTrainerId()).get().getRoles();

        List<String> userRoles =  roles.stream().map(role -> role.getRole()).collect(Collectors.toList());

        List<Long> tagIds = null;
        for (String role :userRoles) {

            if(role.equals("MASTER_TRAINER") || role.equals("ASISTAN_TRAINER")){
                tagIds = questionTagRepository.findAllByTagString(role);
            }else {
                throw new ResourceNotFoundException("Role is not type of trainer");
            }
        }

        Optional<List<Question>> questions = questionRepository.findQuestionsByTagIds(tagIds);

        if (questions.isEmpty()) {
            throw new ResourceNotFoundException("Questions are not found");
        }

        return questions.get().stream().map(question -> QuestionResponseDto.builder()
                .questionOid(question.getOid())
                .questionString(question.getQuestionString())
                .questionTypeOid(question.getQuestionType().getOid())
                .order(question.getOrder())
                .tagOids(question.getQuestionTag().stream().map(QuestionTag::getOid).collect(Collectors.toList()))
//                .subTagOids(question.getSubtag().stream().map(SubTag::getOid).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());

    }

}
