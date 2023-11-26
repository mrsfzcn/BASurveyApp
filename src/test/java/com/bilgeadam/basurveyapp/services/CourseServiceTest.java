package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Course;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.CourseNotFoundException;
import com.bilgeadam.basurveyapp.repositories.ICourseRepository;
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

public class CourseServiceTest {
    @InjectMocks
    private CourseService courseService;
    @Mock
    private ICourseRepository courseRepository;

    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
    }

    //oid
    @Test
    public void testDeleteCourseByOid_Success() {
        Long oid = 1L;
        when(courseRepository.findById(oid)).thenReturn(Optional.of(new Course()));
        when(courseRepository.softDeleteById(oid)).thenReturn(true);

        Boolean result = courseService.deleteCourseByOid(oid);

        assertTrue(result);
    }

    @Test
    public void testDeleteCourseByOid_Failure() {
        Long oid = 1L;
        when(courseRepository.findById(oid)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> {
            courseService.deleteCourseByOid(oid);
        });
    }

    @Test
    public void testDeleteCourseByOid_Failure2() {
        Long oid = 1L;
        when(courseRepository.findById(oid)).thenReturn(Optional.of(new Course()));
        when(courseRepository.softDeleteById(oid)).thenReturn(false);

        assertThrows(CourseNotFoundException.class, () -> {
            courseService.deleteCourseByOid(oid);
        });
    }
    //findallactive

    @Test
    public void testFindAllActiveCourses_Success() {
        when(courseRepository.findAllActive()).thenReturn(List.of(new Course()));
        List<Course> result = courseService.findAllActiveCourses();
        assertNotEquals(new ArrayList<>(), result);
    }

    @Test
    public void testFindAllActiveCourses_Failure() {
        when(courseRepository.findAllActive()).thenReturn(new ArrayList<>());
        List<Course> result = courseService.findAllActiveCourses();
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testExistByApiId(){
        when(courseRepository.existsByApiId(eq("apiId"))).thenReturn(true);
        Boolean resultTrue=courseService.existByApiId("apiId");
        assertTrue(resultTrue);
        when(courseRepository.existsByApiId(eq("apiId"))).thenReturn(false);
        Boolean resultFalse=courseService.existByApiId("apiId");
        assertFalse(resultFalse);
    }
    //findbyname

    @Test
    public void testfindCoursesByName_Success(){
        when(courseRepository.findByName(eq("name"))).thenReturn(Optional.of(new Course()));
        Optional<Course> course=courseRepository.findByName("name");
        assertEquals(course,Optional.of(new Course()));
    }

    @Test
    public void testfindCoursesByName_Failure(){
        when(courseRepository.findByName(eq("name"))).thenReturn(Optional.empty());
        try{
            Optional<Course> course=courseRepository.findByName("name");
        }
        catch (Exception e){
            assertEquals(e.getClass(),CourseNotFoundException.class);
        }
    }

    //findalldelete
    @Test
    public void testfindAllDeletedCourses_Success(){
        when(courseRepository.findAllByState(eq(State.DELETED))).thenReturn(List.of(new Course()));
        List<Course> courseList=courseService.findAllDeletedCourses();
        assertNotEquals(new ArrayList<>(),courseList);
    }

    @Test
    public void testfindAllDeletedCourses_Failure(){
        when(courseRepository.findAllByState(eq(State.DELETED))).thenReturn(new ArrayList<>());
        List<Course> courseList=courseService.findAllDeletedCourses();
        assertEquals(new ArrayList<>(),courseList);
    }






































}
