package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionTypeResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTypeFindByIdResponseDto;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.repositories.QuestionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionTypeService {
    private final QuestionTypeRepository questionTypeRepository;

    public void createQuestionType(CreateQuestionTypeRequestDto dto) {
        // TODO Check if exists
        QuestionType questionType = (QuestionType.builder()
                .questionType(dto.getQuestionType())
                .build());
        questionTypeRepository.save(questionType);
    }

    public Boolean updateQuestionType(UpdateQuestionTypeRequestDto dto) {
        Optional<QuestionType> updateQuestionType = questionTypeRepository.findActiveById(dto.getQuestionTypeOid());
        if (updateQuestionType.isEmpty()) {
            // TODO exception
            throw new RuntimeException("QuestionType is not found");
        } else {
            updateQuestionType.get().setQuestionType(dto.getQuestionType());
            QuestionType questionType = updateQuestionType.get();
            questionTypeRepository.save(questionType);
            return true;
        }
    }

    public QuestionTypeFindByIdResponseDto findById(Long questionTypeId) {
        Optional<QuestionType> optionalQuestionType = questionTypeRepository.findActiveById(questionTypeId);
        if (optionalQuestionType.isEmpty()) {
            // TODO exception
            throw new RuntimeException("QuestionType is not found");
        } else {
            return QuestionTypeFindByIdResponseDto.builder()
                    .questionType(optionalQuestionType.get().getQuestionType())
                    .build();
        }
    }

    public List<AllQuestionTypeResponseDto> findAll() {
        List<QuestionType> findAllList = questionTypeRepository.findAllActive();
        List<AllQuestionTypeResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(questionType ->
                responseDtoList.add(AllQuestionTypeResponseDto.builder()
                        .questionTypeId(questionType.getOid())
                        .questionType(questionType.getQuestionType())
                        .build()));
        return responseDtoList;
    }

    public Boolean delete(Long questionTypeId) {
        Optional<QuestionType> deleteQuestionType = questionTypeRepository.findActiveById(questionTypeId);
        if (deleteQuestionType.isEmpty()) {
            throw new RuntimeException("QuestionType is not found");
        } else {
//            questionTypeRepository.softDeleteById(questionTypeId,"questiontypes");
            questionTypeRepository.deleteById(questionTypeId);
            return true;
        }

    }
}