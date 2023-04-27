package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagNotFoundException;
import com.bilgeadam.basurveyapp.repositories.QuestionTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<QuestionTag> findAllList = questionTagRepository.findAllActive();
        List<TagResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(questionTag ->
                responseDtoList.add(TagResponseDto.builder()
                        .tagStringId(questionTag.getOid())
                        .tagString(questionTag.getTagString())
                        .build()));
        return responseDtoList;
    }
    public Boolean delete(Long tagStringId) {
        Optional<QuestionTag> deleteTag = questionTagRepository.findActiveById(tagStringId);
        if (deleteTag.isEmpty()) {
            throw new QuestionTagNotFoundException("Question tag not found");
        } else {
            return questionTagRepository.softDeleteById(deleteTag.get().getOid());
        }

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
