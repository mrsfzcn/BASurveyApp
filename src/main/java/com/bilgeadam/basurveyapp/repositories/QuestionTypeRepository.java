package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionTypeRepository extends BaseRepository<QuestionType, Long> {


    Optional<QuestionType> findByOidAndState(Long oid, State state);

    Optional<QuestionType> findOptionalByQuestionType(String typeString);
}
