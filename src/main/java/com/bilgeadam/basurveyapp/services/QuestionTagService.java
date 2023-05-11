package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
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

    public void createTag(CreateTagDto dto) {

        Optional<QuestionTag> questionTag1 = questionTagRepository.findOptionalByTagString(dto.getTagString());
        if(questionTag1.isPresent()){
            throw new QuestionTagExistException("Question Tag already exist!");
        }
        QuestionTag questionTag = QuestionTag.builder()
                .tagString(dto.getTagString())
                .build();
        questionTagRepository.save(questionTag);
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
}
