package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface QuestionRepository extends BaseRepository<Question, Long> {
    @Query(value = "SELECT questions_oid FROM questions_surveys WHERE surveys_oid = ?1", nativeQuery = true)
    List<Long> findSurveyQuestionOidList(Long surveyOid);

    @Query(value = "SELECT * FROM questions WHERE oid IN (SELECT questions_oid FROM questions_surveys WHERE surveys_oid = ?1)", nativeQuery = true)
    List<Question> findSurveyQuestionList(Long surveyOid);

    @Query(value = "SELECT * FROM questions WHERE state = 'ACTIVE' AND oid IN (SELECT questions_oid FROM questions_surveys WHERE surveys_oid = ?1)", nativeQuery = true)
    List<Question> findSurveyActiveQuestionList(Long surveyOid);

    @Query(value = "SELECT * FROM questions WHERE oid IN (SELECT questions_oid FROM questions_tag WHERE tag_oid IN ?1)", nativeQuery = true)
    Optional<List<Question>> findQuestionsByTagIds(List<Long> tagIds);

    @Query("SELECT qt FROM QuestionTag qt WHERE qt.state = 'ACTIVE' AND qt.tagString = ?1")
    Optional<QuestionTag> findByTagString(String trainerTagString);

    Optional<Question> findByQuestionString(String questionString);
}