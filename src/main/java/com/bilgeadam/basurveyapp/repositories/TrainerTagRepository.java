package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TrainerTagRepository extends BaseRepository<TrainerTag, Long> {
    @Query(value ="SELECT * FROM trainer_tags WHERE oid IN (SELECT trainer_tag_oid FROM trainer_tags_target_entities WHERE  target_entities_oid= ?1) AND state='ACTIVE'", nativeQuery = true)
    Set<TrainerTag> findActiveTrainerTagsByTrainerId(Long trainerOid);
}
