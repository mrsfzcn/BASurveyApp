package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends BaseRepository<Student, Long> {
    String FIND_BY_USER = "SELECT * FROM students WHERE user_oid = ?1";

    @Query(value = FIND_BY_USER, nativeQuery = true)
    Optional<Student> findByUser(Long currentUserOid);

    @Query(value = "SELECT * FROM students WHERE state = 'ACTIVE' ", nativeQuery = true)
    List<Student> findAllStudents();

    @Query(value = "SELECT * FROM students st WHERE st.state='ACTIVE' AND st.oid IN (SELECT target_entities_oid FROM studenttags_target_entities WHERE student_tags_oid = ?1)",
            nativeQuery = true)
    List<Student> findByStudentTagOid(Long studentTagOid);

    @Query("SELECT st FROM Student st WHERE st.state = 'ACTIVE' AND st.oid = ?1")
    Optional<Student> findStudentByOid(Long oid);

    Optional<Student> findByOid(Long oid);


    Optional<Student> findByUserOid(Long oid);

    @Query("SELECT u FROM Student st JOIN User u ON st.oid = u.oid WHERE st.courseGroup.oid = :courseGroupId")
    Optional<List<User>> findByCourseGroupId(Long courseGroupId);

}
