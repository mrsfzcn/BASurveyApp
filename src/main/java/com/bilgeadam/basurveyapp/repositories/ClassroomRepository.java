package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends BaseRepository<Classroom, Long> {

    @Query("SELECT c FROM Classroom c WHERE c.name = ?1 AND c.state = 'ACTIVE' ORDER BY c.name ASC")
    Optional<Classroom> findActiveByName(String name);

    @Query("SELECT c.users FROM Classroom c WHERE c.oid = ?1")
    List<User> findUsersInClassroom(Long classroomOid);
}
