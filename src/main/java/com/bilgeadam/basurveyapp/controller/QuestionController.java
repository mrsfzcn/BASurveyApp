package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.*;
import com.bilgeadam.basurveyapp.dto.response.FindAllQuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionFindByIdResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionsTrainerTypeResponseDto;
import com.bilgeadam.basurveyapp.services.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("")
    @Operation(
            summary = "Yeni Soru Oluşturma",
            description = "String türünde bir soru, long türünde soru tipi ve integer türünde sıralama girilerek yeni soru oluşturan metot. #43",
            tags = {"Question Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Oluşturulacak soruların bilgilerini içeren istek gövdesi. questionString-questionTypeOid-order-tagOids(List)-options(List)",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Yeni soru başarıyla oluşturuldu."
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Aynı soru metniyle zaten var olan bir soru bulunmaktadır."
                    )
            }
    )
    public ResponseEntity<Boolean> createQuestion(@RequestBody @Valid List<CreateQuestionDto> createQuestionDtoList) {
        return ResponseEntity.ok(questionService.createQuestion(createQuestionDtoList));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update-question")
    @Operation(
            summary = "Soru Güncelleme",
            description = "Oid ile mevcut bir soruyu bulup string türünde yeni bir sorunun aynı id ile yazılmasını sağlayan metot. #44",
            tags = {"Question Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Güncellenecek soru bilgilerini içeren istek gövdesi. questionOid-questionTypeOid-tagOids(List)-questionString",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Soru başarıyla güncellendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Güncellenecek soru bulunamadı."
                    )
            }
    )
    public ResponseEntity<Boolean> updateQuestion(@RequestBody @Valid UpdateQuestionDto updateQuestionDto) {
        return ResponseEntity.ok(questionService.updateQuestion(updateQuestionDto));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-id/{id}")
    @Operation(
            summary = "Soru Bulma",
            description = "Integer türünde id girilerek o id'ye denk gelen sorunun görüntülenmesini sağlayan metot. #45",
            tags = {"Question Controller"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Soru ID'si",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Soru başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen ID'ye sahip soru bulunamadı."
                    )
            }
    )
    public ResponseEntity<QuestionFindByIdResponseDto> findById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(questionService.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-all")
    @Operation(
            summary = "Tüm Soruları Getir",
            description = "Tüm soruların görüntülenmesini sağlayan metot. #46",
            tags = {"Question Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tüm sorular başarıyla getirildi."
                    )
            }
    )
    public ResponseEntity<List<QuestionResponseDto>> findAll() {
        List<QuestionResponseDto> responseDtoList = questionService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-all-question")
    @Operation(
            summary = "Tüm Soruları Getir",
            description = "Tüm soruların görüntülenmesini sağlayan metot. #47",
            tags = {"Question Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tüm sorular başarıyla getirildi."
                    )
            }
    )
    public ResponseEntity<List<FindAllQuestionResponseDto>> findAllQuestion() {
        List<FindAllQuestionResponseDto> responseDtoList = questionService.findAllQuestion();
        return ResponseEntity.ok(responseDtoList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete-question-by-id/{id}")
    @Operation(
            summary = "Soru Silme",
            description = "Integer türünde ID girilerek bulunan sorunun silinmesini sağlayan metot. #48",
            tags = {"Question Controller"},
            parameters = {
                    @Parameter(name = "id", description = "Silinecek sorunun kimliği.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Soru başarıyla silindi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Soru bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Cevaplanmış sorular silinemez."
                    ),
                    @ApiResponse(
                            responseCode = "423",
                            description = "Soruya ait aktif anketler bulunmaktadır."
                    )
            }
    )
    public ResponseEntity<Boolean> deleteQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.delete(id));
    }

    @GetMapping("/survey-questions/{student-token}")
    @Operation(
            summary = "Anket Sorularını Getirme",
            description = "Token kullanılarak anket sorularına ulaşılmasını sağlayan metot. #50",
            tags = {"Question Controller"},
            parameters = {
                    @Parameter(name = "student-token", description = "Öğrenciye ait token.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Anket soruları başarıyla getirildi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen anket bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Geçersiz token."
                    )
            }
    )
    public ResponseEntity<List<QuestionResponseDto>> getSurveyQuestions(@PathVariable(name = "student-token") String token) {
        return ResponseEntity.ok(questionService.findAllSurveyQuestions(token));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{survey-oid}/keyword")
    @Operation(
            summary = "Anket Sorularını Anahtar Kelime İle Filtreleme",
            description = "Id ile survey'e ulaşıp string tütünde anahtar kelimenin içinde geçtiği soruları görüntülemeyi sağlayan metot. #51",
            tags = {"Question Controller"},
            parameters = {
                    @Parameter(name = "survey-oid", description = "Filtrelenen soruların ait olduğu anketin ID'si.", required = true),
                    @Parameter(name = "keyword", description = "Soruları filtrelemek için kullanılacak anahtar kelime.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sorular başarıyla keyword'e göre filtrelenmiştir."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen anket veya filtre sonucunda herhangi bir soru bulunamadı."
                    )
            }
    )
    public ResponseEntity<List<QuestionResponseDto>> filterSurveyQuestionsByKeyword(@PathVariable("survey-oid") Long survey0id,@RequestParam String keyword) {
        return ResponseEntity.ok(questionService.filterSurveyQuestionsByKeyword(survey0id,keyword));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/survey-oid/question-tag")
    @Operation(
            summary = "Anket Sorularını Etiketlere Göre Filtreleme",
            description = "Id ile survey bulup, survey'deki tüm soruları belirtilen etiketlere göre filtrelemeyi sağlayan metot. #52",
            tags = {"Question Controller"},
            parameters = {
                    @Parameter(name = "survey0id", description = "Filtrelenecek anketin ID'si.", required = true),
                    @Parameter(name = "questionTag0ids", description = "Filtreleme için kullanılacak etiketlerin ID'leri.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sorular başarıyla etiketlere göre filtrelenmiştir."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen anket veya filtre sonucunda herhangi bir soru bulunamadı."
                    )
            }
    )
    public ResponseEntity<List<QuestionResponseDto>> filterSurveyQuestions(@RequestParam Long survey0id, @RequestParam List<Long> questionTag0ids) {
        return ResponseEntity.ok(questionService.filterSurveyQuestions(survey0id,questionTag0ids));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ASSISTANT_TRAINER', 'MASTER_TRAINER')")
    @GetMapping("/trainer-id/survey-id")
    @Operation(
            summary = "Eğitmen ve Anket Bazında Soruları Getir",
            description = "Id ile eğitmen ve anketi bulup, belirtilen eğitmen tipine göre soruları görüntülemeyi sağlayan metot. #53",
            tags = {"Question Controller"},
            parameters = {
                    @Parameter(name = "trainerid", description = "Eğitmenin kimlik numarası.", required = true),
                    @Parameter(name = "surveyid", description = "Anketin kimlik numarası.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sorular başarıyla getirilmiştir."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen eğitmen veya anket bulunamadı."
                    )
            }
    )
    ResponseEntity<List<QuestionsTrainerTypeResponseDto>> QuestionsByTrainerType(@RequestParam Long trainerid,@RequestParam Long surveyid) {
        return ResponseEntity.ok(questionService.questionByTrainerType(trainerid,surveyid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/type")
    @Operation(
            summary = "Belirli Türdeki Soruları Getir",
            description = "Belirli bir soru türüne sahip tüm soruları görüntülemeyi sağlayan metot. #54",
            tags = {"Question Controller"},
            parameters = {
                    @Parameter(name = "type", description = "Görüntülenecek soru türünü belirten parametre.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sorular başarıyla getirilmiştir."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen soru türüne sahip soru bulunamadı."
                    )
            }
    )
    public ResponseEntity<List<String>> findAllByQuestionType(@RequestParam String type){
        return ResponseEntity.ok(questionService.findAllByQuestionType(type));
    }

}
