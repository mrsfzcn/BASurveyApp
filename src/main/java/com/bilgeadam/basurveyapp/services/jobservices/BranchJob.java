package com.bilgeadam.basurveyapp.services.jobservices;

import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.response.BranchModelResponseDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchNotFoundException;
import com.bilgeadam.basurveyapp.services.BranchService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BranchJob {
    private final BranchService branchService;

    public BranchJob(BranchService branchService) {
        this.branchService = branchService;
    }

    public void checkBranchData(List<BranchModelResponseDto> baseApiBranches) {
        if (baseApiBranches.isEmpty()) {
            throw new BranchNotFoundException("Şube ile ilgili herhangi bir data bulunamamıştır.");
        }
        List<Branch> currentBranches = branchService.findAllBranches(); // SurveyApp uzerindeki veriler
        List<Branch> deletedBranches = new ArrayList<>();

        currentBranches.forEach(cBranch->{
            Optional<BranchModelResponseDto> first = baseApiBranches.stream().filter(branch ->("Branch-" + branch.getId()).equals(cBranch.getApiId())).findFirst();
            if (first.isEmpty()) {
                deletedBranches.add(cBranch);
            }
        });

        if (!deletedBranches.isEmpty()) {
            deletedBranches.forEach(dBranch->branchService.deleteBranchByOid(dBranch.getOid()));
        }

        for (BranchModelResponseDto baseApiBranch : baseApiBranches) {
            boolean existsByApiId = branchService.existByApiId("Branch-" + baseApiBranch.getId());
            if (!existsByApiId) {
                branchService.create(CreateBranchRequestDto.builder().apiId("Branch-"+baseApiBranch.getId()).name(baseApiBranch.getName()).city(baseApiBranch.getCity()).build());
            }
        }
    }
}
