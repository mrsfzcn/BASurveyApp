package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TrainerTagRepository extends BaseRepository<TrainerTag, Long> {
    @Query(value ="SELECT * FROM trainertags WHERE oid IN (SELECT trainer_tags_oid FROM trainertags_target_entities " +
            "WHERE  target_entities_oid= ?1) AND state='ACTIVE'", nativeQuery = true)
    Set<TrainerTag> findActiveTrainerTagsByTrainerId(Long trainerOid);

    @Query(value ="SELECT * FROM trainertags WHERE state='ACTIVE' AND tag_string = ?1 ", nativeQuery = true)
    Optional<TrainerTag> findByTrainerTagName(String tagString);
}
