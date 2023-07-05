package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentTagRepository extends BaseRepository<StudentTag, Long> {


    @Query("SELECT st FROM StudentTag st WHERE st.state='ACTIVE' AND st.tagString = ?1")
    Optional<StudentTag> findByTagName(String studentTag);

    @Query("SELECT s FROM Student s JOIN s.user u JOIN s.studentTags t WHERE s.state = 'ACTIVE' AND t.oid = :studentTagOid")
    List<Student> findByStudentTagOid(@Param("studentTagOid") Long studentTagOid);

    @Query(value = "SELECT user_oid FROM students WHERE state='ACTIVE' AND oid IN (SELECT target_entities_oid FROM studenttags_target_entities WHERE student_tags_oid = ?1)", nativeQuery = true)
    List<Long> findUserOidByStudentTagOid(Long studentTagOid);

    @Query(value = "select survey_oid from survey_registration where student_tag_oid = ?1 ",nativeQuery = true)
    List<Long> findSurveyCountByStudentTag(Long studentTagOid);

}
