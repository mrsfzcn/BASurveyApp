package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.CourseGroup;
import com.bilgeadam.basurveyapp.exceptions.custom.CourseGroupNotFoundException;
import com.bilgeadam.basurveyapp.repositories.ICourseGroupRepository;
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
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CourseGroupServiceTest {
    @InjectMocks
    private CourseGroupService courseGroupService;

    @Mock
    private ICourseGroupRepository courseGroupRepository;

    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testfindAllCourseGroup() {
        when(courseGroupRepository.findAll()).thenReturn(List.of(new CourseGroup()));
        List<CourseGroup> courseGroupFindAll = courseGroupRepository.findAll();
        assertNotEquals(new ArrayList<>(), courseGroupFindAll);
    }

    @Test
    void testdeleteCourseGroupByOid_Success() {
        when(courseGroupRepository.findById(1L)).thenReturn(Optional.of(new CourseGroup()));
        when(courseGroupRepository.softDeleteById(1L)).thenReturn(true);
        Boolean result = courseGroupService.deleteCourseGroupByOid(1L);
        assertTrue(result);
    }

    @Test
    void testdeleteCourseGroupByOid_Failure() {
        when(courseGroupRepository.findById(1L)).thenThrow(new CourseGroupNotFoundException("Sınıf yok"));
        try {
            courseGroupService.deleteCourseGroupByOid(1L);
        } catch (CourseGroupNotFoundException e) {
            assertEquals("Sınıf yok", e.getMessage());
        }
    }

    @Test
    void testdeleteCourseGroupByOid_FailToDelete() {
        Long id = 1L;
        when(courseGroupRepository.findById(id)).thenReturn(Optional.of(new CourseGroup()));
        doThrow(CourseGroupNotFoundException.class).when(courseGroupRepository).softDeleteById(id);
        assertFalse(courseGroupService.deleteCourseGroupByOid(id));
    }


    @Test
    void testExistByApiId() {
        when(courseGroupRepository.existsByApiId("apiId")).thenReturn(true);
        boolean apiIdTrue = courseGroupRepository.existsByApiId("apiId");
        assertTrue(apiIdTrue);

        when(courseGroupRepository.existsByApiId("apiId")).thenReturn(false);
        boolean apiIdFalse = courseGroupRepository.existsByApiId("apiId");
        assertFalse(apiIdFalse);

    }

    @Test
    void testfindByName_Success() {
        when(courseGroupRepository.findByName("sfg")).thenReturn(List.of(new CourseGroup()));
        List<CourseGroup> sfg = courseGroupRepository.findByName("sfg");
        assertNotEquals(new ArrayList<>(), sfg);
    }

    @Test
    void testfindByName_Failure() {
        when(courseGroupRepository.findByName("sfg")).thenThrow(new CourseGroupNotFoundException("Sınıf yok"));
        try {
            courseGroupRepository.findByName("sfg");
        } catch (CourseGroupNotFoundException e) {
            assertEquals("Sınıf yok", e.getMessage());
        }
    }

    @Test
    void testfindCourseGroupByBranchId_Success() {
        when(courseGroupRepository.findCourseGroupByCourseId(1L)).thenReturn(List.of(new CourseGroup()));
        List<CourseGroup> courseGroupByBranchId = courseGroupRepository.findCourseGroupByBranchId(1L);
        assertEquals(new ArrayList<>(), courseGroupByBranchId);
    }

    @Test
    void testfindCourseGroupByBranchId_Failure() {
        when(courseGroupRepository.findCourseGroupByCourseId(1L)).thenThrow(new CourseGroupNotFoundException("Sınıf yok"));
        try {
            courseGroupRepository.findCourseGroupByBranchId(1L);
        } catch (CourseGroupNotFoundException e) {
            assertEquals("Sınıf yok", e.getMessage());
        }
    }

    @Test
    void testfindCourseGroupByCourseId_Success() {
        when(courseGroupRepository.findCourseGroupByCourseId(1L)).thenReturn(List.of(new CourseGroup()));
        List<CourseGroup> courseGroupByCourseId = courseGroupRepository.findCourseGroupByCourseId(1L);
        assertNotEquals(new ArrayList<>(), courseGroupByCourseId);
    }

    @Test
    void testfindCourseGroupByCourseId_Failure() {
        when(courseGroupRepository.findCourseGroupByCourseId(1L)).thenThrow(new CourseGroupNotFoundException("Sınıf yok"));
        try {
            courseGroupRepository.findCourseGroupByCourseId(1L);
        } catch (CourseGroupNotFoundException e) {
            assertEquals("Sınıf yok", e.getMessage());
        }
    }

    @Test
    void testfindCourseGroupByTrainerId_Success(){
        List<CourseGroup> allCourseGroups = new ArrayList<>();
        CourseGroup courseGroup1 = new CourseGroup();
        courseGroup1.setOid(1L);
        courseGroup1.setTrainers(List.of(22L, 33L, 44L,55L));
        CourseGroup courseGroup2 = new CourseGroup();
        courseGroup2.setOid(2L);
        courseGroup2.setTrainers(List.of(22L, 33L, 44L));
        allCourseGroups.add(courseGroup1);
        allCourseGroups.add(courseGroup2);
        when(courseGroupRepository.findAll()).thenReturn(allCourseGroups);
        List<CourseGroup> result = courseGroupService.findCourseGroupByTrainerId(44L);
        //Sınıfın içindeki trainer'da 2 tane 44 id'li trainer var eğer 55 verilirse 55 id'li trainer 1 sınıfta oldugu için result.size() 1 beklenecek!!
        assertEquals(2, result.size(), "Sınıf sayısı uyuşmuyor");
    }

    @Test
    void testfindCourseGroupByTrainerId_Failure(){
        List<CourseGroup> allCourseGroups = new ArrayList<>();
        CourseGroup courseGroup = new CourseGroup();
        courseGroup.setOid(1L);
        courseGroup.setTrainers(List.of(22L,33L,44L));
        allCourseGroups.add(courseGroup);
        when(courseGroupRepository.findAll()).thenReturn(allCourseGroups);
        when(courseGroupService.findCourseGroupByTrainerId(22L)).thenThrow(new CourseGroupNotFoundException("Sınıf yok"));
        try {
            courseGroupService.findCourseGroupByTrainerId(22L);
        }catch (CourseGroupNotFoundException e){
            assertEquals("Sınıf yok",e.getMessage());
        }
    }
}
