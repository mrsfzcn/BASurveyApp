package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionFindByIdResponseDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTypeNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyNotFoundException;
import com.bilgeadam.basurveyapp.repositories.QuestionRepository;
import com.bilgeadam.basurveyapp.repositories.QuestionTypeRepository;
import com.bilgeadam.basurveyapp.repositories.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final SurveyRepository surveyRepository;

    public void createQuestion(CreateQuestionDto createQuestionDto) {
            Question question = Question.builder()
                    .questionString(createQuestionDto.getQuestionString())
                    .questionType(questionTypeRepository.findActiveById(createQuestionDto.getQuestionTypeOid()).orElseThrow(
                            ()-> new QuestionTypeNotFoundException("Question type is not found")))
                    .survey(surveyRepository.findActiveById(createQuestionDto.getSurveyOid()).orElseThrow(
                            ()-> new SurveyNotFoundException("Survey is not found.")))
                    .order(createQuestionDto.getOrder())
                    .build();
            questionRepository.save(question);

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
                        .surveyTitle(surveyRepository.findActiveById(optionalQuestion.get().getSurvey().getOid()).get().getSurveyTitle()
                                .describeConstable().orElseThrow(()-> new SurveyNotFoundException("Survey not found")))
                        .questionType(questionTypeRepository.findActiveById(optionalQuestion.get().getQuestionType().getOid()).get().getQuestionType()
                                .describeConstable().orElseThrow(()-> new QuestionTypeNotFoundException("Question type not found")))
                        .order(optionalQuestion.get().getOrder())
                        .build();

        }

    }

    public List<AllQuestionResponseDto> findAll() {
        List<Question> findAllList = questionRepository.findAllActive();
        List<AllQuestionResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(question ->
                responseDtoList.add(AllQuestionResponseDto.builder()
                .questionString(question.getQuestionString())
                .order(question.getOrder())
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
}
