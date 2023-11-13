package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.EmailService;
import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.request.StudentModelResponse;
import com.bilgeadam.basurveyapp.dto.request.CreateCourseGroupRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.BranchModelResponseDto;
import com.bilgeadam.basurveyapp.dto.response.CourseGroupModelResponseDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerModelResponseDto;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.BranchNotFoundException;
import com.bilgeadam.basurveyapp.manager.IBranchManager;
import com.bilgeadam.basurveyapp.manager.IStudentManager;
import com.bilgeadam.basurveyapp.manager.ICourseGroupManager;
import com.bilgeadam.basurveyapp.manager.ITrainerManager;
import com.bilgeadam.basurveyapp.repositories.SurveyRegistrationRepository;
import com.bilgeadam.basurveyapp.utilty.Helpers;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Schedule işlemleri bu service içerisinde çözümlenmeli.
 */
@Service
public class JobService {
    private final ITrainerManager trainerManager;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final SurveyRegistrationRepository surveyRegistrationRepository;
    private final StudentTagService studentTagService;

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    private final QrCodeService qrCodeService;
    private final StudentService studentService;
    private final TrainerService trainerService;
    private final RoleService roleService;

    private final BranchService branchService;

    private final IBranchManager branchManager;
    private final ICourseGroupManager courseGroupManager;
    private final CourseGroupService courseGroupService;

    private final IStudentManager studentManager;

    public JobService(ITrainerManager trainerManager, EmailService emailService, JwtService jwtService, SurveyRegistrationRepository surveyRegistrationRepository, StudentTagService studentTagService, PasswordEncoder passwordEncoder, UserService userService, QrCodeService qrCodeService, StudentService studentService, TrainerService trainerService, RoleService roleService, BranchService branchService, IBranchManager branchManager, ICourseGroupManager courseGroupManager, CourseGroupService courseGroupService,IStudentManager studentManager) {
        this.trainerManager = trainerManager;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.surveyRegistrationRepository = surveyRegistrationRepository;
        this.studentTagService = studentTagService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.qrCodeService = qrCodeService;
        this.studentService = studentService;
        this.trainerService = trainerService;
        this.roleService = roleService;
        this.branchService = branchService;
        this.branchManager = branchManager;
        this.courseGroupManager = courseGroupManager;
        this.courseGroupService = courseGroupService;
        this.studentManager = studentManager;
    }

    /**
     * cron = "0 30 9 * * MON-FRI"
     * cron = "*1 * * * * *" -> everySecond
     * cron = "0 *1 * * * *" -> everyMinute
     *
     * @throws MessagingException
     */
//    private List<Student> getStudentsByStudentTag(StudentTag studentTag) {
//
//        return studentTagService.getStudentsByStudentTag(studentTag);
//    }
    @Async
    @Scheduled(cron = "0 30 9 * * MON-FRI") // Haftaiçi her gün 9.30'da kontrollerini yapıyor.
    public void initiateSurveys() throws MessagingException {
        List<SurveyRegistration> surveyRegistrations = surveyRegistrationRepository.findAllByEndDateAfter(LocalDateTime.now());

        if (surveyRegistrations.size() != 0) {

            Map<String, String> emailTokenMap = new HashMap<>();

//TODO student listesinden student tag classroom tage eşit olanları student listesi olarak dönecek
            surveyRegistrations
                    .parallelStream()
                    .filter(sR -> sR.getStartDate().toLocalDate().equals(LocalDate.now()))
                    .forEach(sR -> studentTagService.getStudentsByStudentTag(sR.getStudentTag())
                            .stream()
                            .forEach(student -> emailTokenMap.put(
                                    student.getUser().getEmail(),
                                    jwtService.generateSurveyEmailToken(
                                            student.getUser().getOid(),
                                            sR.getSurvey().getOid(),
                                            sR.getStudentTag().getOid(),
                                            student.getUser().getEmail(),
                                            (int) ChronoUnit.DAYS.between(sR.getEndDate(), sR.getStartDate())))));

            emailService.sendSurveyMail(emailTokenMap);
        }
//        logger.info("Scheduled - " + Thread.currentThread().getId() + " - " + LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void getDatasFromApi() {
        checkTrainerData(trainerManager.findAll().getBody());
        checkBranchData(branchManager.findAll().getBody());
        checkCourseGroupData(courseGroupManager.findall().getBody());
        //Api'dan gelen Student ve CourseGroup en son kaydedilmeli çünkü bağımlılıklar mevcut.
        checkStudentData(studentManager.findAll().getBody());
    }

