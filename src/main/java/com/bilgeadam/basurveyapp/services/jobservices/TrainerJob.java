package com.bilgeadam.basurveyapp.services.jobservices;

import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.TrainerModelResponseDto;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.Trainer;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.services.*;
import com.bilgeadam.basurveyapp.utilty.Helpers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TrainerJob {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final QrCodeService qrCodeService;

    private final TrainerService trainerService;
    private final RoleService roleService;

    public TrainerJob(PasswordEncoder passwordEncoder, UserService userService, QrCodeService qrCodeService, TrainerService trainerService, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.qrCodeService = qrCodeService;
        this.trainerService = trainerService;
        this.roleService = roleService;
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
                User savedUser = userService.save(toUser(trainer, roles));
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
                    if (optionalTrainer.isPresent()) {
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
     * API'dan gelen trainerların User'a çevrilme işlemlerini çözen metod.
     *
     * @param trainer API'dan alınan trainer verisi
     * @param roles   Database'de kayıtlı olan roller.
     * @return Trainer verisi ile oluşturulmuş User nesnesi
     */
    User toUser(TrainerModelResponseDto trainer, List<Role> roles) {
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

    /**
     * API'dan gelen trainer bilgileri ile SurveyApp database'indeki iz düşümünü karşılaştırıp değişiklik kontrolü yapan metod.
     * @param trainer API'dan gelen trainer bilgisi
     * @param user  SurveyApp database'indeki trainer'a uygun user bilgisi
     * @return Değişiklik varsa false dönüyor. Değişiklik yoksa true dönüyor.
     */
    public boolean compareApiAndAppTrainerData(TrainerModelResponseDto trainer, User user) {
        return trainer.getName().equals(user.getFirstName()) && trainer.getSurname().equals(user.getLastName()) && trainer.getEmail().equals(user.getEmail()) && trainer.getTrainerRole().name().equals(user.getAuthorizedRole());
    }
}
