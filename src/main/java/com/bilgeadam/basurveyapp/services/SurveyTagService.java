package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.entity.tags.SurveyTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyTagExistException;
import com.bilgeadam.basurveyapp.repositories.SurveyTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyTagService {

    private final SurveyTagRepository surveyTagRepository;

    public void createTag(CreateTagDto dto) {
        Optional<SurveyTag> surveyTag1 = surveyTagRepository.findOptionalByTagString(dto.getTagString());
        if(surveyTag1.isPresent()){
            throw new SurveyTagExistException("Survey Tag already exist!");
        }

        SurveyTag surveyTag = SurveyTag.builder()
                .tagString(dto.getTagString())
                .build();
        surveyTagRepository.save(surveyTag);
    }

    public List<TagResponseDto> findAll() {
        List<SurveyTag> findAllList = surveyTagRepository.findAllActive();
        List<TagResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(surveyTag ->
                responseDtoList.add(TagResponseDto.builder()
                        .tagStringId(surveyTag.getOid())
                        .tagString(surveyTag.getTagString())
                        .build()));
        return responseDtoList;
    }

    public Boolean delete(Long tagStringId) {
        Optional<SurveyTag> deleteTag = surveyTagRepository.findActiveById(tagStringId);
        if (deleteTag.isEmpty()) {
            throw new RuntimeException("Tag is not found");
        } else {
            return surveyTagRepository.softDeleteById(deleteTag.get().getOid());
        }
    }
}
