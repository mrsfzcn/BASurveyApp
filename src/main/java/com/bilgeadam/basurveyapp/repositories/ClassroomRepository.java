package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomRepository extends BaseRepository<Classroom, Long> {

    Optional<Classroom> findActiveByName(String name);
}
