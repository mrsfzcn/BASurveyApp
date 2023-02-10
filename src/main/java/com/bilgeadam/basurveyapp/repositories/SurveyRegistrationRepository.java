package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.SurveyRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRegistrationRepository extends JpaRepository<SurveyRegistration, Long> {
}