    /**
     * API'dan getirilen trainer verilerini silinmesi,değiştrilmesi ve yeni eklenmesi gibi durumlara göre güncelleyen/silen/ekleyen metod
     *
     * @param trainers API'dan çekilen trainer listesi.
     */
    void checkTrainerData(List<TrainerModelResponseDto> trainers) {

        List<Role> roles = roleService.findRoles();
        List<User> savedTrainers = userService.findByApiIdContainsAndState("trainer-", State.ACTIVE);
        List<User> deletedTrainers = new ArrayList<>();
        savedTrainers.forEach(user -> {
            Optional<TrainerModelResponseDto> first = trainers.stream().filter(trainer -> user.getApiId().equals("trainer-" + trainer.getId())).findFirst();
            if (first.isEmpty())
                deletedTrainers.add(user);
        });
        if (!deletedTrainers.isEmpty())
            deletedTrainers.forEach(user -> userService.softDeleteTrainer(user.getOid()));
        for (TrainerModelResponseDto trainer : trainers) {
            Optional<User> user = userService.findByApiId("trainer-" + trainer.getId());
            if (user.isEmpty()) {
                User savedUser = userService.save(toTrainer(trainer, roles));
                Trainer apiTrainer = new Trainer();
                apiTrainer.setMasterTrainer(trainer.getTrainerRole().name().equals("MASTER_TRAINER"));
                apiTrainer.setUser(savedUser);
                trainerService.createTrainer(apiTrainer);
            } else {
                if(!compareApiAndAppTrainerData(trainer,user.get())){
                    User updatedUser = userService.updateTrainerWithApiData(user.get().getEmail(), UserUpdateRequestDto.builder()
                            .firstName(trainer.getName())
                            .lastName(trainer.getSurname())
                            .email(trainer.getEmail())
                            .authorizedRole(trainer.getTrainerRole().name())
                            .build());
                    Optional<Trainer> optionalTrainer = trainerService.findByUser(updatedUser);
                    if (optionalTrainer.isPresent() && optionalTrainer.get().getState().equals(State.DELETED)) {
                        optionalTrainer.get().setMasterTrainer(trainer.getTrainerRole().name().equals("MASTER_TRAINER"));
                        optionalTrainer.get().setUser(updatedUser);
                        optionalTrainer.get().setState(State.ACTIVE);
                        trainerService.createTrainer(optionalTrainer.get());
                    }
                }
            }
        }
    }

    /**
     * API'dan gelen trainer bilgileri ile SurveyApp database'indeki iz düşümünü karşılaştırıp değişiklik kontrolü yapan metod.
     * @param trainer API'dan gelen trainer bilgisi
     * @param user  SurveyApp database'indeki trainer'a uygun user bilgisi
     * @return Değişiklik varsa false dönüyor. Değişiklik yoksa true dönüyor.
     */
    public boolean compareApiAndAppTrainerData(TrainerModelResponseDto trainer, User user) {
        return trainer.getName().equals(user.getFirstName()) && trainer.getSurname().equals(user.getLastName()) && trainer.getEmail().equals(user.getEmail()) && trainer.getTrainerRole().name().equals(user.getAuthorizedRole());
    }

    /**
     * API'dan gelen trainerların User'a çevrilme işlemlerini çözen metod.
     *
     * @param trainer API'dan alınan trainer verisi
     * @param roles   Database'de kayıtlı olan roller.
     * @return Trainer verisi ile oluşturulmuş User nesnesi
     */
    User toTrainer(TrainerModelResponseDto trainer, List<Role> roles) {
        Optional<Role> firstRole = roles.stream().filter(role -> role.getRole().equals(trainer.getTrainerRole().name())).findFirst();
        String password = Helpers.generatePassword();
        System.out.println(trainer.getName() + " şifresi: " + password);
        return User.builder()
                .apiId("trainer-" + trainer.getId())
                .firstName(trainer.getName())
                .lastName(trainer.getSurname())
                .password(passwordEncoder.encode(password))
                .email(trainer.getEmail())
                .authorizedRole(trainer.getTrainerRole().name())
                .roles(firstRole.map(Set::of).orElse(null))
                .twoFactory(false)
                .twoFactorKey(qrCodeService.generateSecret())
                .build();
    }


