package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ResponseRepository extends BaseRepository<Response, Long> {
    @Query("SELECT COUNT(r) > 0 FROM Response r WHERE r.user.oid = ?1")
    Boolean isSurveyAnsweredByUser(Long userOid);

    @Query("SELECT r FROM Response r WHERE r.user.email = ?1 AND r.question.oid IN ?2")
    List<Response> findAllResponsesOfUserFromSurvey(String userEmail, List<Long> surveyQuestionOidList);

    @Query("SELECT r FROM Response r WHERE r.user.oid = ?1 AND r.question.oid = ?2")
    Optional<Response> findByUserOidAndQuestionOid(Long userOid, Long questionOid);

    @Query("SELECT r FROM Response r WHERE r.state='ACTIVE' AND r.user.oid= ?1 AND r.survey.oid = ?2")
    Set<Response> findResponsesByUserOidAndSurveyOid(Long userOid, Long surveyOid);
}