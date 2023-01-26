package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends BaseRepository<Classroom,Long> {

}
