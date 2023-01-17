package com.bilgeadam.basurveyapp.repositories.irepository;

import com.bilgeadam.basurveyapp.entity.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionTypeRepository extends JpaRepository<QuestionType, Long> {
}
