package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateTagDto;
import com.bilgeadam.basurveyapp.dto.response.StudentTagDetailResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.exceptions.custom.StudentTagExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.StudentTagNotFoundException;
import com.bilgeadam.basurveyapp.repositories.StudentTagRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Mert Cömertoğlu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentTagServiceTest {

    @Mock
    private StudentTagRepository studentTagRepository;

    private StudentTagService studentTagService;

    @BeforeAll
    public void Init(){
        MockitoAnnotations.openMocks(this);
        studentTagService= new StudentTagService(studentTagRepository);
    }

    @Test
    public void testCreateTag_StudentTagExistException(){

        CreateTagDto createTagDto = CreateTagDto.builder()
                .tagString("test")
                .build();

        when(studentTagRepository.findByTagName(createTagDto.getTagString())).thenReturn(Optional.of(StudentTag.builder().tagString("test").build()));
        try{
            studentTagService.createTag(createTagDto);
            fail("Expected StudentTagExistException was not thrown");
        }catch (StudentTagExistException e){
            assertEquals("Student Tag already exist!", e.getMessage());
        }
    }

    @Test
    public void testCreateTag(){
        CreateTagDto createTagDto = CreateTagDto.builder()
                .tagString("test")
                .build();
        StudentTag studentTag = StudentTag.builder()
                .tagString(createTagDto.getTagString())
                .build();

        when(studentTagRepository.findByTagName(createTagDto.getTagString())).thenReturn(Optional.empty());
        when(studentTagRepository.save(studentTag)).thenReturn(studentTag);

        studentTagService.createTag(createTagDto);

        verify(studentTagRepository).save(studentTag);
    }

    @Test
    public void testGetStudentsByStudentTag(){
        StudentTag studentTag = StudentTag.builder()
                .tagString("test")
                .build();
        Set<StudentTag> studentTags = new HashSet<>();
        studentTags.add(studentTag);
        Student student1= Student.builder()
                .studentTags(studentTags)
                .user(User.builder()
                        .firstName("ad")
                        .lastName("soyad")
                        .email("student1@bilgeadam.com")
                        .build())
                .build();
        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);

        when(studentTagRepository.findByStudentTagOid(studentTag.getOid())).thenReturn(studentList);

        List<Student> result = studentTagService.getStudentsByStudentTag(studentTag);
        assertEquals(studentTags,result.get(0).getStudentTags());
        assertEquals("student1@bilgeadam.com",result.get(0).getUser().getEmail());
    }

    @Test
    public void testFindByStudentTag(){
        StudentTag studentTag = StudentTag.builder()
                .tagString("test")
                .build();

        when(studentTagRepository.findByTagName(studentTag.getTagString())).thenReturn(Optional.of(studentTag));

        Optional<StudentTag> result= studentTagService.findByStudentTag(studentTag);

        assertTrue(result.isPresent());
        assertEquals(studentTag.getTagString(),result.get().getTagString());
    }

    @Test
    public void testFindByStudentTagName(){
        String tagString = "test";

        when(studentTagRepository.findByTagName(tagString)).thenReturn(Optional.of(StudentTag.builder()
                .tagString(tagString)
                .build()));

        Optional<StudentTag> result= studentTagService.findByStudentTagName(tagString);

        assertEquals(tagString,result.get().getTagString());
    }

    @Test
    public void testFindByStudentTagOid(){
        Long oid = 1L;
        StudentTag studentTag = StudentTag.builder()
                .tagString("test")
                .build();
        Set<StudentTag> studentTags = new HashSet<>();
        studentTags.add(studentTag);
        Student student1= Student.builder()
                .studentTags(studentTags)
                .user(User.builder()
                        .firstName("ad")
                        .lastName("soyad")
                        .email("student1@bilgeadam.com")
                        .build())
                .build();
        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);

        when(studentTagRepository.findByStudentTagOid(oid)).thenReturn(studentList);

        List<Student> result = studentTagService.findByStudentTagOid(oid);

        assertEquals(studentList,result);
        assertEquals(1,result.size());
    }

    @Test
    public void testFindActiveById(){
        Long oid = 1L;
        StudentTag studentTag = StudentTag.builder()
                .tagString("test")
                .build();

        when(studentTagRepository.findActiveById(oid)).thenReturn(Optional.of(studentTag));

        Optional<StudentTag> result = studentTagService.findActiveById(oid);

        assertTrue(result.isPresent());
        assertEquals(studentTag,result.get());
    }

    @Test
    public void testDelete(){
        Long oid = 1L;
        StudentTag studentTag = StudentTag.builder()
                .oid(oid)
                .tagString("test")
                .build();

        when(studentTagRepository.findActiveById(oid)).thenReturn(Optional.of(studentTag));

        studentTagService.delete(oid);

        verify(studentTagRepository).softDeleteById(studentTag.getOid());
    }

    @Test
    public void testDelete_StudentTagNotFoundException(){
        Long oid = 1L;
        when(studentTagRepository.findActiveById(oid)).thenReturn(Optional.empty());

        try{
            studentTagService.delete(oid);
            fail("Expected StudentTagNotFoundException was not thrown");
        }catch (StudentTagNotFoundException e){
            assertEquals("Student Tag is not found", e.getMessage());
        }
    }

    @Test
    public void testGetStudentTagList(){
        StudentTag studentTag1 = StudentTag.builder()
                .oid(1L)
                .tagString("test")
                .build();
        StudentTag studentTag2 = StudentTag.builder()
                .oid(2L)
                .tagString("test2")
                .build();
        List<StudentTag> studentTags = new ArrayList<>();
        studentTags.add(studentTag1);
        studentTags.add(studentTag2);

        when(studentTagRepository.findAllActive()).thenReturn(studentTags);

        List<StudentTagDetailResponseDto> response = studentTagService.getStudentTagList();

        assertEquals(studentTag1.getOid(),response.get(0).getOid());
        assertEquals(studentTag1.getTagString(),response.get(0).getTagString());
        assertEquals(studentTag2.getTagString(),response.get(1).getTagString());
    }

}
