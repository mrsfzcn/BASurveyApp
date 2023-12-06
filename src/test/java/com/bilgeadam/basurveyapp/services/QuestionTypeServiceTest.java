package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionTypeResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTypeFindByIdResponseDto;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTypeNotFoundException;
import com.bilgeadam.basurveyapp.repositories.QuestionTypeRepository;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionTypeServiceTest {

    @InjectMocks
    QuestionTypeService questionTypeService;
    @Mock
    QuestionTypeRepository questionTypeRepository;


    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }


     /*
   ======================================================= createQuestionType Test=======================================================
    */

    @Test
    public void createQuestion_EmptyListType(){
        CreateQuestionTypeRequestDto createQuestionTypeRequestDto = CreateQuestionTypeRequestDto.builder().questionType("NewQuestionType").build();

        Mockito.when(questionTypeRepository.findAll()).thenReturn(Collections.emptyList());

        boolean result = questionTypeService.createQuestionType(createQuestionTypeRequestDto);

        Assertions.assertTrue(result);
    }


    @Test
    public void createQuestion_NotEmptyButDeletedListType(){
        CreateQuestionTypeRequestDto createQuestionTypeRequestDto = CreateQuestionTypeRequestDto.builder().questionType("QuestionType").build();

        QuestionType questionType = QuestionType.builder().questionType("QuestionType").build();
        questionType.setState(State.DELETED);

        List<QuestionType> questionTypeList = new ArrayList<>();
        questionTypeList.add(questionType);

        Mockito.when(questionTypeRepository.findAll()).thenReturn(questionTypeList);

        boolean result = questionTypeService.createQuestionType(createQuestionTypeRequestDto);

        Assertions.assertEquals(questionType.getState(),State.ACTIVE);
        Assertions.assertTrue(result);
    }


    @Test
    public void createQuestion_NotEmptyNotDeletedListType(){
        CreateQuestionTypeRequestDto createQuestionTypeRequestDto = CreateQuestionTypeRequestDto.builder().questionType("QuestionType").build();

        QuestionType questionType = QuestionType.builder().questionType("QuestionType").build();
        questionType.setState(State.ACTIVE);

        List<QuestionType> questionTypeList = new ArrayList<>();
        questionTypeList.add(questionType);

        Mockito.when(questionTypeRepository.findAll()).thenReturn(questionTypeList);

        boolean result = questionTypeService.createQuestionType(createQuestionTypeRequestDto);

        Assertions.assertFalse(result);
    }


      /*
   ======================================================= updateQuestionType Test=======================================================
    */

    @Test
    public void updateQuestion_EmptyTest(){
        UpdateQuestionTypeRequestDto updateQuestionTypeRequestDto = UpdateQuestionTypeRequestDto.builder().questionType("QuestionType").build();
        Long id = 1L;

        QuestionType questionType = QuestionType.builder().questionType("QuestionType").build();

        Mockito.when(questionTypeRepository.findActiveById(id)).thenReturn(Optional.empty());

        QuestionTypeNotFoundException exception= Assertions.assertThrows(QuestionTypeNotFoundException.class,()->{
            questionTypeService.updateQuestionType(updateQuestionTypeRequestDto,id);
        });

        Assertions.assertEquals(exception.getMessage(),"QuestionType is not found");
    }

