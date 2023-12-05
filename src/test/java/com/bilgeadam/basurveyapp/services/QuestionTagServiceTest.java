package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.TagResponseDto;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagNotFoundException;
import com.bilgeadam.basurveyapp.repositories.QuestionTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionTagServiceTest {

    @InjectMocks
    private QuestionTagService questionTagService;

    @Mock
    private QuestionTagRepository questionTagRepository;

    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
    }

     /*
   ======================================================= CreateTag Test=======================================================
    */

    @Test
    public void testCreateTagOptionalEmpty() {

        CreateTagDto createTagDto = new CreateTagDto();
        createTagDto.setTagString("TestTag");
        createTagDto.setMainTagOid(1L);


        Mockito.when(questionTagRepository.findOptionalByTagString("TestTag")).thenReturn(Optional.empty());

        assertNull(questionTagRepository.findOptionalByTagString("TestTag").orElse(null));
        assertNotEquals("TestTag", questionTagRepository.findOptionalByTagString("TestTag").orElse(null));
        assertNotEquals(1L, questionTagRepository.findOptionalByTagString("TestTag").orElse(null));
        assertNotEquals(State.ACTIVE, questionTagRepository.findOptionalByTagString("TestTag").orElse(null));

    }

    @Test
    public void testCreateTagNotEmptyStateActive() {

        CreateTagDto createTagDto = new CreateTagDto();
        createTagDto.setTagString("TestTag");
        createTagDto.setMainTagOid(1L);
        QuestionTag resultTag = QuestionTag.builder()
                .tagString(createTagDto.getTagString())
                .mainTagOid(createTagDto.getMainTagOid())
                .state(State.ACTIVE)
                .build();

        Mockito.when(questionTagRepository.findOptionalByTagString("TestTag")).thenReturn(Optional.of(resultTag));
        assertEquals(resultTag, questionTagRepository.findOptionalByTagString("TestTag").get());


        try {
            questionTagService.createTag(createTagDto);
        } catch (QuestionTagExistException e) {
            assertEquals(e.getMessage(), "Question Tag already exists!");
        }
    }

    @Test
    public void testCreateTagNotEmptyStateDeleted() {

        CreateTagDto createTagDto = new CreateTagDto();
        createTagDto.setTagString("TestTag");
        createTagDto.setMainTagOid(1L);
        QuestionTag resultTag = QuestionTag.builder()
                .tagString(createTagDto.getTagString())
                .mainTagOid(createTagDto.getMainTagOid())
                .state(State.DELETED)
                .build();

        Mockito.when(questionTagRepository.findOptionalByTagString("TestTag")).thenReturn(Optional.of(resultTag));
        Mockito.when(questionTagService.createTag(createTagDto)).thenReturn(resultTag);

        assertEquals(resultTag.getState(), State.ACTIVE);
        assertEquals(resultTag.getTagString(), "TestTag");
        assertNotNull(resultTag);
    }

     /*
   ======================================================= findAll Test=======================================================
    */

    @Test
    public void findAllTest(){

        List<QuestionTag> questionResponseList=new ArrayList<>();
        List<TagResponseDto> tagResponseDtos=new ArrayList<>();

        Mockito.when(questionTagRepository.findAllActive()).thenReturn(questionResponseList);

        Mockito.when(questionTagService.findAll()).thenReturn(tagResponseDtos);
    }

     /*
   ======================================================= Delete Test=======================================================
    */

    @Test
    public void deleteWhenTagFound(){
        Long tagStringId=1L;
        QuestionTag questionTag = QuestionTag.builder().oid(tagStringId).build();

        Mockito.when(questionTagRepository.findActiveById(tagStringId)).thenReturn(Optional.of(questionTag));

        Mockito.when(questionTagRepository.softDeleteById(tagStringId)).thenReturn(true);

        assertTrue(questionTagService.delete(1L));
    }

    @Test
    public void testDeleteWhenTagNotFound() {
        Long tagStringId = 1L;


        Mockito.when(questionTagRepository.findActiveById(tagStringId)).thenReturn(Optional.empty());

        QuestionTagNotFoundException exception = assertThrows(QuestionTagNotFoundException.class, () -> {
            questionTagService.delete(tagStringId);
        });

        assertEquals("Question tag not found", exception.getMessage());
    }


     /*
   ======================================================= findActiveById Test=======================================================
    */

    @Test
    public void findActiveByIdTest(){
        Long questionTagOid=1L;

        QuestionTag questionTag=new QuestionTag();

        Mockito.when(questionTagRepository.findActiveById(questionTagOid)).thenReturn(Optional.of(questionTag));

        Optional<QuestionTag> resultForRepository = questionTagRepository.findActiveById(questionTagOid);

        assertEquals(Optional.of(questionTag), resultForRepository);

        Mockito.when(questionTagService.findActiveById(questionTagOid)).thenReturn(Optional.of(questionTag));


        Optional<QuestionTag> resultForService = questionTagService.findActiveById(questionTagOid);

        assertEquals(Optional.of(questionTag), resultForService);
    }


    /*
  ======================================================= Save Test=======================================================
   */
    @Test
    public void saveTest(){
        QuestionTag questionTag = QuestionTag.builder().tagString("tagString").mainTagOid(1L).build();

        questionTagService.save(questionTag);


        Mockito.verify(questionTagRepository, Mockito.times(1)).save(questionTag);
    }


    /*
  ======================================================= findById Test=======================================================
   */
    @Test
    public void findByIdTest(){
        Long questionTagOid=1L;

        QuestionTag questionTag=QuestionTag.builder().oid(questionTagOid).build();

        Mockito.when(questionTagRepository.findById(questionTagOid)).thenReturn(Optional.of(questionTag));

        Optional<QuestionTag> result = questionTagService.findById(questionTagOid);

        assertEquals(Optional.of(questionTag),result);


        Mockito.verify(questionTagRepository, Mockito.times(1)).findById(questionTagOid);
    }


     /*
   ======================================================= Update Test=======================================================
    */

    @Test
    public void updateTagByTagStringTest() {
        String tagString = "tagString";
        String newTagString = "newTagString";

        QuestionTag questionTag = QuestionTag.builder().tagString(tagString).build();

        Mockito.when(questionTagRepository.findOptionalByTagString(tagString)).thenReturn(Optional.of(questionTag));

        Mockito.when(questionTagService.updateTagByTagString(tagString, newTagString)).thenReturn(questionTag);

        QuestionTag questionTag1 = questionTagService.updateTagByTagString(tagString, newTagString);

        assertEquals(questionTag1.getTagString(),newTagString);
    }

    @Test
    public void updateTagByTagStringEmptyTest() {
        String tagString = "tagString";
        String newTagString = "newTagString";


        Mockito.when(questionTagRepository.findOptionalByTagString(tagString)).thenReturn(Optional.empty());


        QuestionTagNotFoundException exception = assertThrows(QuestionTagNotFoundException.class,()->{
            questionTagService.updateTagByTagString(tagString,newTagString);
        });

        assertEquals(exception.getMessage(),"Question Tag not found");
    }

    /*
      ======================================================= DeleteByTagString Test=======================================================
       */
    @Test
    public void deleteByTagStringTest() {
        String tagString = "tagString";

        QuestionTag questionTag = QuestionTag.builder().tagString(tagString).build();

        Mockito.when(questionTagRepository.findOptionalByTagString(tagString)).thenReturn(Optional.of(questionTag));

        Mockito.when(questionTagService.deleteByTagString(tagString)).thenReturn(true);

        assertEquals(true,questionTagService.deleteByTagString(tagString));
    }

    @Test
    public void deleteByTagStringEmptyTest() {
        String tagString = "tagString";


        Mockito.when(questionTagRepository.findOptionalByTagString(tagString)).thenReturn(Optional.empty());



        QuestionTagNotFoundException questionTagNotFoundException= assertThrows(QuestionTagNotFoundException.class,()->{
            questionTagService.deleteByTagString(tagString);
        });

        assertEquals(questionTagNotFoundException.getMessage(),"Question tag not found");

    }

    /*
      ======================================================= activeByTagString Test=======================================================
       */
    @Test
    public void activeByTagStringTest(){
        String tagString = "tagString";

        QuestionTag questionTag = QuestionTag.builder().tagString(tagString).build();

        Mockito.when(questionTagRepository.findOptionalByTagString(tagString)).thenReturn(Optional.of(questionTag));

        Mockito.when(questionTagService.activeByTagString(tagString)).thenReturn(true);

        assertEquals(true,questionTagService.activeByTagString(tagString));

    }

    @Test
    public void activeByTagStringEmptyTest(){
        String tagString = "tagString";

        Mockito.when(questionTagRepository.findOptionalByTagString(tagString)).thenReturn(Optional.empty());

        QuestionTagNotFoundException exception= assertThrows(QuestionTagNotFoundException.class,()->{
            questionTagService.activeByTagString(tagString);
        });
        assertEquals(exception.getMessage(),"Question tag not found");
    }
}