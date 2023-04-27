package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.TrainerUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.exceptions.custom.TrainerNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.TrainerTagNotFoundException;
import com.bilgeadam.basurveyapp.repositories.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Mert Cömertoğlu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainerServiceTest {

    private TrainerService trainerService;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainerTagService trainerTagService;

    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
        trainerService = new TrainerService(trainerRepository,trainerTagService);
    }

    @Test
    public void testCreateTrainer(){

        User user = User.builder()
                .email("ad.soyad@bilgeadam.com")
                .password("12345678")
                .firstName("ad")
                .lastName("soyad")
                .build();

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerRepository.save(trainer)).thenReturn(trainer);
        Boolean result = trainerService.createTrainer(trainer);
        verify(trainerRepository).save(trainer);
        assertEquals(true, result);
    }

    @Test
    void testFindTrainerByUserOid() {
        Long oid = 123L;
        User user = new User();
        user.setOid(oid);

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerRepository.findTrainerByUserOid(oid)).thenReturn(Optional.of(trainer));
        Optional<Trainer> result = trainerService.findTrainerByUserOid(oid);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
        verify(trainerRepository, times(1)).findTrainerByUserOid(oid);
    }

    @Test
    void testFindActiveById() {
        Long oid = 123L;
        User user = new User();
        user.setOid(oid);

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerRepository.findActiveById(oid)).thenReturn(Optional.of(trainer));
        Optional<Trainer> result = trainerService.findActiveById(oid);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
        verify(trainerRepository, times(1)).findActiveById(oid);

    }
    @Test
    void testGetMasterTrainerList() {
        List<Trainer> masterTrainers = new ArrayList<>();

        Trainer trainer1 = Trainer.builder()
                .isMasterTrainer(true)
                .user(User.builder()
                        .firstName("master")
                        .lastName("trainer1")
                        .email("master.trainer1@bilgeadam.com")
                        .build())
                .build();
        Trainer trainer2 = Trainer.builder()
                .isMasterTrainer(true)
                .user(User.builder()
                        .firstName("master")
                        .lastName("trainer2")
                        .email("master.trainer2@bilgeadam.com")
                        .build())
                .build();
        masterTrainers.add(trainer1);
        masterTrainers.add(trainer2);


        when(trainerRepository.findAllMasterTrainers()).thenReturn(masterTrainers);
        List<MasterTrainerResponseDto> dto = trainerService.getMasterTrainerList();

        assertEquals(masterTrainers.size(), dto.size());
        for (int i = 0; i < dto.size(); i++) {
            assertEquals(masterTrainers.get(i).getUser().getFirstName(), dto.get(i).getFirstName());
            assertEquals(masterTrainers.get(i).getUser().getLastName(), dto.get(i).getLastName());
            assertEquals(masterTrainers.get(i).getUser().getEmail(), dto.get(i).getEmail());
        }
    }

    @Test
    void testGetAssistantTrainerList(){
        List<Trainer> assistantTrainers = new ArrayList<>();

        Trainer trainer1 = Trainer.builder()
                .isMasterTrainer(false)
                .user(User.builder()
                        .firstName("Assistant")
                        .lastName("trainer1")
                        .email("assistant.trainer1@bilgeadam.com")
                        .build())
                .build();
        Trainer trainer2 = Trainer.builder()
                .isMasterTrainer(false)
                .user(User.builder()
                        .firstName("Assistant")
                        .lastName("trainer2")
                        .email("assistant.trainer2@bilgeadam.com")
                        .build())
                .build();
        assistantTrainers.add(trainer1);
        assistantTrainers.add(trainer2);


        when(trainerRepository.findAllAssistantTrainers()).thenReturn(assistantTrainers);
        List<AssistantTrainerResponseDto> dto = trainerService.getAssistantTrainerList();

        assertEquals(assistantTrainers.size(), dto.size());
        for (int i = 0; i < dto.size(); i++) {
            assertEquals(assistantTrainers.get(i).getUser().getFirstName(), dto.get(i).getFirstName());
            assertEquals(assistantTrainers.get(i).getUser().getLastName(), dto.get(i).getLastName());
            assertEquals(assistantTrainers.get(i).getUser().getEmail(), dto.get(i).getEmail());
        }
    }

    @Test
    public void testUpdateTrainer_TrainerTagNotFoundException(){
        when(trainerRepository.findActiveById(1L)).thenReturn(Optional.empty());
        when(trainerTagService.findOptionalTrainerTagById(1L)).thenReturn(Optional.empty());

        try{
            trainerService.updateTrainer(TrainerUpdateDto.builder().trainerOid(1L).trainerTagOid(1L).build());
        } catch (TrainerTagNotFoundException e){
            assertEquals("Trainer tag is not found", e.getMessage());
        }
    }

    @Test
    public void testUpdateTrainer_TrainerNotFoundException(){
        when(trainerRepository.findActiveById(1L)).thenReturn(Optional.empty());
        when(trainerTagService.findOptionalTrainerTagById(1L)).thenReturn(Optional.of(TrainerTag.builder()
                        .state(State.ACTIVE)
                        .tagString("test")
                .build()));
        try{
            trainerService.updateTrainer(TrainerUpdateDto.builder().trainerOid(1L).trainerTagOid(1L).build());
        } catch (TrainerNotFoundException e){
            assertEquals("Trainer is not found", e.getMessage());
        }
    }
}
