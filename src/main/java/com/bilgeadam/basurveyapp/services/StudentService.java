package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.StudentUpdateDto;
import com.bilgeadam.basurveyapp.dto.response.StudentResponseDto;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.exceptions.custom.ResourceNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.SurveyAlreadyAssignToClassException;
import com.bilgeadam.basurveyapp.exceptions.custom.TagAlreadyAssignedException;
import com.bilgeadam.basurveyapp.mapper.StudentMapper;
import com.bilgeadam.basurveyapp.repositories.StudentRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentTagService studentTagService;

    public void createStudent(Student student) {
        studentRepository.save(student);
    }

    public StudentResponseDto updateStudent(StudentUpdateDto dto)   {
        Optional<Student> studentOptional = studentRepository.findActiveById(dto.getStudentOid());
        Optional<StudentTag> studentTagOptional = studentTagService.findActiveById(dto.getStudentTagOid());

        Student student = studentOptional.orElseThrow(() -> new ResourceNotFoundException("Student is not found"));
        StudentTag studentTag = studentTagOptional.orElseThrow(() -> new ResourceNotFoundException("Student tag is not found"));

        if (!student.getStudentTags().isEmpty()) {
            throw new TagAlreadyAssignedException("The student already has a tag assigned");
        }
        if (student.getStudentTags().contains(studentTag)) {
            throw new TagAlreadyAssignedException("This tag is already assigned to the student");
        }

        student.getStudentTags().add(studentTag);
        studentTag.getTargetEntities().add(student);

        studentRepository.save(student);
        studentTagService.save(studentTag);

        return StudentMapper.INSTANCE.toStudentResponseDto(student);
    }

    public Optional<Student> findByUser(User currentUser) {

        return studentRepository.findByUser(currentUser.getOid());
    }

    public List<Student> findByStudentTagOid(Long studentTagOid) {

        return studentRepository.findByStudentTagOid(studentTagOid);
    }

    public List<StudentResponseDto> getStudentList() {

        List<Student> students = studentRepository.findAllStudents();
        return StudentMapper.INSTANCE.toStudentResponseDtoList(students);
    }

    public void save(Student student) {

        studentRepository.save(student);
    }

    public Optional<Student> findByOid(Long studentOid) {

        return studentRepository.findById(studentOid);
    }

    public void deleteByStudentOid(Long oid) {
       Optional<Student> student =  studentRepository.findByUser(oid);
       studentRepository.softDeleteById(student.get().getOid());
    }
}