    private void checkBranchData(List<BranchModelResponseDto> baseApiBranches) {
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

    private void checkCourseGroupData(List<CourseGroupModelResponseDto> baseApiCourseGroup) {
        if (baseApiCourseGroup.isEmpty())
            throw new RuntimeException("Sınıf ile ilgili herhangi bir veri bulunamamıştır.");
        List<CourseGroup> currentCourseGroups = courseGroupService.findAllCourseGroup();
        List<CourseGroup> deletedCourseGroups = new ArrayList<>();

        currentCourseGroups.forEach(cCourseGroup -> {
            Optional<CourseGroupModelResponseDto> optCourseGroup = baseApiCourseGroup.stream().filter(courseGroup -> ("CourseGroup-" + courseGroup.getId()).equals(cCourseGroup.getApiId())).findFirst();
            if (optCourseGroup.isEmpty()) {
                deletedCourseGroups.add(cCourseGroup);
            }
        });
        if (!deletedCourseGroups.isEmpty()) {
            deletedCourseGroups.forEach(dCourseGroup -> courseGroupService.deleteCourseGroupByOid(dCourseGroup.getOid()));
        }
        for (CourseGroupModelResponseDto courseGroupApi : baseApiCourseGroup) {
            boolean existsByApiId = courseGroupService.existsByApiId("CourseGroup-" + courseGroupApi.getId());
            if (!existsByApiId) {
                courseGroupService.createCourseGroup(CreateCourseGroupRequestDto.builder()

                        .apiId("CourseGroup-" + courseGroupApi.getId())
                        .name(courseGroupApi.getName())
                        .courseId(courseGroupApi.getCourseId())
                        .branchId(courseGroupApi.getCourseId())
                        .trainers(courseGroupApi.getTrainers())
                        .startDate(courseGroupApi.getStartDate())
                        .endDate(courseGroupApi.getEndDate())
                        .build());
            }
        }
    }


    private void checkStudentData(List<StudentModelResponse> students) {

        List<Role> roles = roleService.findRoles();
        List<User> savedStudents = userService.findByApiIdContainsAndState("student-", State.ACTIVE);
        List<User> deletedStudents = new ArrayList<>();
        savedStudents.forEach(savedStudent -> {
            Optional<StudentModelResponse> first = students.stream().filter(student -> savedStudent.getApiId().equals("student-" + student.getId())).findFirst();
            if (first.isEmpty())
                deletedStudents.add(savedStudent);
        });
        if (!deletedStudents.isEmpty())
            deletedStudents.forEach(user -> userService.deleteUser(user.getOid()));
        for (StudentModelResponse student : students) {
            Optional<User> user = userService.findByApiId("student-" + student.getId());
            if (user.isEmpty()) {
                User savedUser = userService.save(toUser(student, roles));
                Student apiStudent = new Student();
                apiStudent.setBaBoostEmail(student.getBaBoostEmail());
                apiStudent.setBaEmail(student.getBaEmail());
                apiStudent.setUser(savedUser);
                Branch apiBranch = new Branch();
                apiBranch.setOid(student.getBranchId());
                apiStudent.setBranch(apiBranch);
                 /* CourseGroup apiGroup = new CourseGroup();
                    apiGroup.setOid(student.getGroupId());
                    apiStudent.setCourseGroup(apiGroup); CourseGroup verileri çekildiğinde açılacak. CourseGroup verileri yokken @ManyToOne anotasyonu yüzünden hata verebilir.*/
                studentService.createStudent(apiStudent);
            } else {
                if (!compareApiAndAppStudentData(student, user.get())) {
                    User updatedUser = userService.updateTrainerWithApiData(user.get().getEmail(), UserUpdateRequestDto.builder()
                            .firstName(student.getName())
                            .lastName(student.getSurname())
                            .email(student.getPersonalEmail())
                            .authorizedRole("STUDENT")
                            .build());
                    Optional<Student> optionalStudent = studentService.findByUser(updatedUser);
                    if (optionalStudent.isPresent()) {
                        optionalStudent.get().setBaEmail(student.getBaEmail());
                        optionalStudent.get().setBaBoostEmail(student.getBaBoostEmail());
                        optionalStudent.get().setUser(updatedUser);
                        Branch updatedBranch = new Branch();
                        updatedBranch.setOid(student.getBranchId());
                        optionalStudent.get().setBranch(updatedBranch);
                       /* CourseGroup updatedCourseGroup = new CourseGroup();
                        updatedCourseGroup.setOid(student.getGroupId());
                        optionalStudent.get().setCourseGroup(updatedCourseGroup); CourseGroup verileri çekildiğinde açılacak. CourseGroup verileri yokken @ManyToOne anotasyonu yüzünden hata verebilir.*/
                        optionalStudent.get().setState(State.ACTIVE);
                        studentService.createStudent(optionalStudent.get());
                    }
                }
            }
        }

    }

    User toUser(StudentModelResponse student, List<Role> roles) {
        Optional<Role> firstRole = roles.stream().filter(role -> role.getRole().equals("STUDENT")).findFirst();
        return User.builder()
                .apiId("student-" + student.getId())
                .firstName(student.getName())
                .lastName(student.getSurname())
                .password(null)
                .email(student.getPersonalEmail())
                .authorizedRole("STUDENT")
                .roles(firstRole.map(Set::of).orElse(null))
                .twoFactory(false)
                .twoFactorKey(null)
                .build();
    }

    public boolean compareApiAndAppStudentData(StudentModelResponse student, User user) {
        return student.getName().equals(user.getFirstName()) && student.getSurname().equals(user.getLastName()) && student.getPersonalEmail().equals(user.getEmail()) && user.getAuthorizedRole().equals("STUDENT");
    }
}
