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

    // anket id si üzerinden anketi cevaplaması gereken bir studentTag'e ait öğrencilerin toplam sayısını bulan sorgu
    @Query(value = "select distinct ste.target_entities_oid from survey_registration as sr \n" +
            "inner join studenttags_target_entities as ste \n" +
            "on sr.student_tag_oid = ste.student_tags_oid  where sr.survey_oid = ?1 and ste.student_tags_oid = ?2",nativeQuery = true)
    List<Long> findTotalStudentBySurveyOid(Long surveyOid,Long studentTagOid);


    // anket id si üzerinden anketi cevaplaması gereken bir studentTag'e ait öğrencilerin isimlerini bulan sorgu
    @Query(value = "select distinct  u.first_name,u.last_name from survey_registration as sr \n" +
            "inner join studenttags_target_entities as ste \n" +
            "on sr.student_tag_oid = ste.student_tags_oid \n" +
            "inner join students as s\n" +
            "on ste.target_entities_oid = s.oid\n" +
            "inner join users as u\n" +
            "on s.user_oid = u.oid\n" +
            "where sr.survey_oid = ?1 and ste.student_tags_oid = ?2",nativeQuery = true)
    List<String> findStudentNameBySurveyOid(Long surveyOid,Long studentTagOid);


}