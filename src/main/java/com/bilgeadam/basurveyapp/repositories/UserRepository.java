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

    @Query("SELECT u.email FROM User u WHERE u.state = 'ACTIVE' AND u.roles IN (SELECT r FROM Role r WHERE r.role = 'STUDENT') ORDER BY u.email ASC")
    List<String> findStudentEmails();

    @Query("SELECT u FROM User u WHERE u.state = 'ACTIVE' AND u.roles IN (SELECT r FROM Role r WHERE r.role = 'STUDENT') ORDER BY u.email ASC")
    List<User> findStudents();

    @Query("SELECT u FROM User u WHERE u.state = 'ACTIVE' AND u.roles IN (SELECT r FROM Role r WHERE r.role = 'MASTER_TRAINER') ORDER BY u.email ASC")
    List<User> findMasterTrainers();

    @Query("SELECT u FROM User u WHERE u.state = 'ACTIVE' AND u.roles IN (SELECT r FROM Role r WHERE r.role = 'ASSISTANT_TRAINER') ORDER BY u.email ASC")
    List<User> findAssistantTrainers();

    @Query("SELECT u FROM User u WHERE u.state = 'ACTIVE' AND u.roles IN (SELECT r FROM Role r WHERE r.role = 'MANAGER') ORDER BY u.email ASC")
    List<User> findManagers();

    @Query("SELECT u FROM User u WHERE u.state = 'ACTIVE' AND u.roles IN (SELECT r FROM Role r WHERE r.role = 'ADMIN') ORDER BY u.email ASC")
    List<User> findAdmins();

    @Query("SELECT u FROM User u WHERE u.state = 'ACTIVE' AND u.roles IN (SELECT r FROM Role r WHERE r.role = 'STUDENT' OR r.role = 'ASSISTANT_TRAINER' OR r.role = 'MASTER_TRAINER') ORDER BY u.email ASC")
    List<User> findTrainersAndStudents();
}
