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
    @Query("SELECT r.users FROM Role r WHERE r.state = 'ACTIVE' AND r.role = ?1")
    List<User> findUsersWithRole(String role);
}
