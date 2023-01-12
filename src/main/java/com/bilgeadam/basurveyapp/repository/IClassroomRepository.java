package com.bilgeadam.basurveyapp.repository;

import com.bilgeadam.basurveyapp.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClassroomRepository extends JpaRepository<Classroom,Long> {

}
