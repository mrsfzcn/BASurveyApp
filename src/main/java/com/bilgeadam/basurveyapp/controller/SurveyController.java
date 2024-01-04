package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Student;
import com.bilgeadam.basurveyapp.entity.Survey;
import com.bilgeadam.basurveyapp.services.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/get-all-survey-question-response-by-student")
    @Operation(
            summary = "Öğrenciye Göre Anketteki Soru Yanıtlarını Listele",
            description = "Belirli bir sınıftaki öğrencilerin bir anketteki her soru için verdiği yanıtları listeler. #91",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(
                            name = "dto",
                            description = "Öğrenci ve anket bilgilerini içeren istek gövdesi. surveyTitle-studentTagOId",
                            required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anketteki soru yanıtları başarıyla listelendi."
                    )
            }
    )
    ResponseEntity<List<SurveyQuestionResponseByStudentResponseDto>> getAllSurveyQuestionResponseByStudent(SurveyQuestionResponseByStudentRequestDto dto){
        return ResponseEntity.ok(surveyService.getAllSurveyQuestionResponseByStudent(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/list")
    @Operation(
            summary = "Tüm Anketleri Listele",
            description = "Aktif olan tüm anketleri listeler. #92",
            tags = {"Survey Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anketler başarıyla listelendi."
                    )
            }
    )
    ResponseEntity<List<SurveySimpleResponseDto>> getSurveyList() {
        return ResponseEntity.ok(surveyService.getSurveyList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/page")
    @Operation(
            summary = "Anket Sayfalarını Listele",
            description = "Gelen anket bilgilerine göre aktif olan anketi sayfalayarak gösterir. #93",
            tags = {"Survey Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anket sayfaları başarıyla listelendi."
                    )
            }
    )
    ResponseEntity<Page<Survey>> getSurveyPage(Pageable pageable) {
        return ResponseEntity.ok(surveyService.getSurveyPage(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-survey-by-id/{survey-id}")
    @Operation(
            summary = "Anket ID'sine Göre Anket Bul",
            description = "ID ile belirli bir anketi bulup görüntüleyen metot. #94",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(
                            name = "survey-id",
                            description = "Survey Kimliği (OID)",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anket başarıyla bulundu ve görüntülendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen ID ile anket bulunamadı."
                    )
            }
    )
    ResponseEntity<SurveyResponseDto> findByOid(@PathVariable("survey-id") Long surveyId) {
        return ResponseEntity.ok(surveyService.findByOid(surveyId));
    }


    @GetMapping("/find-survey-by-email-token/{token}")
    @Operation(
            summary = "Email Token ile Anket Bul",
            description = "E-posta token'ı girilerek belirli bir anketi bulup görüntüleyen metot. #95",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(
                            name = "token",
                            description = "Email token",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anket başarıyla bulundu ve görüntülendi."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Geçersiz token hatası."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen ID ile anket bulunamadı."
                    )
            }
    )
    ResponseEntity<SurveyResponseByEmailTokenDto> findByEmailToken(@PathVariable String token) {
        return ResponseEntity.ok(surveyService.findByEmailToken(token));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    @Operation(
            summary = "Yeni Anket Oluştur",
            description = "Belirtilen başlık ve konu ile yeni bir anket oluşturan metot. #96",
            tags = {"Survey Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Yeni anket bilgilerini içeren istek gövdesi. surveyTitle-courseTopic-surveyTagIds(List)",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anket başarıyla oluşturuldu."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Başlık zaten kullanımda hatası."
                    )
            }
    )
    ResponseEntity<SurveySimpleResponseDto> create(@RequestBody @Valid SurveyCreateRequestDto dto) {
        return ResponseEntity.ok(surveyService.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/assign-survey-tag")
    @Operation(
            summary = "Anket Etiketi Ata",
            description = "Belirtilen anket ve etiketler arasında ilişki kurarak etiketleri ankete atayan metot. #97 " +
                    "Not: Long olarak surveyTag0id ve survey0id girilir. Birden fazla tag için ',' ile ayrılmalıdır.",
            tags = {"Survey Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Anket etiketi atama işlemini tetikleyen istek gövdesi. surveyTagOid-surveyOid",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anket etiketleri başarıyla atandı."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Anket etiketi zaten mevcut hatası."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Anket veya etiket bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<SurveySimpleResponseDto> assignSurveyTag(@RequestBody @Valid SurveyTagAssignRequestDto dto) {
        return ResponseEntity.ok(surveyService.assignSurveyTag(dto));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/add-question-to-survey")
    @Operation(
            summary = "Ankete Soru Ekle",
            description = "Belirtilen anket ve soru arasında ilişki kurarak ankete yeni bir soru ekleyen metot. #98",
            tags = {"Survey Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Ankete eklenmek istenen yeni sorunun bilgilerini içeren istek gövdesi. questionId-surveyId",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Soru başarıyla ankete eklendi."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Soru zaten mevcut hatası."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Anket veya soru bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<Boolean> addQuestionToSurvey(@RequestBody SurveyAddQuestionRequestDto dto) {
        return ResponseEntity.ok(surveyService.addQuestionToSurvey(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/add-questions-to-survey")
    @Operation(
            summary = "Ankete Sorular Ekle",
            description = "Belirtilen anket ve soru ilişkilerini kullanarak ankete yeni sorular ekleyen metot. #99",
            tags = {"Survey Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Ankete eklemek için kullanılacak soru ve anket ilişkilerini içeren istek gövdesi. questionIds(List)-surveyId",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sorular başarıyla ankete eklendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Anket veya soru bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<Boolean> addQuestionsToSurvey( @RequestBody @Valid SurveyAddQuestionsRequestDto dto) {
        surveyService.addQuestionsToSurvey( dto);
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update")
    @Operation(
            summary = "Anket Güncelleme",
            description = "Belirtilen anket OID'si kullanılarak ankette değişiklik yapmayı sağlayan metot. #100",
            tags = {"Survey Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Güncellenecek anket bilgilerini içeren istek gövdesi. surveyOid-surveyTitle-courseTopic-surveyTagIds(List)",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anket başarıyla güncellendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Anket veya etiket bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<Survey> update(@RequestBody @Valid SurveyUpdateRequestDto dto) {
        return ResponseEntity.ok(surveyService.update(dto));
    }

    @Deprecated
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete/{survey-id}")
    @Operation(
            summary = "Anket Silme",
            description = "Belirtilen anket OID'si kullanılarak anketi silmeyi sağlayan metot. #101",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(
                            name = "survey-id",
                            description = "Survey Kimliği (OID)",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anket başarıyla silindi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Anket bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<Void> delete(@PathVariable("survey-id") Long surveyId) {
        surveyService.delete(surveyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(
            summary = "Anketi sınıfa Atama",
            description = "Belirli bir anketin sınıfa atamasını sağlayan metot. #102",
            tags = {"Survey Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Anketin sınıfa atanmasını sağlayan istek gövdesi. surveyId-studentTagId-days-startDate",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anket başarıyla sınıfa atandı."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Geçersiz tarih hatası veya sınıfta öğrenci bulunamadı hatası."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Anket veya öğrenci etiketi(sınıfı) bulunamadı hatası."
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Anket zaten sınıfa atanmış hatası."
                    )
            }
    )
    ResponseEntity<Boolean> assignSurveyToClassroom(@RequestBody SurveyAssignRequestDto surveyAssignRequestDto) throws MessagingException {
        return ResponseEntity.ok(surveyService.assignSurveyToClassroom(surveyAssignRequestDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-surveys-by-student-tag")
    @Operation(
            summary = "Öğrenci Etiketine(sınıfına) Göre Anketleri Bul",
            description = "Belirli bir öğrenci etiketine(sınıfına) sahip öğrencilerin katıldığı anketleri bulan metot. #103",
            tags = {"Survey Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anketler başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Öğrenci etiketi bulunamadı hatası."
                    )
            }
    )
    public ResponseEntity<List<SurveyByStudentTagResponseDto>> findSurveysByStudentTag(
            @RequestParam Long studentTagOid) {
        return ResponseEntity.ok(surveyService.findSurveysByStudentTag(studentTagOid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','STUDENT')")
    @GetMapping("/find-surveys-by-student-oid")
    @Operation(
            summary = "Öğrenci OID'ye Göre Anketleri Bul",
            description = "Belirli bir öğrenci OID'ye sahip öğrencilerin katıldığı anketleri bulan metot. #104",
            tags = {"Survey Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anketler başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Öğrenci bulunamadı hatası."
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Erişim reddedildi hatası."
                    )
            }
    )
    ResponseEntity<List<SurveyByStudentTagResponseDto>> findSurveysByStudentOid(@RequestParam Long studentOid) {
        return ResponseEntity.ok(surveyService.findSurveysByStudentOid(studentOid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-survey-answers-unmasked")
    @Operation(
            summary = "Anket Cevaplarını Maskesiz Bul",
            description = "Belirli bir anketin ve öğrenci tag'inin(sınıfının) cevaplarını maskesiz bulan metot. #105",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(name = "dto", description = "Anket ve öğrenci tag bilgilerini içeren istek gövdesi. surveyOid-studentTagOid", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cevaplar başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Anket veya öğrenci tag(sınıf) bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<SurveyResponseWithAnswersDto> findSurveyAnswersUnmasked(@ParameterObject FindSurveyAnswersRequestDto dto) {
        return ResponseEntity.ok(surveyService.findSurveyAnswersUnmasked(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-masked-survey-answers-as-admin-or-manager")
    @Operation(
            summary = "Maskelenen Anketin Cevaplarını Göster (Admin ve Manager için)",
            description = "Anketin ve öğrenci tag'inin(sınıfının) maskelenerek admin veya manager'a cevaplarını bulup gösteren metot. #106",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(
                            name = "findSurveyAnswersRequestDto",
                            description = "Anket(surveyOid) ve sınıf(studentTagOid) Kimlikleri",
                            required = true,
                            example = "{\n  \"surveyOid\": 1,\n  \"studentTagOid\": 2\n}"
                    ),
                    @Parameter(
                            name = "surveyOid",
                            description = "Anket Kimliği (surveyOid)",
                            required = true
                    ),
                    @Parameter(
                            name = "studentTagOid",
                            description = "Sınıf Kimliği (studentTagOid)",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cevaplar başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Anket veya öğrenci tag(sınıf) bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<SurveyOfClassroomMaskedResponseDto> findMaskedSurveyAnswersAsAdminOrManager(@ParameterObject FindSurveyAnswersRequestDto findSurveyAnswersRequestDto) {
        return ResponseEntity.ok(surveyService.findMaskedSurveyAnswersAsAdminOrManager(findSurveyAnswersRequestDto));
    }

    @PreAuthorize("hasAnyRole('MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/find-masked-survey-answers-as-trainer")
    @Operation(
            summary = "Maskelenen Anketin Cevaplarını Göster (Trainer için)",
            description = "Maskelenen anketin cevaplarını eğitmen için getir. #107",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(
                            name = "findSurveyAnswersRequestDto",
                            description = "Anket ve öğrenci tag OID'leri",
                            required = true,
                            example = "{\n  \"surveyOid\": 1,\n  \"studentTagOid\": 2\n}"
                    ),
                    @Parameter(
                            name = "surveyOid",
                            description = "Anket Kimliği (surveyOid)",
                            required = true
                    ),
                    @Parameter(
                            name = "studentTagOid",
                            description = "Sınıf Kimliği (studentTagOid)",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cevaplar başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Anket, öğrenci tag(sınıf) veya eğitmen bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<SurveyOfClassroomMaskedResponseDto> findMaskedSurveyAnswersAsTrainer(@ParameterObject FindSurveyAnswersRequestDto findSurveyAnswersRequestDto) {
        return ResponseEntity.ok(surveyService.findMaskedSurveyAnswersAsTrainer(findSurveyAnswersRequestDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/find-trainer-surveys")
    @Operation(
            summary = "Eğitmen Anketleri",
            description = "Eğitmenin yönettiği sınıflardaki anketleri getirme metodu. #108",
            tags = {"Survey Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Eğitmenin yönettiği sınıflardaki anketler başarıyla getirildi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Eğitmen bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<TrainerClassroomSurveyResponseDto> findTrainerSurveys() {
        return ResponseEntity.ok(surveyService.findTrainerSurveys());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','MASTER_TRAINER', 'ASSISTANT_TRAINER')")
    @GetMapping("/find-survey-participants")
    @Operation(
            summary = "Anket Katılımcılarını göster",
            description = "Belirli bir ankete katılan öğrenci bilgilerini getirme metodu. #109",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(
                            name = "dto",
                            description = "Anket katılımcılarını getirmek için kullanılacak istek gövdesi. surveyOid-studentTagOid",
                            required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anket katılımcıları başarıyla getirildi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Öğrenci tag veya anket kaydı bulunamadı hatası."
                    )
            }
    )
    ResponseEntity<SurveyParticipantResponseDto> findSurveyParticipants(SurveyParticipantRequestDto dto) {
        return ResponseEntity.ok(surveyService.findSurveyParticipants(dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ASSISTANT_TRAINER', 'MASTER_TRAINER')")
    @GetMapping("/{surveyid}/questions")
    @Operation(
            summary = "Ankete Atanan Soruları göster",
            description = "Belirli bir ankete atanan soruları getirme metodu. #110",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(
                            name = "surveyid",
                            description = "Anket Kimliği (OID)",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ankete atanan sorular başarıyla getirildi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Anket bulunamadı hatası."
                    )
            }
    )
    public ResponseEntity<List<SurveyQuestionsResponseDto>> findSurveyQuestions(@PathVariable Long surveyid){
        return ResponseEntity.ok(surveyService.findSurveyQuestions(surveyid));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ASSISTANT_TRAINER', 'MASTER_TRAINER')")
    @DeleteMapping("/{surveyid}/questions")
    @Operation(
            summary = "Ankete Atanan Soruları Kaldırma",
            description = "Belirli bir ankete atanan soruları kaldırma metodu. #111",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(
                            name = "surveyid",
                            description = "Anket Kimliği (OID)",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ankete atanan sorular başarıyla kaldırıldı."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Anket bulunamadı veya sorular kaldırılamadı hatası."
                    )
            }
    )
    public ResponseEntity<Boolean> removeSurveyQuestions(@PathVariable Long surveyid,@RequestBody RemoveSurveyQuestionRequestDto dto){
        return ResponseEntity.ok(surveyService.removeSurveyQuestions(surveyid,dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ASSISTANT_TRAINER', 'MASTER_TRAINER')")
    @GetMapping("find-active-survey-by-question-id")
    @Operation(
            summary = "Soruya Atanan Aktif Anket ID'lerini Bulma",
            description = "Belirli bir soruya atanmış aktif anket ID'lerini bulma metodu. #112",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(
                            name = "questionId",
                            description = "Sorunun ID'si (questionId)",
                            required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Aktif anket ID'leri başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Soruya atanmış aktif anket bulunamadı hatası."
                    )
            }
    )
    public ResponseEntity<List<Long>> findActiveSurveyIdsByQuestionId(@RequestParam Long questionId) {
        return ResponseEntity.ok(surveyService.findActiveSurveyIdsByQuestionId(questionId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ASSISTANT_TRAINER', 'MASTER_TRAINER')")
    @GetMapping("is-any-survey-registrated")
    @Operation(
            summary = "Herhangi Bir Anketin herhangi bir Sınıfa atanıp atanmadığını Kontrol Etme",
            description = "Herhangi bir anketin herhangi bir sınıfa atanıp atanmadığını kontrol eden metodu. #113",
            tags = {"Survey Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Herhangi bir ankete kayıt yapılmış."
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "Herhangi bir ankete kayıt yapılmamış."
                    )
            }
    )
    public ResponseEntity<Boolean> isAnySurveyRegistrated(@RequestParam List<Long> surveyIds) {
        return ResponseEntity.ok(surveyService.isAnySurveyRegistrated(surveyIds));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/full-delete/{id}")
    @Operation(
            summary = "Anketin Tamamen Silinmesi",
            description = "Belirtilen anketin ve ilgili soruların tamamen silinmesini sağlayan metot. #114",
            tags = {"Survey Controller"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Anket Kimliği (Survey ID)",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anket ve sorular başarıyla silindi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen ID'ye sahip anket bulunamadı."
                    )
            }
    )
    ResponseEntity<Void> fullDelete(@PathVariable Long id) {
        surveyService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/add-required-question-indexes")
    @Operation(
            summary = "Zorunlu Soruların Id'sini Ayarla",
            description = "Belirtilen anketin zorunlu soru id'lerini ayarlayan metot. (yazara göre index = id) #115",
            tags = {"Survey Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Zorunlu soru ID'lerini içeren istek gövdesi. oid-requiredIndexes(List)",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Zorunlu soru id'leri başarıyla güncellendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen ID'ye sahip aktif anket bulunamadı."
                    )
            }
    )
    public ResponseEntity<Boolean> setRequiredQuestionIndexes(@RequestBody SetRequiredQuestionIndexesDto dto){
        return ResponseEntity.ok(surveyService.setRequiredQuestionIndexes(dto));
    }
}

