package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBranchRepository extends BaseRepository<Branch,Long> {

    boolean existsByApiId(String apiId);

    Optional<Branch> findByApiId(String apiId);

    boolean existsByNameAndCity(String name, String city);

    List<Branch> findByNameAndState(String name, State state);

    Optional<Branch> findByNameAndCityAndState(String name, String city,State state);
    Optional<Branch> findByNameAndCity(String name, String city);


    List<Branch> findByCityAndState(String city,State state);

    Optional<Branch> findByApiIdAndState(String apiId,State state);
}
