package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Question;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends BaseRepository<Question, Long> {


}

