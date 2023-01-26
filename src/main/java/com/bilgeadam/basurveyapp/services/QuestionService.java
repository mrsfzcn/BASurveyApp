package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionFindByIdResponseDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public void createQuestion(CreateQuestionDto createQuestionDto) {
        // TODO check if exists
        Question question = Question.builder()
                .questionString(createQuestionDto.getQuestionString())
                .questionType(createQuestionDto.getQuestionTypeOid())
                .survey(createQuestionDto.getSurveyOid())
                .order(createQuestionDto.getOrder())
                .build();
        questionRepository.save(question);
    }


    public Boolean updateQuestion(UpdateQuestionDto updateQuestionDto) {
        Optional<Question> updateQuestion = questionRepository.findActiveById(updateQuestionDto.getQuestionOid());
        if (updateQuestion.isEmpty()) {
            // TODO
            throw new RuntimeException("Question is not found");
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
            //todo exception
            throw new RuntimeException("Question is not found");
        } else {
            return QuestionFindByIdResponseDto.builder()
                    .questionString(optionalQuestion.get().getQuestionString())
                    .surveyId(optionalQuestion.get().getSurvey())
                    .questionTypeId(optionalQuestion.get().getQuestionType())
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
            // TODO exception
            throw new RuntimeException("Question is not found");
        } else {
            Question question = deleteQuestion.get();
            questionRepository.softDelete(question);
            return true;
        }
    }
}
