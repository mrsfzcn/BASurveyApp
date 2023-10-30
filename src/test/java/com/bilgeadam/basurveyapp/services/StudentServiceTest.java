package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentServiceTest {

    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentTagService studentTagService;


    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentService(studentRepository,studentTagService);
    }


    @Test
    public void testFindUserByStudentOid() {

        Student student = new Student();
        User user = new User();
        student.setUser(user);

        when(studentRepository.findStudentByOid(1L)).thenReturn(Optional.of(student));
        User result = studentService.findUserByStudentOid(1L);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void testFindUserByStudentOidWhenStudentNotPresent() {
        when(studentRepository.findStudentByOid(1L)).thenReturn(Optional.empty());

        try {
            studentRepository.findStudentByOid(1L);
        }catch (ResourceNotFoundException e){
            assertEquals("student id bulunamadi",e.getMessage());
        }
    }
}
