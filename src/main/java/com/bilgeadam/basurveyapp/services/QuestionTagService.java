package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
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

    //TODO: Bunu dto ile yapmamız lazım.
    public void createTag(CreateTagDto dto) {
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
            throw new RuntimeException("Tag is not found");
        } else {
            QuestionTag questionTag = deleteTag.get();
            questionTagRepository.softDelete(questionTag);
            return true;
        }

    }
}
