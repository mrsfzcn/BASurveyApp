package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentTagRepository extends BaseRepository<StudentTag, Long> {

    @Query(value = "SELECT * FROM students WHERE oid IN (SELECT target_entities_oid FROM student_tags_target_entities WHERE student_tag_oid = ?1)", nativeQuery = true)
    List<Student> getStudentsByStudentTag(Long studentTagId);
}
