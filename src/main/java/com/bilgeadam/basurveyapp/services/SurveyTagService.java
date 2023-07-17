package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.entity.tags.SurveyTag;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyTagExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyTagNotFoundException;
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

    public SurveyTag createTag(CreateTagDto dto) {
        System.out.println("survey service create tag");

        Optional<SurveyTag> existingSurveyTag = surveyTagRepository.findOptionalByTagString(dto.getTagString());

        if (existingSurveyTag.isPresent()) {
            SurveyTag surveyTag = existingSurveyTag.get();

            if (surveyTag.getState() == State.ACTIVE) {
                throw new SurveyTagExistException("Survey Tag already exists!");
            } else if (surveyTag.getState() == State.DELETED) {
                surveyTag.setState(State.ACTIVE);
                return surveyTagRepository.save(surveyTag);
            }
        }
        SurveyTag surveyTag = SurveyTag.builder()
                .tagString(dto.getTagString())
                .state(State.ACTIVE)
                .mainTagOid(dto.getMainTagOid())

                .build();
        return surveyTagRepository.save(surveyTag);
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
            throw new SurveyTagNotFoundException("Survey Tag is not found");
        } else {
            return surveyTagRepository.softDeleteById(deleteTag.get().getOid());
        }
    }
    public SurveyTag updateTagByTagString( String tagString,String newTagString) {
        SurveyTag surveyTag = surveyTagRepository.findOptionalByTagString(tagString)
                .orElseThrow(() -> new SurveyTagNotFoundException("Survey Tag not found"));

        surveyTag.setTagString(newTagString);

        return surveyTagRepository.save(surveyTag);
    }
    public boolean deleteByTagString(String tagString) {
        SurveyTag questionTag = surveyTagRepository.findOptionalByTagString(tagString)
                .orElseThrow(() -> new SurveyTagNotFoundException("Survey tag not found"));
        return surveyTagRepository.softDeleteById(questionTag.getOid());
    }

    public Optional<SurveyTag> findActiveById(Long surveyTagOid) {
        return surveyTagRepository.findActiveById(surveyTagOid);
    }

    public void save(SurveyTag surveyTag) {
        surveyTagRepository.save(surveyTag);
    }
}
