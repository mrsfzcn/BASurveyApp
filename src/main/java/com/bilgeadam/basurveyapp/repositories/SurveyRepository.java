package com.bilgeadam.basurveyapp.repositories;


import com.bilgeadam.basurveyapp.dto.response.SurveyResponseByEmailTokenDto;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends BaseRepository<Survey, Long> {

    Optional<Survey> findOptionalBySurveyTitle(String surveyTitle);

    @Query("SELECT s FROM Survey s JOIN FETCH s.questions qo " +
            "JOIN FETCH qo.questionSurveys qs " +
            "WHERE s.oid = :surveyId " +
            "AND s.state = 'ACTIVE' " +
            "ORDER BY qs.order ASC")
    Optional<Survey> findSurveyWithOrderedQuestions(@Param("surveyId") Long surveyId);


}