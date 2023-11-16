package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.request.FindByNameAndCityRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.response.BranchModelResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchAlreadyExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchIsUpToDateException;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.ResponseNotFoundException;
import com.bilgeadam.basurveyapp.manager.IBranchManager;
import com.bilgeadam.basurveyapp.mapper.IBranchMapper;
import com.bilgeadam.basurveyapp.repositories.IBranchRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

    private final IBranchRepository branchRepository;
    private final IBranchManager branchManager;


    public BranchService(IBranchRepository branchRepository, IBranchManager branchManager) {
        this.branchRepository = branchRepository;
        this.branchManager = branchManager;
    }


    public MessageResponseDto create(CreateBranchRequestDto dto) {
        Optional<Branch> optionalBranch = branchRepository.findByApiId(dto.getApiId());
        Optional<Branch> byNameAndCity = branchRepository.findByNameAndCity(dto.getName(), dto.getCity());
        if (optionalBranch.isPresent()) {
            if (optionalBranch.get().getState().equals(State.ACTIVE)) {
                throw new BranchAlreadyExistException("Eklemeye calıştığınız şube zaten mevcut");
            } else {
                return new MessageResponseDto("Eklemeye calıştığınız şube sistemde mevcut fakat silinmiş. Lütfen şube aktif et metodunu kullanınız.");
            }
        }
        if (byNameAndCity.isPresent()) {
            if (byNameAndCity.get().getState().equals(State.ACTIVE)) {
                throw new BranchAlreadyExistException("Eklemeye calıştığınız şube zaten mevcut");
            } else {
                return new MessageResponseDto("Eklemeye calıştığınız şube sistemde mevcut fakat silinmiş. Lutfen şube aktif et metodunu kullanınız.");
            }
        }
        branchRepository.save(IBranchMapper.INSTANCE.toBranch(dto));

        return new MessageResponseDto(dto.getName() + " isimli şube " + dto.getCity() + " şehrine eklendi.");
    }

    /**
     * Metod JobService icine tasindi ve kontroller belirlenen tarih ve saate gore otomatik ayarlandi
     *
     * @return
     */
    @Deprecated
    public List<BranchModelResponseDto> getAllDataFromBaseApi() {

        List<BranchModelResponseDto> baseApiBranches = branchManager.findAll().getBody(); // Base Api uzerinden verileri getirdik.
        if (baseApiBranches.isEmpty()) {
            throw new BranchNotFoundException("Şube ile ilgili herhangi bir data bulunamamistir");
        }
        List<Branch> currentBranches = branchRepository.findAll(); // SurveyApp uzerindeki veriler
        List<Branch> deletedBranches = new ArrayList<>();

        currentBranches.forEach(cBranch -> {
            Optional<BranchModelResponseDto> first = baseApiBranches.stream().filter(branch -> ("Branch-" + branch.getId()).equals(cBranch.getApiId())).findFirst();
            if (first.isEmpty()) {
                deletedBranches.add(cBranch);
            }
        });

        if (!deletedBranches.isEmpty()) {
            deletedBranches.forEach(dBranch -> branchRepository.softDeleteById(dBranch.getOid()));
        }

        for (BranchModelResponseDto baseApiBranch : baseApiBranches) {
            boolean existsByApiId = branchRepository.existsByApiId("Branch-" + baseApiBranch.getId());
            if (!existsByApiId) {
                create(CreateBranchRequestDto.builder().apiId("Branch-" + baseApiBranch.getId()).name(baseApiBranch.getName()).city(baseApiBranch.getCity()).build());
            }
        }
        return baseApiBranches;
    }


    public Boolean deleteBranchByOid(Long oid) {
        Optional<Branch> optionalBranch = branchRepository.findById(oid);
        if (optionalBranch.isEmpty()) {
            throw new BranchNotFoundException("Böyle bir şube bulunamamıştır.");
        }
        try {
            branchRepository.softDeleteById(oid);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<Branch> findAllActiveBranches() {
        List<Branch> branchRepositoryAll = branchRepository.findAllActive();
        if (branchRepositoryAll.isEmpty()) {
            throw new BranchNotFoundException("Herhangi bir şube bulunamadı.");
        }
        return branchRepositoryAll;
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
            throw new BranchNotFoundException("Herhangi bir şube bulunamadı.");
        }
        return branchList;
    }

    public Branch findByNameAndCity(FindByNameAndCityRequestDto dto) {
        Optional<Branch> optionalBranch = branchRepository.findByNameAndCityAndState(dto.getName(), dto.getCity(), State.ACTIVE);

        if (optionalBranch.isEmpty())
            throw new BranchNotFoundException("Herhangi bir şube bulunamadı.");
        return optionalBranch.get();
    }

    public List<Branch> findByCity(String city) {
        List<Branch> branchList = branchRepository.findByCityAndState(city, State.ACTIVE);
        if (branchList.isEmpty())
            throw new BranchNotFoundException("Herhangi bir şube bulunamadı.");
        return branchList;
    }

    public Branch findByApiId(String apiId) {
        Optional<Branch> optionalBranch = branchRepository.findByApiIdAndState(apiId, State.ACTIVE);
        if (optionalBranch.isEmpty()) {
            throw new BranchNotFoundException("Herhangi bir şube bulunamadı.");
        }
        return optionalBranch.get();
    }

    public MessageResponseDto updateBranchByApiId(UpdateBranchRequestDto dto) {
        Optional<Branch> optionalBranch = branchRepository.findByApiIdAndState(dto.getApiId(), State.ACTIVE);
        if (optionalBranch.isEmpty()) {
            throw new BranchNotFoundException("Aradiginiz şube bulunamadı");
        }

        if (branchRepository.existsByNameAndCity(dto.getName(), dto.getCity())) {
            if (!dto.getName().equals(optionalBranch.get().getName()) || !dto.getCity().equals(optionalBranch.get().getCity())) {
                throw new BranchAlreadyExistException("Bu şube sistemde zaten mevcut");
            }
        }

        if (!optionalBranch.get().getCity().equals(dto.getCity()) || !optionalBranch.get().getName().equals(dto.getName())) {
            optionalBranch.get().setName(dto.getName());
            optionalBranch.get().setCity(dto.getCity());
            branchRepository.save(optionalBranch.get());
            return new MessageResponseDto("Kaydetme işlemi başarılı");
        } else {
            return new MessageResponseDto("Herhangi bir değişiklik yapılmadı.");
        }
    }

    public List<Branch> findAllDeletedBranches() {
        List<Branch> deletedBranches = branchRepository.findAllByState(State.DELETED);
        if (deletedBranches.isEmpty()) {
            throw new BranchNotFoundException("Herhangi siliniş bir şube bulunamadı.");
        }
        return deletedBranches;
    }


    public MessageResponseDto activateBranch(Long oid) {
        Optional<Branch> optionalBranch = branchRepository.findByOid(oid);
        if (optionalBranch.isEmpty()) {
            throw new BranchNotFoundException("Boyle bir şube bulunamadı.");
        }

        if (optionalBranch.get().getState().equals(State.ACTIVE)) {
            throw new BranchAlreadyExistException(optionalBranch.get().getName()+ " isimli "+ optionalBranch.get().getCity()+ " şehrinde şube zaten aktif.");
        }

        optionalBranch.get().setState(State.ACTIVE);
        branchRepository.save(optionalBranch.get());
        return new MessageResponseDto(optionalBranch.get().getName() + " isimli " + optionalBranch.get().getCity() + " şehrindeki şube aktif edildi.");
    }

    public Branch refreshSingleBranch(String apiId) {
        Optional<Branch> branch = branchRepository.findByApiId(apiId);
        if (branch.isEmpty())
            throw new BranchNotFoundException("Böyle bir şube bulunamadı!");
        long baseId = Long.parseLong(apiId.split("-")[1]);
        BranchModelResponseDto body = null;
        try {
            body = branchManager.findById(baseId).getBody();
        } catch (Exception e){
            if(e.getMessage().contains("Sube bulunamamistir.")) {
                branch.get().setState(State.DELETED);
                branchRepository.save(branch.get());
                throw new BranchNotFoundException("Bu şube kapatılmıştır.");
            }
            throw new ResponseNotFoundException("Database erişimi başarısız oldu. Lütfen database sahibiyle iletişime geçin!");
        }
        if (checkDifferencesBetween(branch.get(),body)){
            throw new BranchIsUpToDateException("Şube zaten güncel!");
        }
        branch.get().setName(body.getName());
        branch.get().setCity(body.getCity());
        return branchRepository.save(branch.get());
    }

    boolean checkDifferencesBetween(Branch branch,BranchModelResponseDto dto){
        return branch.getName().equals(dto.getName()) && branch.getCity().equals(dto.getCity());
    }
}
