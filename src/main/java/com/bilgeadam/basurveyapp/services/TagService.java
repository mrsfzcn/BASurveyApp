package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionDto;
import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.SubtagResponseDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.SubTag;
import com.bilgeadam.basurveyapp.entity.Tag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTypeNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyNotFoundException;
import com.bilgeadam.basurveyapp.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public void createTag(CreateTagDto dto) {
        Tag tag = Tag.builder()
                .tagString(dto.getTagString())
                .build();
        tagRepository.save(tag);
    }
    public List<TagResponseDto> findAll() {
        List<Tag> findAllList = tagRepository.findAllActive();
        List<TagResponseDto> responseDtoList = new ArrayList<>();
        findAllList.forEach(tag ->
                responseDtoList.add(TagResponseDto.builder()
                        .tagStringId(tag.getOid())
                        .tagString(tag.getTagString())
                        .build()));
        return responseDtoList;
    }
    public Boolean delete(Long tagStringId) {
        Optional<Tag> deleteTag = tagRepository.findActiveById(tagStringId);
        if (deleteTag.isEmpty()) {
            throw new RuntimeException("Tag is not found");
        } else {
            Tag tag = deleteTag.get();
            tagRepository.softDelete(tag);
            return true;
        }

    }
}
