package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTypeRepository extends BaseRepository<QuestionType, Long> {
}
