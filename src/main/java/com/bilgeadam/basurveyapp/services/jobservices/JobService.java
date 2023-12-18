package com.bilgeadam.basurveyapp.services.jobservices;

import com.bilgeadam.basurveyapp.configuration.EmailService;
import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.entity.*;
import com.bilgeadam.basurveyapp.exceptions.custom.InvalidFormatException;
import com.bilgeadam.basurveyapp.manager.*;
import com.bilgeadam.basurveyapp.repositories.SurveyRegistrationRepository;
import com.bilgeadam.basurveyapp.services.*;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
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

    private final TrainerJob trainerJob;
    private final BranchJob branchJob;
    private final CourseJob courseJob;
    private final CourseGroupJob courseGroupJob;
    private final StudentJob studentJob;


    private final ITrainerManager trainerManager;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final SurveyRegistrationRepository surveyRegistrationRepository;
    private final StudentTagService studentTagService;
    private final ICourseManager courseManager;
    private final IBranchManager branchManager;
    private final ICourseGroupManager courseGroupManager;
    private final IStudentManager studentManager;
    private final ThreadPoolTaskScheduler taskScheduler;

    private String currentUpdateTime = "00:00"; //Mevcut schedule zamanını tutmak için eklendi.

    public JobService(TrainerJob trainerJob, BranchJob branchJob, CourseJob courseJob, CourseGroupJob courseGroupJob, StudentJob studentJob, ITrainerManager trainerManager, EmailService emailService, JwtService jwtService, SurveyRegistrationRepository surveyRegistrationRepository, StudentTagService studentTagService, ICourseManager courseManager, IBranchManager branchManager, ICourseGroupManager courseGroupManager, IStudentManager studentManager, ThreadPoolTaskScheduler taskScheduler) {
        this.trainerJob = trainerJob;
        this.branchJob = branchJob;
        this.courseJob = courseJob;
        this.courseGroupJob = courseGroupJob;
        this.studentJob = studentJob;
        this.trainerManager = trainerManager;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.surveyRegistrationRepository = surveyRegistrationRepository;
        this.studentTagService = studentTagService;
        this.courseManager = courseManager;
        this.branchManager = branchManager;
        this.courseGroupManager = courseGroupManager;
        this.studentManager = studentManager;
        this.taskScheduler = taskScheduler;

        this.taskScheduler.initialize();
        Runnable task = () -> {
            getDatasFromApi();
        };
        this.taskScheduler.schedule(task, new CronTrigger("0 0 0 * * *"));
    }

    /**
     * Parametre olarak String saat bilgisi alıp buna uygun Scheduling işlemini yapan metod.
     * Uygun bir parametre almadığında InvalidFormatException hatası fırlatır.
     *
     * @param newTime işlemin tekrarlanması istenilen zaman örneğin: "12:30"
     */
    public void rescheduleGetDatasFromApi(String newTime) {
        String[] split;
        try {
            split = newTime.split(":");
            int hour = Integer.parseInt(split[0]);
            int minute = Integer.parseInt(split[1]);
            if (hour > 23 || hour < 0)
                throw new Exception();
            if (minute > 59 || minute < 0)
                throw new Exception();
        } catch (Exception e) {
            throw new InvalidFormatException("Geçerli bir saat değeri girilmedi!");
        }
        String newCron = "0 " + split[1] + " " + split[0] + " * * *";
        currentUpdateTime = newTime;
        taskScheduler.destroy();
        taskScheduler.initialize();
        Runnable task = () -> {
            getDatasFromApi();
        };
        taskScheduler.schedule(task, new CronTrigger(newCron));
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

    public void getDatasFromApi() {
        trainerJob.checkTrainerData(trainerManager.findAll().getBody());
        branchJob.checkBranchData(branchManager.findAll().getBody());
        courseGroupJob.checkCourseGroupData(courseGroupManager.findall().getBody());
        //Api'dan gelen Student ve CourseGroup en son kaydedilmeli çünkü bağımlılıklar mevcut.
        studentJob.checkStudentData(studentManager.findAll().getBody());
        courseJob.checkCourseData(courseManager.findAll().getBody());
    }

    public String getCurrentUpdateTime() {
        return currentUpdateTime;
    }
}
