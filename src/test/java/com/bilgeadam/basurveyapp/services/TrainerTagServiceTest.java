package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.exceptions.custom.TrainerTagExistException;
import com.bilgeadam.basurveyapp.repositories.TrainerTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private TrainerTagService trainerTagService;

    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
        trainerTagService = new TrainerTagService(trainerTagRepository);
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

}
