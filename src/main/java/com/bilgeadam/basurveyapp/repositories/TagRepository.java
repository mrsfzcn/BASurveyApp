package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Tag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends BaseRepository<Tag, Long> {
    @Query(value = "SELECT oid FROM tag where tag_string = ?1", nativeQuery = true)
    List<Long> findAllByTagString(String role);

}
