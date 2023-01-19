package com.bilgeadam.basurveyapp.repositories.irepository;

import com.bilgeadam.basurveyapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {

    Optional<User> findByOid(Long oid);
}
