package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.UpdateStudentRequestDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionNotFoundException;
import com.bilgeadam.basurveyapp.mapper.StudentMapper;
import com.bilgeadam.basurveyapp.repositories.StudentRepository;
import com.bilgeadam.basurveyapp.repositories.StudentTagRepository;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentTagRepository studentTagRepository;
    private final StudentTagService studentTagService;


    public Boolean createStudent(Student student) {
        studentRepository.save(student);
        return true;
    }

    public Boolean updateStudent(UpdateStudentRequestDto dto){

       Optional<Student> student = studentRepository.findById(dto.getOid());
       if(student.isPresent()){
           Optional<StudentTag> studentTag = studentTagRepository.findByTagString(dto.getTagString());
           if (studentTag.isPresent()){
               student.get().getStudentTags().add(studentTag.get());
               studentTag.get().getTargetEntities().add(student.get());
               studentRepository.save(student.get());
               studentTagRepository.save(studentTag.get());
               return true;
           }
           else {
               throw new QuestionNotFoundException("StudentTag is not found");
           }

       }
       else {
           throw new QuestionNotFoundException("Student is not found");
       }


    }

    public Optional<Student> findByUser(User currentUser) {
        return studentRepository.findByUser(currentUser.getOid());
    }

    public List<Student> findByStudentTagOid(Long studentTagOid) {
        List<Student> studentList = studentTagService.findByStudentTagOid(studentTagOid);
        return studentList;
    }

    public List<StudentResponseDto> getStudentList() {

        List<Student> students = studentRepository.findAllStudents();

        List<StudentResponseDto> dto = StudentMapper.INSTANCE.toStudentResponseDtoList(students);
        return dto;
    }
}
