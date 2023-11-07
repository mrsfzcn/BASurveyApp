package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.request.FindByNameAndCityRequestDto;
import com.bilgeadam.basurveyapp.dto.response.BranchModelResponse;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchAlreadyExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchNotFoundException;
import com.bilgeadam.basurveyapp.manager.IBranchManager;
import com.bilgeadam.basurveyapp.manager.ITrainerManager;
import com.bilgeadam.basurveyapp.mapper.IBranchMapper;
import com.bilgeadam.basurveyapp.repositories.IBranchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Metod JobService icine tasindi ve kontroller belirlenen tarih ve saate gore otomatik ayarlandi
     * @return
     */
    @Deprecated
    public List<BranchModelResponse> getAllDataFromBaseApi() {

        List<BranchModelResponse> baseApiBranches = branchManager.findAll().getBody(); // Base Api uzerinden verileri getirdik.
        if (baseApiBranches.isEmpty()) {
            throw new BranchNotFoundException("Branch ile ilgili herhangi bir data bulunamamistir");
        }
        List<Branch> currentBranches = branchRepository.findAll(); // SurveyApp uzerindeki veriler
        List<Branch> deletedBranches = new ArrayList<>();

        currentBranches.forEach(cBranch->{
            Optional<BranchModelResponse> first = baseApiBranches.stream().filter(branch ->("Branch-" + branch.getId()).equals(cBranch.getApiId())).findFirst();
            if (first.isEmpty()) {
                deletedBranches.add(cBranch);
            }
        });

        if (!deletedBranches.isEmpty()) {
            deletedBranches.forEach(dBranch->branchRepository.softDeleteById(dBranch.getOid()));
        }

        for (BranchModelResponse baseApiBranch : baseApiBranches) {
            boolean existsByApiId = branchRepository.existsByApiId("Branch-" + baseApiBranch.getId());
            if (!existsByApiId) {
                create(CreateBranchRequestDto.builder().apiId("Branch-"+baseApiBranch.getId()).name(baseApiBranch.getName()).city(baseApiBranch.getCity()).build());
            }
        }
        return baseApiBranches;
    }


    public Boolean deleteBranchByOid(Long oid) {
        Optional<Branch> optionalBranch = branchRepository.findById(oid);
        if (optionalBranch.isEmpty()) {
            throw new BranchNotFoundException("Boyle bir sube bulunamamistir");
        }
        try {
            branchRepository.softDeleteById(oid);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<Branch> findAllBranches() {
        return branchRepository.findAll();
    }

    public Boolean existByApiId(String apiId) {
        return branchRepository.existsByApiId(apiId);
    }

    public List<Branch> findBranchesByName(String name) {
        List<Branch> branchList = branchRepository.findByNameAndState(name, State.ACTIVE);
        if (branchList.isEmpty()) {
            throw new BranchNotFoundException("Herhangi bir branch bulunamadi");
        }
        return branchList;
    }

    public Branch findByNameAndCity(FindByNameAndCityRequestDto dto) {
        Optional<Branch> optionalBranch = branchRepository.findByNameAndCityAndState(dto.getName(), dto.getCity(),State.ACTIVE);

        if (optionalBranch.isEmpty())
            throw new BranchNotFoundException("Herhangi bir branch bulunamadi");
        return optionalBranch.get();
    }

    public List<Branch> findByCity(String city) {
        List<Branch> branchList = branchRepository.findByCityAndState(city,State.ACTIVE);
        if (branchList.isEmpty())
            throw new BranchNotFoundException("Herhangi bir branch bulunamadi");
        return branchList;
    }

    public Branch findByApiId(String apiId) {
        Optional<Branch> optionalBranch = branchRepository.findByApiIdAndState(apiId,State.ACTIVE);
        if (optionalBranch.isEmpty()) {
            throw new BranchNotFoundException("Herhangi bir branch bulunamadi");
        }
        return optionalBranch.get();
    }
}
