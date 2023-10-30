package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.StudentUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.repositories.StudentRepository;
import com.bilgeadam.basurveyapp.services.StudentService;
import com.bilgeadam.basurveyapp.services.StudentTagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentTagService studentTagService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStudent() {
        Student student = new Student();
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        studentService.createStudent(student);

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudent() {
        Long studentOid = 1L;
        Long studentTagOid = 2L;

        StudentUpdateDto dto = new StudentUpdateDto();
        dto.setStudentOid(studentOid);
        dto.setStudentTagOid(studentTagOid);

        Student student = new Student();
        student.setOid(studentOid);

        StudentTag studentTag = new StudentTag();
        studentTag.setOid(studentTagOid);

        when(studentRepository.findActiveById(studentOid)).thenReturn(Optional.of(student));
        when(studentTagService.findActiveById(studentTagOid)).thenReturn(Optional.of(studentTag));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponseDto responseDto = studentService.updateStudent(dto);

        verify(studentRepository, times(1)).findActiveById(studentOid);
        verify(studentTagService, times(1)).findActiveById(studentTagOid);
        verify(studentRepository, times(1)).save(any(Student.class));

        assertNotNull(responseDto);
    }

    @Test
    void testFindByUser() {
        User user = new User();
        user.setOid(1L);

        when(studentRepository.findByUser(user.getOid())).thenReturn(Optional.of(new Student()));

        Optional<Student> result = studentService.findByUser(user);

        assertTrue(result.isPresent());
    }

    @Test
    void testFindByStudentTagOid() {
        Long studentTagOid = 1L;
        List<Student> students = new ArrayList<>();
        when(studentRepository.findByStudentTagOid(studentTagOid)).thenReturn(students);

        List<Student> result = studentService.findByStudentTagOid(studentTagOid);

        assertNotNull(result);
    }

    @Test
    void testGetStudentList() {
        List<Student> students = new ArrayList<>();
        when(studentRepository.findAllStudents()).thenReturn(students);

        List<StudentResponseDto> result = studentService.getStudentList();

        assertNotNull(result);
    }

    @Test
    void testSave() {
        Student student = new Student();
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        studentService.save(student);

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testFindByOid() {
        Long studentOid = 1L;
        when(studentRepository.findById(studentOid)).thenReturn(Optional.of(new Student()));

        Optional<Student> result = studentService.findByOid(studentOid);

        assertTrue(result.isPresent());
    }

    @Test
    void testDeleteByStudentOid() {
        Long oid = 1L;
        Student student = new Student();
        student.setOid(oid);
        User userOfStudent = new User();
        userOfStudent.setState(State.ACTIVE);
        student.setUser(userOfStudent);

        when(studentRepository.findStudentByOid(oid)).thenReturn(Optional.of(student));
        when(studentRepository.findActiveById(oid)).thenReturn(Optional.of(student));
        when(studentRepository.softDeleteById(oid)).thenReturn(true);

        boolean result = studentService.deleteByStudentOid(oid);

        assertTrue(result);
    }

    @Test
    void testFindUserByStudentOid() {
        Long oid = 1L;
        Student student = new Student();
        student.setOid(oid);
        User user = new User();
        student.setUser(user);

        when(studentRepository.findStudentByOid(oid)).thenReturn(Optional.of(student));

        User result = studentService.findUserByStudentOid(oid);

        assertNotNull(result);
    }

}
