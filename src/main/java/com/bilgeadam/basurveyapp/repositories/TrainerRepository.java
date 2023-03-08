package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends BaseRepository<Trainer, Long> {
}
