package com.bilgeadam.basurveyapp.repository;

import com.bilgeadam.basurveyapp.entity.ResponseText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResponseTextRepository extends JpaRepository<ResponseText, Long> {


}

