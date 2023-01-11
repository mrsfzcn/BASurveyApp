package com.bilgeadam.basurveyapp.repository;

import com.bilgeadam.basurveyapp.entity.QuestionNumeric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionNumericRepository extends JpaRepository<QuestionNumeric,Long> {

}
