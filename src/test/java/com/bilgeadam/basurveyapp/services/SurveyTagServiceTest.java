package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.entity.tags.SurveyTag;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyTagExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyTagNotFoundException;
import com.bilgeadam.basurveyapp.repositories.SurveyTagRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Mert Cömertoğlu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SurveyTagServiceTest {

    @Mock
    private SurveyTagRepository surveyTagRepository;

    private SurveyTagService surveyTagService;

    @BeforeAll
    public void Init(){
        MockitoAnnotations.openMocks(this);
        surveyTagService= new SurveyTagService(surveyTagRepository);
    }

    @Test
    public void testCreateTag(){

        CreateTagDto createTagDto = CreateTagDto.builder()
                .tagString("test")
                .build();

        SurveyTag surveyTag = SurveyTag.builder()
                .tagString(createTagDto.getTagString())
                .build();

        when(surveyTagRepository.findOptionalByTagString(createTagDto.getTagString())).thenReturn(Optional.empty());

        surveyTagService.createTag(createTagDto);

        verify(surveyTagRepository).save(surveyTag);
    }

    @Test
    public void testCreateTag_SurveyTagExistException(){
        CreateTagDto createTagDto = CreateTagDto.builder()
                .tagString("test")
                .build();
        SurveyTag surveyTag = SurveyTag.builder()
                .oid(1L)
                .state(State.ACTIVE)
                .tagString(createTagDto.getTagString())
                .build();
        when(surveyTagRepository.findOptionalByTagString(createTagDto.getTagString())).thenReturn(Optional.of(surveyTag));
        try {
            surveyTagService.createTag(createTagDto);
        }catch (SurveyTagExistException e){
            assertEquals("Survey Tag already exist!", e.getMessage());
        }
    }

    @Test
    public void testFindAll(){
        List<SurveyTag> findAllList = new ArrayList<>();
        SurveyTag test1 = SurveyTag.builder()
                .oid(1L)
                .state(State.ACTIVE)
                .tagString("test1")
                .build();
        findAllList.add(test1);
        findAllList.add(SurveyTag.builder()
                .oid(2L)
                .state(State.ACTIVE)
                .tagString("test2")
                .build());

        when(surveyTagRepository.findAllActive()).thenReturn(findAllList);

        List<TagResponseDto> result = surveyTagService.findAll();

        assertEquals(2,result.size());
        assertEquals("test2",result.get(1).getTagString());
    }

    @Test
    public void testDeleteTag_SurveyTagNotFoundException() {
        when(surveyTagRepository.findActiveById(1L)).thenReturn(Optional.empty());
        try {
            surveyTagService.delete(2L);
        }catch (SurveyTagNotFoundException e){
            assertEquals("Survey Tag is not found", e.getMessage());
        }
    }

    @Test
    public void testDeleteTag(){
        SurveyTag surveyTag = SurveyTag.builder()
                .oid(1L)
                .tagString("test")
                .state(State.ACTIVE)
                .build();
        when(surveyTagRepository.findActiveById(surveyTag.getOid())).thenReturn(Optional.of(surveyTag));

        surveyTagService.delete(surveyTag.getOid());

        verify(surveyTagRepository).softDeleteById(surveyTag.getOid());
    }
}
