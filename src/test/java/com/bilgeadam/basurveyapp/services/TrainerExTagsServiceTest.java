package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.TrainerExTags;
import com.bilgeadam.basurveyapp.repositories.TrainerExTagsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class TrainerExTagsServiceTest {

    @InjectMocks
    private TrainerExTagsService trainerExTagsService;

    @Mock
    private TrainerExTagsRepository trainerExTagsRepository;

    public TrainerExTagsServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveExTrainerTag() {
        TrainerExTags trainerExTags = new TrainerExTags();
        trainerExTagsService.saveExTrainerTag(trainerExTags);
        verify(trainerExTagsRepository, times(1)).save(trainerExTags);
    }
}
