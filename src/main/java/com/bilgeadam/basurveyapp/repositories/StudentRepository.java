package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends BaseRepository<Student, Long> {
    String FIND_BY_USER = "SELECT * FROM students WHERE user_oid = ?1";
    @Query(value = FIND_BY_USER, nativeQuery = true)
    Optional<Student> findByUser(Long currentUserOid);
}
