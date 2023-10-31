package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.repositories.IBranchRepository;
import org.springframework.stereotype.Service;

@Service
public class BranchService {

    private IBranchRepository branchRepository;

    public BranchService(IBranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }


    public MessageResponseDto create(CreateBranchRequestDto dto) {
        return new MessageResponseDto();
//        CRUD metodlari icin acilan task icinde guncellenecek.
    }
}
