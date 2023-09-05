package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.tags.StudentTag;
import com.bilgeadam.basurveyapp.entity.tags.TrainerTag;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TrainerTagRepository extends BaseRepository<TrainerTag, Long> {
    @Query(value ="SELECT * FROM trainertags WHERE oid IN (SELECT trainer_tags_oid FROM trainertags_target_entities " +
            "WHERE  target_entities_oid= ?1) AND state='ACTIVE'", nativeQuery = true)
    Set<TrainerTag> findActiveTrainerTagsByTrainerId(Long trainerOid);

    @Query(value ="SELECT * FROM trainertags WHERE state='ACTIVE' AND tag_string = ?1 ", nativeQuery = true)
    Optional<TrainerTag> findByTrainerTagName(String tagString);

    @Query(value ="SELECT tag_string FROM trainertags WHERE oid IN (SELECT trainer_tags_oid FROM trainertags_target_entities " +
            "WHERE  target_entities_oid= ?1) AND state='ACTIVE'", nativeQuery = true)
    List<String> findActiveTrainerTagsByTrainerEmail(Long trainerOid);


    @Query(value ="SELECT oid FROM users WHERE users.state='ACTIVE' AND oid IN (SELECT user_oid FROM trainers WHERE oid IN (SELECT target_entities_oid  FROM trainertags_target_entities " +
            "WHERE  trainer_tags_oid= (SELECT oid FROM trainertags WHERE oid= ?1)))", nativeQuery = true)
    List<Long> findByTagOid(Long tagOid);

    @Query(value ="SELECT target_entities_oid FROM trainertags_target_entities WHERE trainer_tags_oid=?1", nativeQuery = true)
    List<Long> findTrainerOidByTrainerTagOid(Long Oid);

    Optional<TrainerTag> findOptionalByTagString(String tagString);

}
