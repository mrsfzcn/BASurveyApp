package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchAlreadyExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchNotFoundException;
import com.bilgeadam.basurveyapp.repositories.IBranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class BranchServiceTest {
    @InjectMocks
    private BranchService branchService;
    @Mock
    private IBranchRepository branchRepository;

    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
    }


    /*
   ======================================================= DeleteBranchByOid Test=======================================================
    */
    @Test
    public void testDeleteBranchByOid_Success() {
        Long oid = 1L;

        when(branchRepository.findById(oid)).thenReturn(Optional.of(new Branch()));
        when(branchRepository.softDeleteById(oid)).thenReturn(true);

        Boolean result = branchService.deleteBranchByOid(oid);

        assertTrue(result);
    }

    @Test
    public void testDeleteBranchByOid_BranchNotFound() {

        when(branchRepository.findById(1L)).thenThrow(new BranchNotFoundException("Boyle bir sube bulunamamistir"));


        try {
            branchService.deleteBranchByOid(1L);
        } catch (BranchNotFoundException e) {
            System.out.println("yaz bakalim");
            assertEquals("Boyle bir sube bulunamamistir", e.getMessage());
        }

    }

    @Test
    public void testDeleteBranchByOid_FailureToDelete() {
        Long oid = 1L;
        when(branchRepository.findById(oid)).thenReturn(Optional.of(new Branch()));
        doThrow(new RuntimeException("Silme isleminde hata olustu")).when(branchRepository.softDeleteById(oid));
        Boolean result = branchService.deleteBranchByOid(oid);
        assertFalse(result);
    }


    /*
    ======================================================= findAllActive Test =======================================================
     */
    @Test
    public void testfindAllActiveBranches_Success() {
        when(branchRepository.findAllActive()).thenReturn(List.of(new Branch()));
        List<Branch> branchList = branchRepository.findAllActive();
        assertNotEquals(new ArrayList<>(), branchList);
    }

    @Test
    public void testfindAllActiveBranches_Failure() {
        when(branchRepository.findAllActive()).thenReturn(new ArrayList<>());
        List<Branch> branchList = branchRepository.findAllActive();
        assertEquals(branchList, new ArrayList<>());
    }

    /*
    ======================================================= existByApiId Test =======================================================
     */

    @Test
    public void testExistByApiId() {
        when(branchRepository.existsByApiId("apiId")).thenReturn(true);
        Boolean aBooleanTrue  = branchRepository.existsByApiId("apiId");
        assertTrue(aBooleanTrue);

        when(branchRepository.existsByApiId("apiId")).thenReturn(false);
        Boolean aBooleanFalse  = branchRepository.existsByApiId("apiId");
        assertFalse(aBooleanFalse);
    }

    /*
    ======================================================= findBranchesByName Test =======================================================
     */

    @Test
    public void testfindBranchesByName_Success() {
        when(branchRepository.findByNameAndState("name", State.ACTIVE)).thenReturn(List.of(new Branch()));

        List<Branch> branchList = branchRepository.findByNameAndState("name", State.ACTIVE);
        assertNotEquals(new ArrayList<>(),branchList);
    }

    @Test
    public void testfindBranchesByName_Failure() {

        when(branchRepository.findByNameAndState("name", State.ACTIVE)).thenThrow(new BranchNotFoundException("Herhangi bir branch bulunamadi"));

        try {
             branchRepository.findByNameAndState("name", State.ACTIVE);
        } catch (BranchNotFoundException e) {
            assertEquals("Herhangi bir branch bulunamadi",e.getMessage());
        }

    }

 /*
    ======================================================= findByNameAndCity Test =======================================================
     */

    @Test
    public void testfindByNameAndCity_Failure() {
        when(branchRepository.findByNameAndCityAndState("name", "city", State.ACTIVE)).thenThrow(new BranchNotFoundException("Herhangi bir branch bulunamadi"));

        try {
            branchRepository.findByNameAndCityAndState("name", "city", State.ACTIVE);
        } catch (BranchNotFoundException e) {
            assertEquals("Herhangi bir branch bulunamadi",e.getMessage());
        }
    }

    @Test
    public void testfindByNameAndCity_Success() {
            when(branchRepository.findByNameAndCityAndState("name", "city", State.ACTIVE)).thenReturn(Optional.of(new Branch()));

            Optional<Branch> branch = branchRepository.findByNameAndCityAndState("name", "city", State.ACTIVE);

            assertNotEquals(Optional.empty(), branch.get());
    }

    /*
    ======================================================= findByCity Test =======================================================
     */

    @Test
    public void testfindByCity_Failure() {
        when(branchRepository.findByCityAndState("city", State.ACTIVE)).thenThrow(new BranchNotFoundException("Herhangi bir branch bulunamadi"));

        try {
            branchRepository.findByCityAndState("city", State.ACTIVE);
        } catch (BranchNotFoundException e) {
            assertEquals("Herhangi bir branch bulunamadi",e.getMessage());
        }
    }

    @Test
    public void testfindByCity_Success() {
        when(branchRepository.findByCityAndState("city", State.ACTIVE)).thenReturn(List.of(new Branch()));

        List<Branch> branches = branchRepository.findByCityAndState("city", State.ACTIVE);

        assertNotEquals(new ArrayList<>(), branches);
    }

     /*
    ======================================================= findByApiId Test =======================================================
     */

    @Test
    public void testfindByApiId_Failure() {
        when(branchRepository.findByApiIdAndState("apiId", State.ACTIVE)).thenThrow(new BranchNotFoundException("Herhangi bir branch bulunamadi"));

        try {
            branchRepository.findByApiIdAndState("apiId", State.ACTIVE);
        } catch (BranchNotFoundException e) {
            assertEquals("Herhangi bir branch bulunamadi",e.getMessage());
        }
    }

    @Test
    public void testfindByApiId_Success() {
        when(branchRepository.findByApiIdAndState("apiId", State.ACTIVE)).thenReturn(Optional.of(new Branch()));

        Optional<Branch> branchOptional= branchRepository.findByApiIdAndState("apiId", State.ACTIVE);

        assertNotEquals(Optional.empty(), branchOptional);
    }

      /*
    ======================================================= findByApiId Test =======================================================
     */

    @Test
    public void testupdateBranchByApiId_BranchNotFound() {
        when(branchRepository.findByApiIdAndState("apiId", State.ACTIVE)).thenThrow(new BranchNotFoundException("Aradiginiz branch bulunamadi"));

        try {
            branchRepository.findByApiIdAndState("apiId", State.ACTIVE);
        } catch (BranchNotFoundException e) {
            assertEquals("Aradiginiz branch bulunamadi",e.getMessage());
        }
    }

    @Test
    public void testupdateBranchByApiId_BranchAlreadyExist() {
//        when(branchRepository.findByApiIdAndState("apiId", State.ACTIVE)).thenThrow(new BranchAlreadyExistException("Bu branch sistemde zaten mevcut"));

        when(branchRepository.existsByNameAndCity("name", "city")).thenThrow(new BranchAlreadyExistException("Bu branch sistemde zaten mevcut"));

        try {
            branchRepository.existsByNameAndCity("name", "city");
        } catch (BranchAlreadyExistException e) {
            assertEquals(e.getMessage(),"Bu branch sistemde zaten mevcut");
        }
    }



  /*
    ======================================================= findAllDeletedBranches Test =======================================================
     */

    @Test
    public void testfindAllDeletedBranches_BranchNotFoundException() {

        when(branchRepository.findAllByState(State.DELETED)).thenThrow(new BranchNotFoundException("Herhangi Silinmis bir branch bulunamadi"));

        try {
            branchRepository.findAllByState(State.DELETED);
        } catch (BranchNotFoundException e) {
            assertEquals(e.getMessage(),"Herhangi Silinmis bir branch bulunamadi");
        }
    }

    @Test
    public void testfindAllDeletedBranches_Success() {

        when(branchRepository.findAllByState(State.DELETED)).thenReturn(List.of(new Branch()));

        List<Branch> branchList= branchRepository.findAllByState(State.DELETED);

        assertNotEquals(new ArrayList<>(),branchList);
    }

     /*
    ======================================================= activateBranch Test =======================================================
     */


    @Test
    public void testactivateBranch_BranchNotFound() {

        when(branchRepository.findByOid(1L)).thenThrow(new BranchNotFoundException("Boyle bir branch bulunamadi"));

        try {
            branchRepository.findByOid(1L);
        } catch (BranchNotFoundException e) {
            assertEquals(e.getMessage(),"Boyle bir branch bulunamadi");
        }
    }

    @Test
    public void testactivateBranch_BranchAlreadyExist() {

        when(branchRepository.findByOid(1L)).thenReturn(Optional.of(new Branch()));

        Optional<Branch> branchOptional = branchRepository.findByOid(1L);
        branchOptional.get().setState(State.ACTIVE);

        when(branchOptional.get().getState().equals(State.ACTIVE)).thenThrow(new BranchAlreadyExistException("Branch zaten aktif"));

        try {
            branchOptional.get().getState().equals(State.ACTIVE);
        } catch (BranchAlreadyExistException e) {
            assertEquals(e.getMessage(),"Branch zaten aktif");
        }
    }

    @Test
    public void testActivateBranch_Success() {
        Long oid = 1L;
        Branch existingBranch = new Branch();
        existingBranch.setState(State.DELETED);

        when(branchRepository.findByOid(oid)).thenReturn(Optional.of(existingBranch));
        when(branchRepository.save(existingBranch)).thenReturn(existingBranch);

        MessageResponseDto response = branchService.activateBranch(oid);

        assertNotNull(response);
        assertEquals(existingBranch.getName() + " isimli " + existingBranch.getCity() + " sehrindeki sube aktif edildi", response.getSuccessMessage());
    }

}
