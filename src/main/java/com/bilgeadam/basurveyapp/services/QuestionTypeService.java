package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionTypeResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTypeFindByIdResponseDto;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.repositories.IQuestionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionTypeService {
    private final IQuestionTypeRepository questionTypeRepository;

    public void createQuestionType(CreateQuestionTypeRequestDto dto, Long userOid) {
        QuestionType questionType = (QuestionType.builder()
                .questionType(dto.getQuestionType())
                .build());
        questionTypeRepository.save(questionType);
    }

    public Boolean updateQuestionType(UpdateQuestionTypeRequestDto dto, Long userOid) {
        Optional<QuestionType> updateQuestionType = questionTypeRepository.findById(dto.getQuestionTypeOid());
        if (updateQuestionType.isEmpty()) {
            return false;
        } else {
            updateQuestionType.get().setQuestionType(dto.getQuestionType());
            QuestionType questionType = updateQuestionType.get();
            questionTypeRepository.save(questionType);
            return true;
        }
    }

    public QuestionTypeFindByIdResponseDto findById(Long questionTypeId) {
        Optional<QuestionType> optionalQuestionType = questionTypeRepository.findById(questionTypeId);
        if (optionalQuestionType.isEmpty()) {
            return null;
        } else {
            QuestionTypeFindByIdResponseDto dto = QuestionTypeFindByIdResponseDto.builder()
                    .questionType(optionalQuestionType.get().getQuestionType())
                    .build();
            return dto;
        }
    }

    public List<AllQuestionTypeResponseDto> findAll() {
        List<QuestionType> findAllList = questionTypeRepository.findAll();
        List<AllQuestionTypeResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(questionType -> {
            responseDtoList.add(AllQuestionTypeResponseDto.builder()
                    .questionType(questionType.getQuestionType())
                    .build());
        });
        return responseDtoList;
    }

    public Boolean delete(Long questionTypeId, Long userOid) {

        Optional<QuestionType> deleteQuestionType = questionTypeRepository.findById(questionTypeId);
        if (deleteQuestionType.isEmpty()) {
            return false;
        } else {
            QuestionType questionType = deleteQuestionType.get();
            questionTypeRepository.softDelete(questionType);
            return true;
        }

    }
}