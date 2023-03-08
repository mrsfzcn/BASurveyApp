package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Manager;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends BaseRepository<Manager, Long> {
}
