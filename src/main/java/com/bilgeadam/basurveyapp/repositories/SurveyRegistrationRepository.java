package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.SurveyRegistration;
import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SurveyRegistrationRepository extends BaseRepository<SurveyRegistration, Long> {

    List<SurveyRegistration> findAllByEndDateAfter(LocalDateTime localDateTime);

    @Query(value = "SELECT student_tag_oid FROM survey_registration WHERE oid = ?1", nativeQuery = true)
    Long findStudentTagOfSurveyRegistration(Long surveyRegistrationOid);

    SurveyRegistration findByStudentTagAndSurveyOid(StudentTag studentTag, Long surveyOid);
}