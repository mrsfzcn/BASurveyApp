package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionTagRepository extends BaseRepository<QuestionTag, Long> {
    @Query(value = "SELECT target_entities_oid from questiontags_target_entities where question_tag_oid IN (SELECT oid FROM questiontags where tag_string = ?1)", nativeQuery = true)
    List<Long> findAllByTagString(String role);

//    @Query(value ="SELECT * FROM questiontags WHERE state='ACTIVE' AND tag_string = ?1 ", nativeQuery = true)
    Optional<QuestionTag> findOptionalByTagString(String tagString);
}
