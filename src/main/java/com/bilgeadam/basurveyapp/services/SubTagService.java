package com.bilgeadam.basurveyapp.services;
//
//import com.bilgeadam.basurveyapp.dto.request.CreateSubtagDto;
//import com.bilgeadam.basurveyapp.dto.response.SubtagResponseDto;
//import com.bilgeadam.basurveyapp.entity.SubTag;
//import com.bilgeadam.basurveyapp.repositories.SubTagRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class SubTagService {
//
//    private final SubTagRepository subTagRepository;
//
//    public void createSubTag(CreateSubtagDto dto) {
//        SubTag subtag = SubTag.builder()
//                .subTagString(dto.getSubTagString())
//                .build();
//        subTagRepository.save(subtag);
//    }
//    public List<SubtagResponseDto> findAll() {
//        List<SubTag> findAllList = subTagRepository.findAllActive();
//        List<SubtagResponseDto> responseDtoList = new ArrayList<>();
//        findAllList.forEach(subTag ->
//                responseDtoList.add(SubtagResponseDto.builder()
//                        .subTagStringId(subTag.getOid())
//                        .subTagString(subTag.getSubTagString())
//                        .build()));
//        return responseDtoList;
//    }
//    public Boolean delete(Long subTagStringId) {
//        Optional<SubTag> deleteSubtag = subTagRepository.findActiveById(subTagStringId);
//        if (deleteSubtag.isEmpty()) {
//            throw new RuntimeException("Subtag is not found");
//        } else {
//            SubTag subTag = deleteSubtag.get();
//            subTagRepository.softDelete(subTag);
//            return true;
//        }
//
//    }
//}
