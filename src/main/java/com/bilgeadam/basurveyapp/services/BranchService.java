package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.response.BranchModelResponse;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchAlreadyExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchNotFoundException;
import com.bilgeadam.basurveyapp.manager.IBranchManager;
import com.bilgeadam.basurveyapp.manager.ITrainerManager;
import com.bilgeadam.basurveyapp.mapper.IBranchMapper;
import com.bilgeadam.basurveyapp.repositories.IBranchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BranchService {

    private final IBranchRepository branchRepository;
    private final IBranchManager branchManager;


    public BranchService(IBranchRepository branchRepository, IBranchManager branchManager) {
        this.branchRepository = branchRepository;
        this.branchManager = branchManager;
    }


    public MessageResponseDto create(CreateBranchRequestDto dto) {
        boolean existsByApiId = branchRepository.existsByApiId(dto.getApiId());
        boolean existsByNameAndCity = branchRepository.existsByNameAndCity(dto.getName(), dto.getCity());
        if (existsByApiId || existsByNameAndCity) {
            throw new BranchAlreadyExistException("Eklemeye calistiginiz branch zaten mevcut");
        }

        branchRepository.save(IBranchMapper.INSTANCE.toBranch(dto));

        return new MessageResponseDto(dto.getName()+" isimli sube "+dto.getCity()+" sehrine eklendi");
    }


    public List<BranchModelResponse> getAllDataFromBaseApi() {

        List<BranchModelResponse> apiBranches = branchManager.findAll().getBody();

        if (apiBranches.isEmpty()) {
            throw new BranchNotFoundException("Branch ile ilgili herhangi bir data bulunamamistir");
        }
        for (BranchModelResponse apiBranch : apiBranches) {
            create(CreateBranchRequestDto.builder().apiId("Branch-"+apiBranch.getId()).name(apiBranch.getName()).city(apiBranch.getCity()).build());
        }

        return apiBranches;
    }
}
