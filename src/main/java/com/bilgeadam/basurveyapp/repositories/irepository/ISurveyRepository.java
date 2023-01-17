package com.bilgeadam.basurveyapp.repositories.irepository;
import com.bilgeadam.basurveyapp.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISurveyRepository extends JpaRepository<Survey, Long>{
}