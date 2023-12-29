package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.dto.request.CreateCourseGroupRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateCourseGroupRequestDto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponse2Dto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.CourseGroup;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.CourseGroupNotFoundException;
import com.bilgeadam.basurveyapp.manager.ICourseGroupManager;
import com.bilgeadam.basurveyapp.mapper.ICourseGroupMapper;
import com.bilgeadam.basurveyapp.repositories.ICourseGroupRepository;
import com.bilgeadam.basurveyapp.repositories.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseGroupService{
    private final ICourseGroupManager courseGroupManager;
    private final ICourseGroupRepository courseGroupRepository;

    private final TrainerRepository trainerRepository;

    public CourseGroupService(ICourseGroupManager courseGroupManager, ICourseGroupRepository courseGroupRepository, TrainerRepository trainerRepository) {
        super();
        this.courseGroupManager = courseGroupManager;
        this.courseGroupRepository = courseGroupRepository;
        this.trainerRepository = trainerRepository;
    }

    /**
     * Verileri FakeAPI'dan çekmeye arayan get all datası içindi Başarılı gerçekleşti gerek kalmadı!!
     *
     * @return FakeAPI'daki verilerin hepsi
     */
    @Deprecated
    public List<CourseGroupModelResponseDto> getAllDataFromCourseGroup() {
        List<CourseGroupModelResponseDto> allData = courseGroupManager.findall().getBody();
        if (allData.isEmpty()) {
            throw new RuntimeException("Data boştur");
        }
        for (CourseGroupModelResponseDto groupCourses: allData){
            createCourseGroup(CreateCourseGroupRequestDto.builder()
                    .apiId("CourseGroup-"+groupCourses.getId())
                    .name(groupCourses.getName())
                    .courseId(groupCourses.getCourseId())
                    .branchId(groupCourses.getBranchId())
                    .trainers(groupCourses.getTrainers())
                    .startDate(groupCourses.getStartDate())
                    .endDate(groupCourses.getEndDate())
                    .build());
        }
        return allData;
    }

    public MessageResponseDto createCourseGroup(CreateCourseGroupRequestDto dto){
        courseGroupRepository.save(ICourseGroupMapper.INSTANCE.toCourseGroup(dto));
        return MessageResponseDto.builder()
                .successMessage(dto.getName()+ "isim sınıf" + dto.getCourseId() + "kurs" + dto.getBranchId()+ "şubesine ait" + dto.getTrainers() + " sahip eğitmenleri" + dto.getStartDate() + "baslangıç tarihi" + dto.getEndDate() + "bitiş tarihli sınıf eklendi.")
                .build();
    }

    public List<CourseGroup> findAllCourseGroup(){
        return courseGroupRepository.findAll();
    }

    public Boolean deleteCourseGroupByOid(Long oid){
        Optional<CourseGroup> optionalCourseGroup = courseGroupRepository.findById(oid);
        if (optionalCourseGroup.isEmpty()){
            throw new CourseGroupNotFoundException("Boyle bir sınıf bulunamamistir");
        }
        try {
            courseGroupRepository.softDeleteById(oid);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public Boolean existsByApiId(String apiId){
        return courseGroupRepository.existsByApiId(apiId);
    }

    public List<CourseGroup> findByGroupName(String name) {
        List<CourseGroup> courseGroupByName = courseGroupRepository.findByName(name);
        if (courseGroupByName.isEmpty())
            throw new CourseGroupNotFoundException("Bu isimle herhangi bir sınıf bulunamamıştır!!");
        return courseGroupByName;
    }

    public List<CourseGroup> findCourseGroupByCourseId(Long courseId) {
        List<CourseGroup> courseGroupByCourseId = courseGroupRepository.findCourseGroupByCourseId(courseId);
        if (courseGroupByCourseId.isEmpty())
            throw new CourseGroupNotFoundException("Bu id'ye ait herhangi bir sınıf bulunamamıştır!!");
        return courseGroupByCourseId;
    }

    public List<CourseGroup> findCourseGroupByBranchId(Long branchId) {
        List<CourseGroup> courseGroupByBranchId = courseGroupRepository.findCourseGroupByBranchId(branchId);
        if (courseGroupByBranchId.isEmpty())
            throw new CourseGroupNotFoundException("Bu şubeye ait herhangi bir sınıf bulunamamıştır!!");
        return courseGroupByBranchId;
    }

    public List<CourseGroup> findCourseGroupByTrainerId(Long trainerId) {
        List<CourseGroup> allCourseGroups = courseGroupRepository.findAll();
        List<CourseGroup> trainersGroups = allCourseGroups.stream().filter(group -> {
            if (group.getTrainers() != null) {
                return group.getTrainers().contains(trainerId);
            }
            return false;
        }).toList();
        if (trainersGroups.isEmpty()) {
            throw new CourseGroupNotFoundException("Bu eğitmene ait bir sınıf bulunamamıştır!!");
        }
        return trainersGroups;
    }

    //BU UPDATE SORULUP KONUŞULACAK ONA GÖRE DEĞİŞTİRİLİP KONTROL YAPILACAKTIR ŞİMDİLİK BÖYLE BIRAKILDI !!
    public MessageResponseDto updateCourseGroupByApiId(UpdateCourseGroupRequestDto dto) {
        Optional<CourseGroup> optionalCourseGroup = courseGroupRepository.findByApiIdAndState(dto.getApiId(), State.ACTIVE);
        if (dto.getCourseId() == null && dto.getName() == null && dto.getBranchId() == null && dto.getTrainers() == null && dto.getStartDate() == null && dto.getEndDate() == null)
            return MessageResponseDto.builder()
                    .successMessage("Güncelleme işlemi başarısız oldu. Lütfen verilerin doğrulugunu kontrol edin.")
                    .build();
        else {
            optionalCourseGroup.get().setCourseId(dto.getCourseId());
            optionalCourseGroup.get().setBranchId(dto.getBranchId());
            optionalCourseGroup.get().setTrainers(dto.getTrainers());
            optionalCourseGroup.get().setName(dto.getName());
            optionalCourseGroup.get().setStartDate(dto.getStartDate());
            optionalCourseGroup.get().setEndDate(dto.getEndDate());
            courseGroupRepository.save(optionalCourseGroup.get());
            return MessageResponseDto.builder()
                    .successMessage("Güncelleme işlemi başarıyla gerçekleşti!!")
                    .build();
        }
    }

    public List<CourseGroupModelResponse2Dto> getAllDataForFrontendTable() {
        List<CourseGroup> listCorse= courseGroupRepository.findAll();
        List<CourseGroupModelResponse2Dto> listDto =new ArrayList<>();

        for (CourseGroup obj: listCorse) {
            CourseGroupModelResponse2Dto  courseGroupModelDto;
            if (obj.getTrainers()!=null){
            courseGroupModelDto= CourseGroupModelResponse2Dto.builder()
                    .id(obj.getOid())
                    .name(obj.getName())
                    .startDate(obj.getStartDate())
                    .endDate(obj.getEndDate())
                    .masterTrainer(trainerRepository.findActiveById(obj.getTrainers().get(0)).get().getUser().getFirstName()+" "+trainerRepository.findActiveById(obj.getTrainers().get(0)).get().getUser().getLastName())
                    .assistantTrainer(trainerRepository.findActiveById(obj.getTrainers().get(1)).get().getUser().getFirstName()+" "+trainerRepository.findActiveById(obj.getTrainers().get(1)).get().getUser().getLastName())
                    .build();
            listDto.add(courseGroupModelDto);
            }else {
                courseGroupModelDto= CourseGroupModelResponse2Dto.builder()
                        .id(obj.getOid())
                        .name(obj.getName())
                        .startDate(obj.getStartDate())
                        .endDate(obj.getEndDate())
                        .masterTrainer("Atanmadı")
                        .assistantTrainer("Atanmadı")
                        .build();
                listDto.add(courseGroupModelDto);
            }
        }
        return  listDto;
    }


}