package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Manager;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends BaseRepository<Manager, Long> {
    @Query(value = "SELECT * FROM managers WHERE user_oid = ?1",nativeQuery = true)
    Optional<Manager> findByUserOid(Long oid);
}
