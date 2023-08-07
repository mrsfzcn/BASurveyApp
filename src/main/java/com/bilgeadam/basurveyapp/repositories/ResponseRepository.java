package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ResponseRepository extends BaseRepository<Response, Long> {
    @Query("SELECT COUNT(r) > 0 FROM Response r WHERE r.user.oid = ?1 AND r.survey.oid = ?2")
    Boolean isSurveyAnsweredByUser(Long userOid, Long surveyOid);

    @Query("SELECT r FROM Response r WHERE r.user.email = ?1 AND r.survey.oid = ?2 AND r.question.oid IN ?3")
    List<Response> findAllResponsesOfUserFromSurvey(String userEmail, Long surveyOid, List<Long> surveyQuestionOidList);

    @Query("SELECT r FROM Response r WHERE r.user.email = ?1")
    List<Response> findAllResponsesOfUser(String userEmail);

    @Query("SELECT r FROM Response r WHERE r.user.oid = ?1 AND r.question.oid = ?2")
    Optional<Response> findByUserOidAndQuestionOid(Long userOid, Long questionOid);

    @Query("SELECT r FROM Response r WHERE r.state='ACTIVE' AND r.user.oid= ?1 AND r.survey.oid = ?2")
    Set<Response> findResponsesByUserOidAndSurveyOid(Long userOid, Long surveyOid);

    Set<Response> findBySurveyAndUser(Survey survey, User user);

    Set<Response> findSetByUser(User user);

    List<Response> findListByUser(User user);

    // anket id si üzerinden bir studentTag'e ait öğrencilerin anketi cevaplamış olanların sayısını bulan sorgu
    @Query(value = "select count(*) from surveys_students_who_answered as sswa\n" +
            "inner join students as s \n" +
            "on sswa.students_who_answered_oid = s.oid\n" +
            "inner join studenttags_target_entities as ste\n" +
            "on ste.target_entities_oid = s.oid\n" +
            "where sswa.surveys_answered_oid = ?1\n" +
            "and ste.student_tags_oid = ?2",nativeQuery = true)
    Integer findByStudentAnsweredSurvey(Long surveyOid,Long studentTagOid);
    @Query(value = "select count(*) from surveys_students_who_answered as sswa\n" +
            "inner join students as s \n" +
            "on sswa.students_who_answered_oid = s.oid\n" +
            "inner join studenttags_target_entities as ste\n" +
            "on ste.target_entities_oid = s.oid\n" +
            "where sswa.surveys_answered_oid = ?1"
            ,nativeQuery = true)
    Integer findByStudentAnsweredSurvey(Long surveyOid);





}