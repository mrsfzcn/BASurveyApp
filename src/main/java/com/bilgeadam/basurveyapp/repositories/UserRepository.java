package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.state = 'ACTIVE' AND u.email = ?1")
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.state = 'ACTIVE' AND u.email IN ?1")
    List<User> findAllByEmails(List<String> emails);

    @Query(value = "SELECT email FROM users WHERE state = 'ACTIVE' AND oid IN (SELECT ur.users_oid FROM users_roles ur WHERE ur.roles_oid = (SELECT r.oid FROM roles r WHERE r.role = 'STUDENT')) ORDER BY email", nativeQuery = true)
    List<String> findStudentEmails();

    @Query(value = "SELECT * FROM users WHERE state = 'ACTIVE' AND oid IN (SELECT users_oid FROM users_roles WHERE roles_oid = (SELECT oid FROM roles WHERE role = 'STUDENT')) ORDER BY email", nativeQuery = true)
    List<User> findStudents();

    @Query(value = "SELECT * FROM users WHERE state = 'ACTIVE' AND oid IN (SELECT users_oid FROM users_roles WHERE roles_oid = (SELECT oid FROM roles WHERE role = 'MASTER_TRAINER')) ORDER BY email", nativeQuery = true)
    List<User> findMasterTrainers();

    @Query(value = "SELECT * FROM users WHERE state = 'ACTIVE' AND oid IN (SELECT users_oid FROM users_roles WHERE roles_oid = (SELECT oid FROM roles WHERE role = 'ASSISTANT_TRAINER')) ORDER BY email", nativeQuery = true)
    List<User> findAssistantTrainers();

    @Query(value = "SELECT * FROM users WHERE state = 'ACTIVE' AND oid IN (SELECT users_oid FROM users_roles WHERE roles_oid = (SELECT oid FROM roles WHERE role = 'MANAGER')) ORDER BY email", nativeQuery = true)
    List<User> findManagers();

    @Query(value = "SELECT * FROM users WHERE state = 'ACTIVE' AND oid IN (SELECT users_oid FROM users_roles WHERE roles_oid = (SELECT oid FROM roles WHERE role = 'ADMIN')) ORDER BY email", nativeQuery = true)
    List<User> findAdmins();

    @Query(value = "SELECT * FROM users WHERE state = 'ACTIVE' AND oid IN (SELECT users_oid FROM users_roles WHERE roles_oid IN (SELECT oid FROM roles WHERE role = 'STUDENT' OR role = 'ASSISTANT_TRAINER' OR role = 'MASTER_TRAINER')) ORDER BY email", nativeQuery = true)
    List<User> findTrainersAndStudents();

    @Query(value = "SELECT * FROM users WHERE state = 'ACTIVE' AND oid IN (SELECT users_oid FROM users_roles WHERE roles_oid IN (SELECT oid FROM roles WHERE role != 'ADMIN')) ORDER BY email", nativeQuery = true)
    List<User> findAllByRolesNotADMIN();
}
