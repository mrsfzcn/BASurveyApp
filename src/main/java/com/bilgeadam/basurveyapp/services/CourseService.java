package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateCourseRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.entity.Course;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.CourseAlreadyExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.CourseNotFoundException;
import com.bilgeadam.basurveyapp.manager.ICourseManager;
import com.bilgeadam.basurveyapp.mapper.ICourseMapper;
import com.bilgeadam.basurveyapp.repositories.ICourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final ICourseRepository courseRepository;
    private final ICourseManager courseManager;

    public CourseService(ICourseRepository courseRepository, ICourseManager courseManager) {
        this.courseRepository = courseRepository;
        this.courseManager = courseManager;
    }

    public CourseResponseDto create(CreateCourseRequestDto dto){
        Optional<Course> optionalCourse = courseRepository.findByApiId(dto.getApiId());
        Optional<Course> byName = courseRepository.findByName(dto.getName());
        if (optionalCourse.isPresent()){
            if (optionalCourse.get().getState().equals(State.ACTIVE)){
                throw new CourseAlreadyExistException("Eklemeye calistiginiz kurs zaten mevcut");
            }
            else {
                return new CourseResponseDto("Eklemeye calistiginiz kurs sistemde mevcut fakat Silinmis. Lutfen Kurs aktif et metodunu kullaniniz.");
            }
        }
        if (byName.isPresent()){
            if (byName.get().getState().equals(State.ACTIVE)){
                throw new CourseAlreadyExistException("Eklemeye calistiginiz kurs zaten mevcut");
            }
            else {
                return new CourseResponseDto("Eklemeye calistiginiz kurs sistemde mevcut fakat Silinmis. Lutfen Kurs aktif et metodunu kullaniniz.");
            }
        }
        courseRepository.save(ICourseMapper.INSTANCE.toCourse(dto));
        return new CourseResponseDto(dto.getName()+" isimli kurs eklendi");
    }

    public Boolean deleteCourseByOid(Long oid){
        Optional<Course> optionalCourse = courseRepository.findById(oid);
        if (optionalCourse.isEmpty()){
            throw new CourseNotFoundException("Silmeye calistiginiz kurs sistemde mevcut degil");
        }
        try{
            courseRepository.softDeleteById(oid);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public List<Course> findAllActiveCourses(){
        List<Course> courseList=courseRepository.findAllActive();
        if (courseList.isEmpty()){
            throw new CourseNotFoundException("Sistemde aktif kurs bulunmamaktadir");
        }
        return courseList;
    }

    public List<Course> findAllCourses(){
        List<Course> courseList=courseRepository.findAll();
        if (courseList.isEmpty()){
            throw new CourseNotFoundException("Sistemde kurs bulunmamaktadir");
        }
        return courseList;
    }

    public List<Course> findCoursesByName(String name){
        List<Course> courseList=courseRepository.findByNameIgnoreCase(name);
        if (courseList.isEmpty()){
            throw new CourseNotFoundException("herhangi bir kurs bulunmamaktadir");
        }
        return courseList;
    }

    public Course findByApiId(String apiId) {
        Optional<Course> optionalCourse = courseRepository.findByApiId(apiId);
        if (optionalCourse.isEmpty()) {
            throw new BranchNotFoundException("Herhangi bir şube bulunamadı.");
        }
        return optionalCourse.get();
    }

    public List<Course> findAllDeletedCourses(){
        List<Course> deletedCourses = courseRepository.findAllByState(State.DELETED);
        if (deletedCourses.isEmpty()){
            throw new CourseNotFoundException("Sistemde silinmis kurs bulunmamaktadir");
        }
        return deletedCourses;
    }
    public Boolean existByApiId(String apiId) {
        return courseRepository.existsByApiId(apiId);
    }

    public MessageResponseDto activateCourse(String apiId){
        Optional<Course> optionalCourse = courseRepository.findByApiId(apiId);
        if (optionalCourse.isEmpty()){
            throw new CourseNotFoundException("Aktif etmek istediginiz kurs sistemde mevcut degil");
        }
        Course course = optionalCourse.get();
        course.setState(State.ACTIVE);
        courseRepository.save(course);
        return new MessageResponseDto(course.getName()+" isimli kurs aktif edildi");
    }

    public MessageResponseDto updateCourseByApiId(UpdateCourseRequestDto dto){
        Optional<Course> optionalCourse = courseRepository.findByApiId(dto.getApiId());
        if (optionalCourse.isEmpty()){
            throw new CourseNotFoundException("Guncellemeye calistiginiz kurs sistemde mevcut degil");
        }
        if(courseRepository.existsByName(dto.getName())){
            if (!dto.getName().equals(optionalCourse.get().getName())){
                throw new CourseAlreadyExistException("Bu isimde bir kurs zaten mevcut");
            }
        }
        if (!optionalCourse.get().getName().equals(dto.getName())){
            optionalCourse.get().setName(dto.getName());
            courseRepository.save(optionalCourse.get());
            return new MessageResponseDto("Guncelleme islemi basarili");
        }
        else {
            return new MessageResponseDto("Herhangi bir degisiklik yapilmadi");
        }
    }







}
