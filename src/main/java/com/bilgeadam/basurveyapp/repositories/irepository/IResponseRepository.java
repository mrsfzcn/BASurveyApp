package com.bilgeadam.basurveyapp.repositories.irepository;

import com.bilgeadam.basurveyapp.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResponseRepository extends JpaRepository<Response, Long> {


}

