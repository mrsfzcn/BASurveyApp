package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.ResponseNumeric;
import com.bilgeadam.basurveyapp.repository.IResponseNumericRepository;
import com.bilgeadam.basurveyapp.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class ResponseNumericService extends ServiceManager<ResponseNumeric, Long> {
    private final IResponseNumericRepository responseNumericRepository;
    public ResponseNumericService(IResponseNumericRepository responseNumericRepository) {
        super(responseNumericRepository);
        this.responseNumericRepository = responseNumericRepository;
    }
}
