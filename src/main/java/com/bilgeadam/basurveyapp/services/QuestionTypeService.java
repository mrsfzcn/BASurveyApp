package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionTypeResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTypeFindByIdResponseDto;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTypeNotFoundException;
import com.bilgeadam.basurveyapp.repositories.QuestionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionTypeService {
    private final QuestionTypeRepository questionTypeRepository;

    public boolean createQuestionType(CreateQuestionTypeRequestDto dto) {
        List<QuestionType> questionTypes = questionTypeRepository.findAll();
        if(!questionTypes.stream().anyMatch(type -> type.getQuestionType().equalsIgnoreCase(dto.getQuestionType()))){
            QuestionType questionType = (QuestionType.builder()
                    .questionType(dto.getQuestionType())
                    .build());
            questionTypeRepository.save(questionType);
            return true;
        }
        return false;
    }

    public void updateQuestionType(UpdateQuestionTypeRequestDto dto,Long id) {
        Optional<QuestionType> updateQuestionType = questionTypeRepository.findActiveById(id);
        if (updateQuestionType.isEmpty()) {
            throw new QuestionTypeNotFoundException("QuestionType is not found");
        }
        updateQuestionType.get().setQuestionType(dto.getQuestionType());
        QuestionType questionType = updateQuestionType.get();
        questionTypeRepository.save(questionType);
    }

    public QuestionTypeFindByIdResponseDto findById(Long questionId) {
        QuestionType questionType = questionTypeRepository.findActiveById(questionId)
                .orElseThrow(() -> new QuestionTypeNotFoundException("QuestionType is not found"));
        return QuestionTypeFindByIdResponseDto.builder()
                    .questionType(questionType.getQuestionType())
                    .build();

    }

    public List<AllQuestionTypeResponseDto> findAll() {
        List<QuestionType> questionTypes = questionTypeRepository.findAllActive();
        return questionTypes.stream()
                .map(questionType -> AllQuestionTypeResponseDto.builder()
                        .questionTypeId(questionType.getOid())
                        .questionType(questionType.getQuestionType())
                        .build())
                .collect(Collectors.toList());
    }

    public Boolean delete(Long questionTypeId) {
        QuestionType questionType = questionTypeRepository
                .findByOidAndState(questionTypeId, State.ACTIVE)
                .orElseThrow(() -> new QuestionTypeNotFoundException("QuestionType is not found"));
        questionType.setState(State.DELETED);
        questionTypeRepository.save(questionType);
        return true;
    }

    public Optional<QuestionType> findActiveById(Long questionTypeOid) {
        return questionTypeRepository.findActiveById(questionTypeOid);
    }
}