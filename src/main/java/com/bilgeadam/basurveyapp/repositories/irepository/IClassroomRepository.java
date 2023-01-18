package com.bilgeadam.basurveyapp.repositories.irepository;

import com.bilgeadam.basurveyapp.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClassroomRepository extends JpaRepository<Classroom,Long> {

}
