package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateMainTagRequestDto;
import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.entity.enums.Tags;
import com.bilgeadam.basurveyapp.entity.tags.MainTag;
import com.bilgeadam.basurveyapp.repositories.MainTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainTagService {
    private final MainTagRepository mainTagRepository;
    private final QuestionTagService questionTagService;
    private final StudentTagService studentTagService;
    private final SurveyTagService surveyTagService;
    private final TrainerTagService trainerTagService;
    private final UserTagService userTagService;

    /**
     * düzeltilmesi gerekenler:
     * 1_ diger servislerde dto almaya gerek yok
     * 2_ builerları mapperlar ile yapmak lazım.
     * 3_ controller de kisi tek bir yerden tag olusturacak artık.
     * her biri icin ayrı ayrı kayıt islemine gerek yok
     */
    public void createTag (CreateMainTagRequestDto dto){
        for(String tagClass: dto.getTagClass()){
            // main tag icinde bu classa ait bir bu isimde bir tag var mı diye kontrol edilir.
            if (mainTagRepository.isTagClassAndTagName(Tags.valueOf(tagClass), dto.getTagName())){
                System.out.println("zaten var");
            }else {
                // class kontrolleri saglanarak buradan create metotlarına gonderilir.

                if (Tags.valueOf(tagClass).equals(Tags.QUESTION)){
                    System.out.println("queistion tag kayit");
                    questionTagService.createTag(CreateTagDto.builder()
                                    .tagString(dto.getTagName())
                            .build());
                    mainTagRepository.save(MainTag.builder()
                                    .tagClass(Tags.valueOf(tagClass))
                                    .tagName(dto.getTagName())
                            .build());
                } else if (Tags.valueOf(tagClass).equals(Tags.STUDENT)) {
                    System.out.println("student tag kayit");

                    studentTagService.createTag(CreateTagDto.builder()
                                    .tagString(dto.getTagName())
                            .build());
                    mainTagRepository.save(MainTag.builder()
                            .tagClass(Tags.valueOf(tagClass))
                            .tagName(dto.getTagName())
                            .build());
                } else if (Tags.valueOf(tagClass).equals(Tags.SURVEY)) {
                    System.out.println("survey tag kayit");

                    surveyTagService.createTag(CreateTagDto.builder()
                                    .tagString(dto.getTagName())
                            .build());
                    mainTagRepository.save(MainTag.builder()
                            .tagClass(Tags.valueOf(tagClass))
                            .tagName(dto.getTagName())
                            .build());
                } else if (Tags.valueOf(tagClass).equals(Tags.TRAINER)) {
                    System.out.println("trainer tag kayit");

                    trainerTagService.createTag(CreateTagDto.builder()
                                    .tagString(dto.getTagName())
                            .build());
                    mainTagRepository.save(MainTag.builder()
                            .tagClass(Tags.valueOf(tagClass))
                            .tagName(dto.getTagName())
                            .build());
                }else {
                    System.out.println("böyle bir tag classi bulunamadi.");
                }

            }

        }


    }
}
