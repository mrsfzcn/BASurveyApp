package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClassroomRepository extends BaseRepository<Classroom,Long> {

}
