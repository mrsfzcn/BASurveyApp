package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.repositories.irepository.IResponseRepository;
import com.bilgeadam.basurveyapp.repositories.utilities.RepositoryExtension;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ResponseRepositoryImpl extends RepositoryExtension<Response, Long> {
    private final IResponseRepository responseRepository;

    public ResponseRepositoryImpl(IResponseRepository responseRepository) {
        super(responseRepository);
        this.responseRepository = responseRepository;
    }

    public Optional<Response> findByUserOidAndQuestionOid(Long userOid, Long questionOid) {
        return responseRepository.findByUserOidAndQuestionOid(userOid, questionOid);
    }
}
