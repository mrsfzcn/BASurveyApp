package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends BaseRepository<Trainer, Long> {
    @Query("SELECT t FROM Trainer t WHERE t.state = 'ACTIVE' AND t.user.oid = ?1")
    Optional<Trainer> findTrainerByUserOid(Long oid);

    @Query(value = "SELECT * FROM trainers tr WHERE tr.state = 'ACTIVE' AND  tr.is_master_trainer = 'true'", nativeQuery = true)
    List<Trainer> findAllMasterTrainers();

    @Query(value = "SELECT * FROM trainers WHERE state = 'ACTIVE' AND  is_master_trainer = 'false'", nativeQuery = true)
    List<Trainer> findAllAssistantTrainers();

    @Query(value = "SELECT t FROM Trainer t WHERE t.state = 'ACTIVE' AND t.user.email = ?1")
    Optional<Trainer> findActiveByEmail(String email);

    @Query("SELECT t FROM Trainer t WHERE t.state = 'ACTIVE' AND t.oid = ?1")
    Optional<Trainer> findTrainerByTrainerOid(Long oid);

}