@Test
    public void updateQuestion_NotEmptyTest(){
        UpdateQuestionTypeRequestDto updateQuestionTypeRequestDto = UpdateQuestionTypeRequestDto.builder().questionType("QuestionType").build();
        Long id = 1L;

        QuestionType questionType = QuestionType.builder().questionType("QuestionType").build();

        Mockito.when(questionTypeRepository.findActiveById(id)).thenReturn(Optional.of(questionType));

        questionTypeService.updateQuestionType(updateQuestionTypeRequestDto,id);
        Mockito.verify(questionTypeRepository,Mockito.times(1)).save(questionType);
    }

        /*
   ======================================================= findById Test=======================================================
    */

    @Test
    public void findById_NotEmptyTest(){
        Long questionId = 1L;
        QuestionType questionType=QuestionType.builder().questionType("QuestionType").build();
        QuestionTypeFindByIdResponseDto questionTypeFindByIdResponseDto = QuestionTypeFindByIdResponseDto.builder().questionType(questionType.getQuestionType()).build();
        Mockito.when(questionTypeRepository.findActiveById(questionId)).thenReturn(Optional.of(questionType));


        QuestionTypeFindByIdResponseDto byId = questionTypeService.findById(1L);

        Assertions.assertEquals(byId.getQuestionType(),"QuestionType");
    }

    @Test
    public void findById_EmptyTest(){
        Long questionId = 1L;
        QuestionType questionType=QuestionType.builder().questionType("QuestionType").build();
        QuestionTypeFindByIdResponseDto questionTypeFindByIdResponseDto = QuestionTypeFindByIdResponseDto.builder().questionType(questionType.getQuestionType()).build();
        Mockito.when(questionTypeRepository.findActiveById(questionId)).thenReturn(Optional.empty());

        QuestionTypeNotFoundException exception=Assertions.assertThrows(QuestionTypeNotFoundException.class,()->{
            questionTypeService.findById(questionId);
        });

        Assertions.assertEquals(exception.getMessage(),"QuestionType is not found");
    }

         /*
   ======================================================= findAll Test=======================================================
    */

    @Test
    public void findAllTest() {
        QuestionType questionType = QuestionType.builder().questionType("QuestionType").build();
        List<QuestionType> questionTypeList = new ArrayList<>();
        questionTypeList.add(questionType);

        Mockito.when(questionTypeRepository.findAllActive()).thenReturn(questionTypeList);

        List<AllQuestionTypeResponseDto> all = questionTypeService.findAll();

        Assertions.assertEquals(all.get(0).getQuestionType(),questionType.getQuestionType());
    }

             /*
   ======================================================= delete Test=======================================================
    */

    @Test
    public void deleteNotEmptyTest() {
        Long questionTypeId = 1L;

        QuestionType questionType=QuestionType.builder().build();

        Mockito.when(questionTypeRepository
                .findByOidAndState(questionTypeId, State.ACTIVE)).thenReturn(Optional.of(questionType));

        Boolean delete = questionTypeService.delete(questionTypeId);

        Assertions.assertEquals(questionType.getState(),State.DELETED);
        Mockito.verify(questionTypeRepository, Mockito.times(1)).save(questionType);
        Assertions.assertTrue(delete);
    }


    @Test
    public void deleteEmptyTest() {
        Long questionTypeId = 1L;

        Mockito.when(questionTypeRepository
                .findByOidAndState(questionTypeId, State.ACTIVE)).thenReturn(Optional.empty());

        QuestionTypeNotFoundException exception = Assertions.assertThrows(QuestionTypeNotFoundException.class, () -> {
            questionTypeService.delete(questionTypeId);
        });

        Assertions.assertEquals(exception.getMessage(),"QuestionType is not found");


    }



             /*
   ======================================================= findActiveByID Test=======================================================
    */

    @Test
    public void findActiveByIdTest(){
        Long questionTypeOid=1L;
        QuestionType questionType=QuestionType.builder().questionType("QuestionType").build();

        Mockito.when(questionTypeRepository.findActiveById(questionTypeOid)).thenReturn(Optional.of(questionType));
        QuestionType questionType1 = questionTypeRepository.findActiveById(questionTypeOid).get();

        Assertions.assertEquals(questionType1.getQuestionType(), questionType.getQuestionType());
    }

    /*
   ======================================================= updateQuestionTypeByTypeString Test=======================================================
    */


    @Test
    public void updateQuestionTypeByTypeStringEmptyTest() {
        String newTypeString = "newTypeString";
        String typeString = "typeString";

        Mockito.when(questionTypeRepository.findOptionalByQuestionType(typeString)).thenReturn(Optional.empty());

        QuestionTagNotFoundException exception= Assertions.assertThrows(QuestionTagNotFoundException.class,()->{
            questionTypeService.updateQuestionTypeByTypeString(newTypeString, typeString);
        });

        Assertions.assertEquals(exception.getMessage(),"Question Tag not found");
    }

    @Test
    public void updateQuestionTypeByTypeStringNotEmptyTest(){
        String newTypeString = "newTypeString";
        String typeString = "typeString";

        QuestionType questionType=QuestionType.builder().questionType(typeString).build();

        Mockito.when(questionTypeRepository.findOptionalByQuestionType(typeString)).thenReturn(Optional.of(questionType));

        Mockito.when(questionTypeService.updateQuestionTypeByTypeString(newTypeString, typeString)).thenReturn(questionType);

        QuestionType questionTypeReturn = questionTypeService.updateQuestionTypeByTypeString(newTypeString, typeString);

        Assertions.assertEquals(questionType.getQuestionType(),newTypeString);

        Mockito.verify(questionTypeRepository, Mockito.times(1)).save(questionType);

        Assertions.assertEquals(questionTypeReturn.getQuestionType(),newTypeString);
    }

  /*
   ======================================================= existsByQuestionType Test =======================================================
    */

    @Test
    public void existsByQuestionType() {
        String questionType = "QuestionType";
        Mockito.when(questionTypeRepository.existsByQuestionType(questionType)).thenReturn(Boolean.TRUE);

        questionTypeService.existsByQuestionType(questionType);

        Mockito.verify(questionTypeRepository, Mockito.times(1)).existsByQuestionType(questionType);

       Assertions.assertTrue(questionTypeService.existsByQuestionType(questionType));

    }

}
