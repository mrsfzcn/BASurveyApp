package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.ResponseText;
import com.bilgeadam.basurveyapp.repository.IResponseTextRepository;

import com.bilgeadam.basurveyapp.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class ResponseTextService  extends ServiceManager<ResponseText,Long> {

    private final IResponseTextRepository responseTextRepository;

    public ResponseTextService(IResponseTextRepository responseTextRepository) {
        super(responseTextRepository);
        this.responseTextRepository = responseTextRepository;
    }
}
