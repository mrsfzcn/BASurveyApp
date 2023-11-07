package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.EmailService;
import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS;
import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerModelResponse;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.SurveyRegistration;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.enums.State;
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
import java.util.stream.Collectors;

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


    public JobService(ITrainerManager trainerManager, EmailService emailService, JwtService jwtService, SurveyRegistrationRepository surveyRegistrationRepository, StudentTagService studentTagService, PasswordEncoder passwordEncoder, UserService userService, QrCodeService qrCodeService, StudentService studentService, TrainerService trainerService, RoleService roleService) {
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
    }

    /**
     * API'dan getirilen trainer verilerini silinmesi,değiştrilmesi ve yeni eklenmesi gibi durumlara göre güncelleyen/silen/ekleyen metod
     *
     * @param trainers API'dan çekilen trainer listesi.
     */
    void checkTrainerData(List<TrainerModelResponse> trainers) {

        List<Role> roles = roleService.findRoles();
        List<User> savedTrainers = userService.findByApiIdContainsAndState("trainer-", State.ACTIVE);
        List<User> deletedTrainers = new ArrayList<>();
        savedTrainers.forEach(user -> {
            Optional<TrainerModelResponse> first = trainers.stream().filter(trainer -> user.getApiId().equals("trainer-" + trainer.getId())).findFirst();
            if (first.isEmpty())
                deletedTrainers.add(user);
        });
        if (!deletedTrainers.isEmpty())
            deletedTrainers.forEach(user -> userService.softDeleteTrainer(user.getOid()));
        for (TrainerModelResponse trainer : trainers) {
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
    public boolean compareApiAndAppTrainerData(TrainerModelResponse trainer, User user) {
        return trainer.getName().equals(user.getFirstName()) && trainer.getSurname().equals(user.getLastName()) && trainer.getEmail().equals(user.getEmail()) && trainer.getTrainerRole().name().equals(user.getAuthorizedRole());
    }

    /**
     * API'dan gelen trainerların User'a çevrilme işlemlerini çözen metod.
     *
     * @param trainer API'dan alınan trainer verisi
     * @param roles   Database'de kayıtlı olan roller.
     * @return Trainer verisi ile oluşturulmuş User nesnesi
     */
    User toTrainer(TrainerModelResponse trainer, List<Role> roles) {
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
}
