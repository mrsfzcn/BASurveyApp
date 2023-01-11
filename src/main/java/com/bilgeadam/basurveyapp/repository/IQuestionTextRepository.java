package com.bilgeadam.basurveyapp.repository;

import com.bilgeadam.basurveyapp.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionTextRepository extends JpaRepository<Survey, Long> {
}

