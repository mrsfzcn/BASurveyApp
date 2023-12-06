package com.bilgeadam.basurveyapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bilgeadam.basurveyapp.dto.request.CreateMainTagRequestDto;
import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateTagDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateTagNameDto;
import com.bilgeadam.basurveyapp.dto.response.MainTagResponseDto;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.entity.enums.Tags;
import com.bilgeadam.basurveyapp.entity.tags.MainTag;
import com.bilgeadam.basurveyapp.exceptions.custom.TagNotFoundException;
import com.bilgeadam.basurveyapp.repositories.MainTagRepository;
import com.bilgeadam.basurveyapp.services.MainTagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MainTagServiceTest {

    @InjectMocks
    private MainTagService mainTagService;

    @Mock
    private MainTagRepository mainTagRepository;

    @Mock
    private QuestionTagService questionTagService;

    @Mock
    private StudentTagService studentTagService;

    @Mock
    private SurveyTagService surveyTagService;

    @Mock
    private TrainerTagService trainerTagService;

    @Mock
    private UserTagService userTagService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateMainTag_Success() {
        CreateMainTagRequestDto dto = new CreateMainTagRequestDto();
        dto.setTagClass(List.of(new String[]{"QUESTION"}));
        dto.setTagName("ExampleTag");

        when(mainTagRepository.isTagClassAndTagName(Tags.QUESTION, "ExampleTag")).thenReturn(false);

        mainTagService.createMainTag(dto);

        verify(mainTagRepository, times(1)).save(any(MainTag.class));
        verify(questionTagService, times(1)).createTag(any(CreateTagDto.class));
    }

    @Test
    public void testCreateMainTag_TagAlreadyExists() {
        CreateMainTagRequestDto dto = new CreateMainTagRequestDto();
        dto.setTagClass(List.of(new String[]{"QUESTION"}));
        dto.setTagName("ExistingTag");

        when(mainTagRepository.isTagClassAndTagName(Tags.QUESTION, "ExistingTag")).thenReturn(true);

        assertThrows(TagNotFoundException.class, () -> mainTagService.createMainTag(dto));

        verify(mainTagRepository, never()).save(any(MainTag.class));
        verify(questionTagService, never()).createTag(any(CreateTagDto.class));
    }

    @Test
    public void testUpdateTagByTagName_Success() {
        UpdateTagNameDto dto = new UpdateTagNameDto();
        dto.setTagString("OldTagName");
        dto.setNewTagString("NewTagName");

        when(mainTagRepository.findByTagNameAndTagClassAndState("OldTagName", Tags.QUESTION, State.ACTIVE))
                .thenReturn(Optional.of(new MainTag()));

        boolean result = mainTagService.updateTagByTagName(dto);

        assertTrue(result);
        verify(mainTagRepository, times(1)).save(any(MainTag.class));
        verify(questionTagService, times(1)).updateTagByTagString(eq("OldTagName"), eq("NewTagName"));
    }

    @Test
    public void testUpdateTagByTagName_TagNotFound() {
        UpdateTagNameDto dto = new UpdateTagNameDto();
        dto.setTagString("NonExistingTag");
        dto.setNewTagString("NewTagName");

        when(mainTagRepository.findByTagNameAndTagClassAndState("NonExistingTag", Tags.QUESTION, State.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> mainTagService.updateTagByTagName(dto));

        verify(mainTagRepository, never()).save(any(MainTag.class));
        verify(questionTagService, never()).updateTagByTagString(anyString(), anyString());
    }

    @Test
    public void testUpdateTagByTagNameAndTagClass_Success() {
        UpdateTagDto dto = new UpdateTagDto();
        dto.setTagString("OldTagName");
        dto.setNewTagString("NewTagName");
        dto.setTagClass("QUESTION");

        when(mainTagRepository.findByTagNameAndTagClass("OldTagName", Tags.QUESTION))
                .thenReturn(Optional.of(new MainTag()));

        boolean result = mainTagService.updateTagByTagNameAndTagClass(dto);

        assertTrue(result);
        verify(mainTagRepository, times(1)).save(any(MainTag.class));
        verify(questionTagService, times(1)).updateTagByTagString(eq("OldTagName"), eq("NewTagName"));
    }

    @Test
    public void testUpdateTagByTagNameAndTagClass_TagNotFound() {
        UpdateTagDto dto = new UpdateTagDto();
        dto.setTagString("NonExistingTag");
        dto.setNewTagString("NewTagName");
        dto.setTagClass("QUESTION");

        when(mainTagRepository.findByTagNameAndTagClass("NonExistingTag", Tags.QUESTION))
                .thenReturn(null);

        assertThrows(TagNotFoundException.class, () -> mainTagService.updateTagByTagNameAndTagClass(dto));

        verify(mainTagRepository, never()).save(any(MainTag.class));
        verify(questionTagService, never()).updateTagByTagString(anyString(), anyString());
    }

    @Test
    public void testDeleteByTagName_Success() {
        String tagName = "ExampleTag";

        when(mainTagRepository.findOptionalByTagNameAndState(tagName, State.ACTIVE))
                .thenReturn(Optional.of(List.of(new MainTag())));

        boolean result = mainTagService.deleteByTagName(tagName);

        assertTrue(result);
        verify(mainTagRepository, times(1)).softDeleteById(anyLong());
        verify(questionTagService, times(1)).deleteByTagString(tagName);
    }

    @Test
    public void testDeleteByTagName_TagNotFound() {
        String tagName = "NonExistingTag";

        when(mainTagRepository.findOptionalByTagNameAndState(tagName, State.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> mainTagService.deleteByTagName(tagName));

        verify(mainTagRepository, never()).softDeleteById(anyLong());
        verify(questionTagService, never()).deleteByTagString(anyString());
    }

    @Test
    public void testFindByTagNames_Success() {
        String tagName = "ExampleTag";

        when(mainTagRepository.findByTagName(tagName)).thenReturn(List.of(new MainTag()));

        List<MainTagResponseDto> result = mainTagService.findByTagNames(tagName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tagName, result.get(0).getTagName());
    }

    @Test
    public void testFindByTagNames_TagNotFound() {
        String tagName = "NonExistingTag";

        when(mainTagRepository.findByTagName(tagName)).thenReturn(new ArrayList<>());

        assertThrows(TagNotFoundException.class, () -> mainTagService.findByTagNames(tagName));
    }

    @Test
    public void testFindAllTags_Success() {
        when(mainTagRepository.findAllActive()).thenReturn(List.of(new MainTag()));

        List<MainTagResponseDto> result = mainTagService.findAllTags();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindAllTags_NoActiveTags() {
        when(mainTagRepository.findAllActive()).thenReturn(new ArrayList<>());

        List<MainTagResponseDto> result = mainTagService.findAllTags();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
