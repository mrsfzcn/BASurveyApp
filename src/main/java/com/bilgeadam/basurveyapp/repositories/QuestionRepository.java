package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends BaseRepository<Question, Long> {
    @Query("SELECT q.oid FROM Question q WHERE q.survey.oid = ?1")
    List<Long> findSurveyQuestionOidList(Long surveyOid);

    @Query("SELECT q FROM Question q WHERE q.survey.oid = ?1")
    List<Question> findSurveyQuestionList(Long surveyOid);
}