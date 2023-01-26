package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionFindByIdResponseDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.repositories.IQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final IQuestionRepository questionRepository;

    public void createQuestion(CreateQuestionDto createQuestionDto, Long userOid) {
        Question question = Question.builder()
                .questionString(createQuestionDto.getQuestionString())
                .questionType(createQuestionDto.getQuestionTypeOid())
                .survey(createQuestionDto.getSurveyOid())
                .order(createQuestionDto.getOrder())
                .build();
        questionRepository.save(question);
    }


    public Boolean updateQuestion(UpdateQuestionDto updateQuestionDto, Long userOid) {
        Optional<Question> updateQuestion = questionRepository.findById(updateQuestionDto.getQuestionOid());
        if (updateQuestion.isEmpty()) {
            return false;
        } else {
            updateQuestion.get().setQuestionString(updateQuestionDto.getQuestionString());
            Question question = updateQuestion.get();
            questionRepository.save(question);
            return true;
        }
    }

    public QuestionFindByIdResponseDto findById(Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            return null; //todo exception
        } else {
            QuestionFindByIdResponseDto dto = QuestionFindByIdResponseDto.builder()
                    .questionString(optionalQuestion.get().getQuestionString())
                    .surveyId(optionalQuestion.get().getSurvey())
                    .questionTypeId(optionalQuestion.get().getQuestionType())
                    .order(optionalQuestion.get().getOrder())
                    .build();
            return dto;
        }

    }

    public List<AllQuestionResponseDto> findAll() {
        List<Question> findAllList = questionRepository.findAll();
        List<AllQuestionResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(question -> {
            responseDtoList.add(AllQuestionResponseDto.builder()
                    .questionString(question.getQuestionString())
                    .order(question.getOrder())
                    .build());
        });
        return responseDtoList;
    }

    public Boolean delete(Long questionId, Long userOid) {

        Optional<Question> deleteQuestion = questionRepository.findById(questionId);
        if (deleteQuestion.isEmpty()) {
            return false;
        } else {
            Question question = deleteQuestion.get();
            questionRepository.softDelete(question);
            return true;
        }
    }
}
