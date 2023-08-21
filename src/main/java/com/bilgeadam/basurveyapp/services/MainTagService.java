package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.MainTagResponseDto;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.entity.enums.Tags;
import com.bilgeadam.basurveyapp.entity.tags.MainTag;
import com.bilgeadam.basurveyapp.exceptions.custom.TagNotFoundException;
import com.bilgeadam.basurveyapp.mapper.MainTagMapper;
import com.bilgeadam.basurveyapp.repositories.MainTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Muhammed Furkan Türkmen
 */
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
     * 1_ buildırları mapperlar ile yapmak lazım.
     * 2_ controller de kisi tek bir yerden tag olusturacak artık.
     * her biri icin ayrı ayrı kayıt islemine gerek yok
     */
    public void createMainTag (CreateMainTagRequestDto dto){
        deleteByTagName(dto.getTagName());
            for(String tagClass: dto.getTagClass()){
                Tags tag_class= Tags.valueOf(tagClass.trim().toUpperCase());
                // main tag icinde bu classa ait bir bu isimde bir tag var mı diye kontrol edilir.
                if (mainTagRepository.isTagClassAndTagName(tag_class, dto.getTagName())) {
                    // burada hata fırlatınca listede ki diger tagleri kontrol etmiyor bu yüzden bos birakildi.
                    Optional<MainTag> mainTagsDeleted= mainTagRepository.findOptionalByTagNameAndTagClassAndState(dto.getTagName(),Tags.valueOf(tagClass),State.DELETED);

                    if(mainTagsDeleted.isPresent()){
                        MainTag mainTagD = mainTagsDeleted.get();
                        mainTagD.setState(State.ACTIVE);
                        mainTagRepository.save(mainTagD);

                    if (tag_class.equals(Tags.QUESTION)) {
                        questionTagService.activeByTagString(mainTagD.getTagName());

                    } else if (tag_class.equals(Tags.STUDENT)) {

                        studentTagService.activeByTagString(mainTagD.getTagName());

                    } else if (tag_class.equals(Tags.SURVEY)) {

                        surveyTagService.activeByTagString(mainTagD.getTagName());

                    } else if (tag_class.equals(Tags.TRAINER)) {

                        trainerTagService.activeByTagString(mainTagD.getTagName());

                    } else {
                        throw new TagNotFoundException("tag not found");
                    }
                    }
                }else {
                    Optional<MainTag> mainTagsActive = mainTagRepository.findOptionalByTagNameAndTagClassAndState(dto.getTagName(), Tags.valueOf(tagClass), State.ACTIVE);
                    if (mainTagsActive.isEmpty()){
                        MainTag mainTag = mainTagRepository.save(MainTag.builder()
                                .tagClass(tag_class)
                                .tagName(dto.getTagName())
                                .build());

                    if (tag_class.equals(Tags.QUESTION)) {
                        questionTagService.createTag(CreateTagDto.builder()
                                .tagString(dto.getTagName())
                                .mainTagOid(mainTag.getOid())
                                .build());

                    } else if (tag_class.equals(Tags.STUDENT)) {
                        System.out.println("student tag kayit");

                        studentTagService.createTag(CreateTagDto.builder()
                                .tagString(dto.getTagName())
                                .mainTagOid(mainTag.getOid())
                                .build());

                    } else if (tag_class.equals(Tags.SURVEY)) {
                        System.out.println("survey tag kayit");

                        surveyTagService.createTag(CreateTagDto.builder()
                                .tagString(dto.getTagName())
                                .mainTagOid(mainTag.getOid())
                                .build());

                    } else if (tag_class.equals(Tags.TRAINER)) {
                        System.out.println("trainer tag kayit");

                        trainerTagService.createTag(CreateTagDto.builder()
                                .tagString(dto.getTagName())
                                .mainTagOid(mainTag.getOid())
                                .build());

                    } else {
                        throw new TagNotFoundException("tag not found");
                    }

                    }
                }
            }
    }
    public List<MainTagResponseDto> findByTagNames(String tagName) {
        List<MainTag> mainTags= findByTagName(tagName);
        List<MainTagResponseDto> mainTagResponseDtos = new ArrayList<>();
        for (MainTag mainTag: mainTags){
            mainTagResponseDtos.add(MainTagMapper.INSTANCE.toDto(mainTag));
        }
        return mainTagResponseDtos;
    }


    public Boolean updateTagByTagName(UpdateTagNameDto dto) {
        if (dto.getTagString().equals(null) || dto.getTagString()==""){
            throw new TagNotFoundException("Tag name not empty ");
        }
        List<MainTag> mainTags = findByTagName(dto.getTagString());
        System.out.println("tag name list : "+ mainTags.toString());

        for (MainTag mainTag: mainTags){
            mainTag.setTagName(dto.getNewTagString());
            mainTagRepository.save(mainTag);
            if (mainTag.getTagClass().equals(Tags.QUESTION)){
                questionTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
            } else if (mainTag.getTagClass().equals(Tags.TRAINER)) {
                trainerTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
            } else if (mainTag.getTagClass().equals(Tags.SURVEY)) {
                surveyTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
            } else if (mainTag.getTagClass().equals(Tags.STUDENT)) {
                studentTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
            }else {
                throw new TagNotFoundException("böyle bir tag adi bulunamadi");
            }
        }

        return true;
    }
    public Boolean updateTagByTagNameAndTagClassesFrontEnd(UpdateTagNameAndTagClassesDto dto){
        if (dto.getTagString().equals(null) || dto.getTagString()==""){
            throw new TagNotFoundException("Tag name not empty ");
        }
        List<MainTag> mainTags = findByTagName(dto.getTagString());
        System.out.println("tag name list : "+ mainTags.toString());

        for (MainTag mainTag: mainTags){
            mainTag.setTagName(dto.getNewTagString());
            mainTag.setState(State.DELETED);
            mainTagRepository.save(mainTag);
            if (mainTag.getTagClass().equals(Tags.QUESTION)){
                questionTagService.deleteByTagString(dto.getTagString());
                questionTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
            } else if (mainTag.getTagClass().equals(Tags.TRAINER)) {
                trainerTagService.deleteByTagString(dto.getTagString());
                trainerTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
            } else if (mainTag.getTagClass().equals(Tags.SURVEY)) {
                surveyTagService.deleteByTagString(dto.getTagString());
                surveyTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
            } else if (mainTag.getTagClass().equals(Tags.STUDENT)) {
                studentTagService.deleteByTagString(dto.getTagString());
                studentTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
            }else {
                throw new TagNotFoundException("böyle bir tag adi bulunamadi");
            }
        }
        for(String tagClass: dto.getTagClass()){
            Tags tag_class= Tags.valueOf(tagClass.trim().toUpperCase());
            // main tag icinde bu classa ait bir bu isimde bir tag var mı diye kontrol edilir.
            if (mainTagRepository.isTagClassAndTagName(tag_class, dto.getNewTagString())) {
                // burada hata fırlatınca listede ki diger tagleri kontrol etmiyor bu yüzden bos birakildi.
                Optional<MainTag> mainTagsDeleted= mainTagRepository.findOptionalByTagNameAndTagClassAndState(dto.getNewTagString(),Tags.valueOf(tagClass),State.DELETED);

                if(mainTagsDeleted.isPresent()){
                    MainTag mainTagNTC = mainTagsDeleted.get();
                    mainTagNTC.setState(State.ACTIVE);
                    mainTagRepository.save(mainTagNTC);

                    if (tag_class.equals(Tags.QUESTION)) {
                        questionTagService.activeByTagString(mainTagNTC.getTagName());

                    } else if (tag_class.equals(Tags.STUDENT)) {

                        studentTagService.activeByTagString(mainTagNTC.getTagName());

                    } else if (tag_class.equals(Tags.SURVEY)) {

                        surveyTagService.activeByTagString(mainTagNTC.getTagName());

                    } else if (tag_class.equals(Tags.TRAINER)) {

                        trainerTagService.activeByTagString(mainTagNTC.getTagName());

                    } else {
                        throw new TagNotFoundException("tag not found");
                    }
                }
            }else {
                MainTag mainTag= mainTagRepository.save(MainTag.builder()
                        .tagClass(tag_class)
                        .tagName(dto.getNewTagString())
                        .build());

                if (tag_class.equals(Tags.QUESTION)){
                    questionTagService.createTag(CreateTagDto.builder()
                            .tagString(dto.getNewTagString())
                            .mainTagOid(mainTag.getOid())
                            .build());

                } else if (tag_class.equals(Tags.STUDENT)) {
                    System.out.println("student tag kayit");

                    studentTagService.createTag(CreateTagDto.builder()
                            .tagString(dto.getNewTagString())
                            .mainTagOid(mainTag.getOid())
                            .build());

                } else if (tag_class.equals(Tags.SURVEY)) {
                    System.out.println("survey tag kayit");

                    surveyTagService.createTag(CreateTagDto.builder()
                            .tagString(dto.getNewTagString())
                            .mainTagOid(mainTag.getOid())
                            .build());

                } else if (tag_class.equals(Tags.TRAINER)) {
                    System.out.println("trainer tag kayit");

                    trainerTagService.createTag(CreateTagDto.builder()
                            .tagString(dto.getNewTagString())
                            .mainTagOid(mainTag.getOid())
                            .build());

                }else {
                    throw new TagNotFoundException("tag not found");
                }



            }
        }
        return true;
    }

    public boolean updateTagByTagNameAndTagClass(UpdateTagDto dto) {
        if (dto.getTagClass().equals(null) ||dto.getTagClass()==""){
            throw new TagNotFoundException("Tag title not found");
        }
        if (dto.getTagString().equals(null) || dto.getTagString()==""){
            throw new TagNotFoundException("Tag name not empty ");
        }

        Tags tagClass= Tags.valueOf(dto.getTagClass());
        MainTag mainTag=findByTagNameAndTagClass(dto.getTagString(),tagClass);
        mainTagRepository.save(mainTag);

        if (mainTag.getTagClass().equals(Tags.QUESTION)){
            questionTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
        } else if (mainTag.getTagClass().equals(Tags.TRAINER)) {
            trainerTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
        } else if (mainTag.getTagClass().equals(Tags.SURVEY)) {
            surveyTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
        } else if (mainTag.getTagClass().equals(Tags.STUDENT)) {
            studentTagService.updateTagByTagString(dto.getTagString(),dto.getNewTagString());
        }else {
            throw new TagNotFoundException("Tag not found");
        }
        return true;
    }

    public List<MainTagResponseDto> findAllTags(){
        List<MainTagResponseDto> mainTagResponseDtos = mainTagRepository.findAllActive().stream().map(x->MainTagMapper.INSTANCE.toDto(x)).toList();
        return mainTagResponseDtos;
    }


    public MainTag findByTagNameAndTagClass(String tagName,Tags tagClass) {
        MainTag mainTag= mainTagRepository.findOptionalByTagNameAndTagClass(tagName,tagClass)
                .orElseThrow(() -> new TagNotFoundException("tag not found"));
        return mainTag;
    }

    public List<MainTag> findByTagName(String tagName) {
        List<MainTag> mainTags= mainTagRepository.findOptionalByTagName(tagName).orElseThrow(() -> new TagNotFoundException("tag not found"));
        if (mainTags.size()==0 ){
            throw new TagNotFoundException("Tag Name Not Found");
        }
        return mainTags;
    }
    public MainTagResponseDto findByTagNameAndTagClass(String tagName,String tagClass) {
        MainTag mainTag= findByTagNameAndTagClass(tagName,Tags.valueOf(tagClass));
        return MainTagMapper.INSTANCE.toDto(mainTag);
    }
    public List<MainTag> findOptionalByTagClass(String tagClass){
        List<MainTag> mainTags=mainTagRepository.findOptionalByTagClass(Tags.valueOf(tagClass)).orElseThrow(()->new TagNotFoundException("tag class not found"));
        return mainTags;
    }

    public Boolean deleteByTagName(String tagName) {
        Optional<List<MainTag>> mainTags= mainTagRepository.findOptionalByTagNameAndState(tagName,State.ACTIVE);
        if(mainTags.isPresent()) {
            for (MainTag mainTag : mainTags.get()) {
                if (mainTag.getTagClass().equals(Tags.QUESTION)) {
                    mainTagRepository.softDeleteById(mainTag.getOid());
                    questionTagService.deleteByTagString(mainTag.getTagName());
                } else if (mainTag.getTagClass().equals(Tags.TRAINER)) {
                    mainTagRepository.softDeleteById(mainTag.getOid());
                    trainerTagService.deleteByTagString(mainTag.getTagName());
                } else if (mainTag.getTagClass().equals(Tags.SURVEY)) {
                    mainTagRepository.softDeleteById(mainTag.getOid());
                    surveyTagService.deleteByTagString(mainTag.getTagName());
                } else if (mainTag.getTagClass().equals(Tags.STUDENT)) {
                    mainTagRepository.softDeleteById(mainTag.getOid());
                    studentTagService.deleteByTagString(mainTag.getTagName());
                } else {
                    throw new TagNotFoundException("böyle bir tag adi bulunamadi");
                }
            }
        }else{
            throw new TagNotFoundException("böyle bir tag adi bulunamadi");
        }
        return true;
    }


}
