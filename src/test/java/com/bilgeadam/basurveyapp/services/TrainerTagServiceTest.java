package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.FindActiveTrainerTagByIdResponseDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerTagDetailResponseDto;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.exceptions.custom.TrainerTagExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.TrainerTagNotFoundException;
import com.bilgeadam.basurveyapp.repositories.TrainerRepository;
import com.bilgeadam.basurveyapp.repositories.TrainerTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Mert Cömertoğlu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainerTagServiceTest {

    @Mock
    private TrainerTagRepository trainerTagRepository;

    @Mock
    private TrainerService trainerService;

    private TrainerTagService trainerTagService;
    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
       trainerTagService = new TrainerTagService(trainerTagRepository,trainerService);
    }

    @Test
    public void testCreateTag() {
        CreateTagDto createTagDto = new CreateTagDto("New Tag");
        when(trainerTagRepository.findByTrainerTagName(createTagDto.getTagString())).thenReturn(Optional.empty());

        trainerTagService.createTag(createTagDto);

        verify(trainerTagRepository).save(any(TrainerTag.class));
    }

    @Test
    public void testCreateTag_TrainerTagExistException() {
        CreateTagDto createTagDto = new CreateTagDto("Existing Tag");
        TrainerTag existingTag = new TrainerTag();
        existingTag.setTagString("Existing Tag");
        when(trainerTagRepository.findByTrainerTagName(createTagDto.getTagString())).thenReturn(Optional.of(existingTag));

        try{
            trainerTagService.createTag(createTagDto);
        } catch (TrainerTagExistException e){
            assertEquals("Trainer Tag already exist!", e.getMessage());
        }
    }

    @Test
    public void testDeleteTrainerTag() {
        Long tagOid = 1L;
        TrainerTag tag = new TrainerTag();
        tag.setOid(tagOid);

        when(trainerTagRepository.findActiveById(tagOid)).thenReturn(Optional.of(tag));
        when(trainerTagRepository.softDeleteById(tagOid)).thenReturn(true);

        boolean result = trainerTagService.delete(tagOid);
        assertTrue(result);

        verify(trainerTagRepository).findActiveById(tagOid);
        verify(trainerTagRepository).softDeleteById(tagOid);
    }

    @Test
    public void testDeleteTrainerTagWhenRuntimeException() {
        Long id = 1L;
        when(trainerTagRepository.findActiveById(id)).thenReturn(Optional.empty());

        try {
            trainerTagService.delete(id);
        }catch (RuntimeException e){
            assertEquals("Trainer tag not found", e.getMessage());
        }
    }

    @Test
    public void testGetTrainerTagsOids() {
        Trainer trainer = new Trainer();
        trainer.setOid(1L);

        Set<TrainerTag> trainerTags = new HashSet<>();
        TrainerTag tag1 = new TrainerTag();
        tag1.setOid(1L);
        tag1.setTagString("Tag1");
        TrainerTag tag2 = new TrainerTag();
        tag2.setOid(2L);
        tag2.setTagString("Tag2");
        trainerTags.add(tag1);
        trainerTags.add(tag2);

        when(trainerTagRepository.findActiveTrainerTagsByTrainerId(trainer.getOid())).thenReturn(trainerTags);

        Set<Long> trainerTagOids = trainerTagService.getTrainerTagsOids(trainer);

        assertEquals(2, trainerTagOids.size());
        assertTrue(trainerTagOids.contains(tag1.getOid()));
        assertTrue(trainerTagOids.contains(tag2.getOid()));
    }

    @Test
    public void testFindActiveByIdWhenTagFound(){
        Long id = 5L;
        TrainerTag tag = TrainerTag.builder()
                .oid(id)
                .tagString("Tag1")
                .state(State.ACTIVE)
                .build();
        when(trainerTagRepository.findActiveById(id)).thenReturn(Optional.of(tag));

        FindActiveTrainerTagByIdResponseDto result = trainerTagService.findActiveById(id);

        assertEquals(tag.getTagString(), result.getTagString());
        assertEquals(id, result.getOid());
        verify(trainerTagRepository).findActiveById(id);
    }

    @Test
    public void testFindActiveByIdWhenTagNotFound() {
        long id = 45L;
        when(trainerTagRepository.findActiveById(id)).thenReturn(Optional.empty());
        try {
            trainerTagService.findActiveById(id);
        }catch (TrainerTagNotFoundException e){
            assertEquals("Trainer tag not found", e.getMessage());
        }

        verify(trainerTagRepository).findActiveById(id);
    }

    @Test
    public void testGetTrainerTags(){

        Set<TrainerTag> trainerTags = new HashSet<>();
        TrainerTag tag1 = TrainerTag.builder()
                .oid(1L)
                .tagString("Tag1")
                .state(State.ACTIVE)
                .build();
        trainerTags.add(tag1);
        TrainerTag tag2 = TrainerTag.builder()
                .oid(2L)
                .tagString("Tag2")
                .state(State.ACTIVE)
                .build();
        trainerTags.add(tag2);
        TrainerTag tag3 = TrainerTag.builder()
                .oid(3L)
                .tagString("Tag3")
                .state(State.DELETED)
                .build();
        trainerTags.add(tag3);

        Trainer trainer = Trainer.builder()
                .user(User.builder()
                        .firstName("Trainer")
                        .lastName("Test")
                        .build())
                .trainerTags(trainerTags)
                .build();

        when(trainerTagRepository.findActiveTrainerTagsByTrainerId(trainer.getOid())).thenReturn(Set.of(tag1, tag2));

        Set<TrainerTag> result = trainerTagService.getTrainerTags(trainer);

        assertEquals(2, result.size());
        assertTrue(result.contains(tag1));
        assertTrue(result.contains(tag2));
        assertFalse(result.contains(tag3));
    }

    @Test
    public void testGetTrainerTagList(){
        List<TrainerTag> trainerTags = new ArrayList<>();
        TrainerTag trainerTag1 = new TrainerTag();
        trainerTag1.setOid(1L);
        trainerTag1.setTagString("tag1");
        trainerTags.add(trainerTag1);
        TrainerTag trainerTag2 = new TrainerTag();
        trainerTag2.setOid(2L);
        trainerTag2.setTagString("tag2");
        trainerTags.add(trainerTag2);
        when(trainerTagRepository.findAllActive()).thenReturn(trainerTags);

        List<TrainerTagDetailResponseDto> responseDtos = new ArrayList<>();
        TrainerTagDetailResponseDto responseDto1 = new TrainerTagDetailResponseDto();
        responseDto1.setOid(trainerTag1.getOid());
        responseDto1.setTagString(trainerTag1.getTagString());
        responseDtos.add(responseDto1);
        TrainerTagDetailResponseDto responseDto2 = new TrainerTagDetailResponseDto();
        responseDto2.setOid(trainerTag2.getOid());
        responseDto2.setTagString(trainerTag2.getTagString());
        responseDtos.add(responseDto2);

        List<TrainerTagDetailResponseDto> result = trainerTagService.getTrainerTagList();

        verify(trainerTagRepository).findAllActive();

        assertEquals(responseDtos, result);
    }

}
