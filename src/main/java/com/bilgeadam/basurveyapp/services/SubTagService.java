package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.SubTag;
import com.bilgeadam.basurveyapp.entity.Tag;
import com.bilgeadam.basurveyapp.repositories.SubTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubTagService {

    private SubTagRepository subTagRepository;

    public Boolean createTag(SubTag subTag) {
        SubTag tag1 = SubTag.builder()
                .subTagString(subTag.getSubTagString())
                .build();
        subTagRepository.save(tag1);
        return true;
    }
}
