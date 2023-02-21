package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.SurveyRegistration;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SurveyRegistrationRepository extends BaseRepository<SurveyRegistration, Long> {

    List<SurveyRegistration> findAllByEndDateAfter(LocalDateTime localDateTime);

    SurveyRegistration findByClassroomOidAndSurveyOid(Long classroomOid, Long surveyOid);
}