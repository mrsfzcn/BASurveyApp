package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagNotFoundException;
import com.bilgeadam.basurveyapp.repositories.QuestionTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionTagService {

    private final QuestionTagRepository questionTagRepository;

    public QuestionTag createTag(CreateTagDto dto) {
        System.out.println("question tag service");

        Optional<QuestionTag> existingQuestionTag = questionTagRepository.findOptionalByTagString(dto.getTagString());

        if (existingQuestionTag.isPresent()) {
            QuestionTag questionTag = existingQuestionTag.get();

            if (questionTag.getState() == State.ACTIVE) {
                throw new QuestionTagExistException("Question Tag already exists!");
            } else if (questionTag.getState() == State.DELETED) {
                questionTag.setState(State.ACTIVE);
                return questionTagRepository.save(questionTag);
            }
        }
        QuestionTag questionTag = QuestionTag.builder()
                .tagString(dto.getTagString())
                .mainTagOid(dto.getMainTagOid())
                .state(State.ACTIVE)
                .build();
        return questionTagRepository.save(questionTag);
    }

    public List<TagResponseDto> findAll() {

        List<QuestionTag> questionTags = questionTagRepository.findAllActive();
        return questionTags.stream()
                .map(questionTag -> TagResponseDto.builder()
                        .tagStringId(questionTag.getOid())
                        .tagString(questionTag.getTagString())
                        .build())
                .collect(Collectors.toList());
    }
    public Boolean delete(Long tagStringId) {

        QuestionTag questionTag = questionTagRepository.findActiveById(tagStringId)
                .orElseThrow(() -> new QuestionTagNotFoundException("Question tag not found"));
        return questionTagRepository.softDeleteById(questionTag.getOid());
    }

    public Optional<QuestionTag> findActiveById(Long questTagOid) {

        return questionTagRepository.findActiveById(questTagOid);
    }

    public void save(QuestionTag questionTag) {

        questionTagRepository.save(questionTag);
    }

    public Optional<QuestionTag> findById(Long tag) {

        return questionTagRepository.findById(tag);
    }

    public QuestionTag updateTagByTagString(String tagString,String newTagString) {
        QuestionTag questionTag = questionTagRepository.findOptionalByTagString(tagString)
                .orElseThrow(() -> new QuestionTagNotFoundException("Question Tag not found"));
        questionTag.setTagString(newTagString);
        return questionTagRepository.save(questionTag);
    }

    public boolean deleteByTagString(String tagString) {
        QuestionTag questionTag = questionTagRepository.findOptionalByTagString(tagString)
                .orElseThrow(() -> new QuestionTagNotFoundException("Question tag not found"));
        return questionTagRepository.softDeleteById(questionTag.getOid());
    }
}
