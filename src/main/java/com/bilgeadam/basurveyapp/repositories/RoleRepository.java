package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Eralp Nitelik
 */
@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {
    @Query(value = "SELECT * FROM users WHERE state = 'ACTIVE' AND oid IN (SELECT users_oid FROM users_roles WHERE roles_oid = (SELECT oid FROM roles WHERE role = ?1)) ORDER BY email", nativeQuery = true)
    List<User> findUsersWithRole(String role);

    @Query(value = "SELECT * FROM roles WHERE state = 'ACTIVE' AND role = ?1", nativeQuery = true)
    Role findActiveRole(String role);
}
