package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTagRepository extends BaseRepository<QuestionTag, Long> {
    @Query(value = "SELECT oid from questiontags_target_entities where oid IN (SELECT oid FROM questiontags where tag_string = ?1)", nativeQuery = true)
    List<Long> findAllByTagString(String role);

}
