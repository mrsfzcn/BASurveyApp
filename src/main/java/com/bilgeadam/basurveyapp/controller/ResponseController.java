package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.AnswerResponseDto;
import com.bilgeadam.basurveyapp.dto.response.WhoDidntAnswerSurveyStudentDto;
import com.bilgeadam.basurveyapp.entity.Response;
import com.bilgeadam.basurveyapp.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/responses")
@RequiredArgsConstructor
public class ResponseController {
    private final ResponseService responseService;
    @PostMapping
    @Operation(
            summary = "Yanıt Oluşturma",
            description = "Response oluşturmak için kullanılan metot. #65",
            tags = {"Response Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Oluşturulacak Response'un bilgilerini içeren DTO. responseString-questionOid-surveyOid-userOid"
            )
    )
    public ResponseEntity<Boolean> createResponse (@RequestBody @Valid ResponseRequestSaveDto responseRequestSaveDto){
        responseService.createResponse(responseRequestSaveDto);
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update-response/{id}")
    @Operation(
            summary = "Yanıt Güncelleme",
            description = "Id ile belirtilen bir Response'u güncellemek için kullanılan metot. #66",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Güncellenecek Response'un id'si",
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Güncellenecek Response'un yeni bilgilerini içeren DTO. responseString"
            )
    )
    public ResponseEntity<Void> updateResponse(@PathVariable Long id,@RequestBody @Valid ResponseRequestDto dto) {
        responseService.updateResponse(dto,id);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-id/{id}")
    @Operation(
            summary = "Yanıt Görüntüleme",
            description = "Id ile belirtilen bir Response'u görüntülemek için kullanılan metot. #67",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Görüntülenecek Response'un id'si",
                            required = true
                    )
            }
    )
    public ResponseEntity<AnswerResponseDto> findById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(responseService.findById(id));

    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    @Operation(
            summary = "Tüm Yanıtları Görüntüleme",
            description = "Sistemde bulunan tüm Response'ları görüntülemek için kullanılan metot. #68",
            tags = {"Response Controller"}
    )
    public ResponseEntity<List<AnswerResponseDto>> findAll() {
        return ResponseEntity.ok(responseService.findAll());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete-response-by-id/{id}")
    @Operation(
            summary = "Response Silme",
            description = "id ile belirlenen Response'un silinmesini sağlayan metot. #69",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Silinecek Response'un ID'si",
                            required = true
                    )
            }
    )
    public ResponseEntity<Boolean> deleteResponseById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(responseService.deleteResponseById(id));
    }

    @PutMapping("/survey-answers/{token}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(
            summary = "Tüm Response'ları Kaydet",
            description = "Belirli bir ankete ait tüm sorulara verilen Response'ları kaydetmeye yarayan metot. #70",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "token",
                            description = "Anket Token'ı",
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Kaydedilecek Response'ların listesi. responseString-questionOid-surveyOid-userOid",
                    required = true
            )
    )
    public ResponseEntity<Boolean> saveAll(@PathVariable @Valid String token,@RequestBody @Valid List<ResponseRequestSaveDto> responseRequestSaveDtoList){
        return ResponseEntity.ok(responseService.saveAll(token, responseRequestSaveDtoList));
    }


    @GetMapping("/find-all-responses-of-user-from-survey")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(
            summary = "Kullanıcının Belirli Bir Anketteki Tüm Cevaplarını Getir",
            description = "Belirli bir ankette kullanıcının verdiği tüm cevapları getiren metot. #71",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "dto",
                            description = "Cevapları getirmek için kullanılacak veri transfer nesnesi. userEmail-surveyOid",
                            required = true
                    )
            }
    )
    public ResponseEntity<List<AnswerResponseDto>> findAllResponsesOfUserFromSurvey(FindAllResponsesOfUserFromSurveyRequestDto dto) {
        return ResponseEntity.ok(responseService.findAllResponsesOfUserFromSurvey(dto));
    }

    @GetMapping("/find-all-responses-of-user")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(
            summary = "Kullanıcının Tüm Cevaplarını Getir",
            description = "Belirtilen kullanıcının tüm cevaplarını getiren metot. #72",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "userEmail",
                            description = "Cevapları getirmek için kullanılacak kullanıcının e-posta adresi",
                            required = true
                    )
            }
    )
    public ResponseEntity<List<AnswerResponseDto>> findAllResponsesOfUser(String userEmail) {
        return ResponseEntity.ok(responseService.findAllResponsesOfUser(userEmail));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'MASTER_TRAINER', 'ASISTANT_TRAINER')")
    @GetMapping("/find-response-by-student-tag")
    @Operation(
            summary = "Öğrenci Etiketine Göre Cevapları Getir",
            description = "Belirtilen öğrenci etiketine sahip tüm cevapları getiren metot. #73",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "studentTagOid",
                            description = "Cevapları getirmek için kullanılacak öğrenci etiketinin kimliği (studentTagOid)",
                            required = true
                    )
            }
    )
    public ResponseEntity<List<AnswerResponseDto>> findResponseByStudentTag(@RequestParam Long studentTagOid) {
        return ResponseEntity.ok(responseService.findResponseByStudentTag(studentTagOid));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STUDENT')")
    @PostMapping("/update-student-responses")
    @Operation(
            summary = "Öğrenci Cevaplarını Güncelle",
            description = "Belirli bir ankete ait öğrenci cevaplarını güncellemek için kullanılır. #74",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "surveyOid",
                            description = "Cevapları güncellenecek anketin kimliği (OID)",
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Güncellenecek cevapları içeren istek gövdesi. updateResponseMap(List)"
            )
    )
    public ResponseEntity<Boolean> updateStudentAnswers(@RequestParam Long surveyOid, @RequestBody SurveyUpdateResponseRequestDto dto) {
        return ResponseEntity.ok(responseService.updateStudentAnswers(surveyOid, dto));
    }
    //TODO ADMIN ROLÜ İLERDE KALDIRILACAK ADMİN GİRİŞİ YAPILDIĞINDA METHODUN ÇALIŞMASI İÇİN ADMIN AUTHORIZE'I EKLENDI !!

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @GetMapping("/excel/{id}")
    @Operation(
            summary = "Anket Cevaplarını Excele Aktar",
            description = "Belirli bir anketin cevaplarını Excel dosyasına aktarır. #75",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Cevapları alınacak anketin kimliği (OID)",
                            required = true
                    )
            }
    )
    public ResponseEntity<byte[]> exportToExcel(@PathVariable Long id) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment","result.xlsx");
        return new ResponseEntity<>(responseService.exportToExcel(id),headers, HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @GetMapping("/survey-response-rate/{survey-id}/{student-tag-oid}")
    @Operation(
            summary = "Anket Doldurma Oranını Hesapla",
            description = "Belirli bir anketin belirli bir öğrenci etiketi altındaki doldurma oranını hesaplar. #76",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "survey-id",
                            description = "Anketin kimliği (studentTagOid)",
                            required = true
                    ),
                    @Parameter(
                            name = "student-tag-oid",
                            description = "Öğrenci etiketinin kimliği (surveyOid)",
                            required = true
                    )
            }
    )
    public ResponseEntity<Double> surveyResponseRate(@PathVariable(name = "survey-id")Long surveyId, @PathVariable(name = "student-tag-oid") Long studentTagOid){
        return ResponseEntity.ok(responseService.surveyResponseRate(surveyId,studentTagOid));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @GetMapping("/survey-response-rates/{survey-id}/{student-tag-oid}")
    @Operation(
            summary = "Sınıftaki Öğrencilerin Ad Soyadlarını Listele",
            description = "Belirli bir anketin belirli bir öğrenci etiketi altındaki öğrencilerin ad soyadlarını listeler. #77",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "survey-id",
                            description = "Anketin kimliği (surveyOid)",
                            required = true
                    ),
                    @Parameter(
                            name = "student-tag-oid",
                            description = "Öğrenci etiketinin(sınıfının) kimliği (studentTagOid)",
                            required = true
                    )
            }
    )
    public ResponseEntity<List<String>> surveyResponseRateName(@PathVariable(name = "survey-id")Long surveyId, @PathVariable(name = "student-tag-oid") Long studentTagOid){
        return ResponseEntity.ok(responseService.surveyResponseRateName(surveyId,studentTagOid));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @GetMapping("/who-need-to-complete/{survey-id}")
    @Operation(
            summary = "Anketi Cevaplaması Gereken Öğrencileri Bul",
            description = "Belirli bir anketi henüz cevaplamamış öğrencilerin ad ve soyadlarını listeler. #78",
            tags = {"Response Controller"},
            parameters = {
                    @Parameter(
                            name = "survey-id",
                            description = "Anketin kimliği (surveyOid)",
                            required = true
                    )
            }
    )
    public ResponseEntity<List<WhoDidntAnswerSurveyStudentDto>> whoNeedToComplete(@PathVariable(name = "survey-id") Long surveyId){
        return ResponseEntity.ok(responseService.whoNeedToComplete(surveyId));
    }

}
