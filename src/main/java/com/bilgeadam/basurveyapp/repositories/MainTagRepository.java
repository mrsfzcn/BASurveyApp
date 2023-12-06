package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.entity.enums.Tags;
import com.bilgeadam.basurveyapp.entity.tags.MainTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MainTagRepository extends BaseRepository<MainTag, Long>{
    @Query(value = "select count(*)>0 from MainTag a where a.tagClass=?1 and a.tagName=?2")
    Boolean isTagClassAndTagName(Tags tagClass, String tagName);
    Optional<List<MainTag>> findOptionalByTagName(String tagName);

    List<MainTag> findByTagName(String tagName);
    Optional<MainTag> findOptionalByTagNameAndTagClass(String tagName, Tags tagClass);
    Optional<List<MainTag>> findOptionalByTagClass(Tags tagClass);

    Optional<MainTag> findOptionalByTagNameAndTagClassAndState(String tagName, Tags tagClass, State state);

    Optional<List<MainTag>> findOptionalByTagNameAndState(String tagName, State state);

    Optional<MainTag> findByTagNameAndTagClassAndState(String oldTagName, Tags tags, State state);

    Optional<MainTag> findByTagNameAndTagClass(String oldTagName, Tags tags);
}

