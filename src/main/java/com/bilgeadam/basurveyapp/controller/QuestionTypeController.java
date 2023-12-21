package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionTypeByTagStringRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateQuestionTypeRequestDto;
import com.bilgeadam.basurveyapp.dto.response.AllQuestionTypeResponseDto;
import com.bilgeadam.basurveyapp.dto.response.QuestionTypeFindByIdResponseDto;
import com.bilgeadam.basurveyapp.entity.QuestionType;
import com.bilgeadam.basurveyapp.entity.tags.QuestionTag;
import com.bilgeadam.basurveyapp.exceptions.custom.QuestionTagNotFoundException;
import com.bilgeadam.basurveyapp.services.QuestionTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question-types")
@RequiredArgsConstructor
public class QuestionTypeController {
    private final QuestionTypeService questionTypeService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update-question-type-by-type-string")
    @Operation(
            summary = "Question Type Güncelleme",
            description = "Belirtilen tip stringine sahip olan soru tipini güncelleyen metot. #59",
            tags = {"Question Type Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Güncellenecek soru tipi bilgileri. tagString-newTagString",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Soru tipi başarıyla güncellendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen tip stringine sahip soru tipi bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Beklenmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity <QuestionType> updateQuestionTypeByTagString(@RequestBody UpdateQuestionTypeByTagStringRequestDto dto){

        try {
            QuestionType questionType = questionTypeService.updateQuestionTypeByTypeString(dto.getNewTagString(), dto.getTagString());
            System.out.println(questionType);
            return ResponseEntity.ok(questionType);
        } catch (QuestionTagNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create-question-type")
    @Operation(
            summary = "Soru Tipi Oluşturma",
            description = "String türünde soru tipi ismi girilerek yeni bir soru tipi oluşturan metot. #60",
            tags = {"Question Type Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Oluşturulacak soru tipi bilgileri. questionType",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Soru tipi başarıyla oluşturuldu."
                    )
            }
    )
    public ResponseEntity<Boolean> createQuestionType(@RequestBody @Valid CreateQuestionTypeRequestDto dto) {
        return ResponseEntity.ok(questionTypeService.createQuestionType(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update-question-type/{id}")
    @Operation(
            summary = "Soru Tipi Güncelleme",
            description = "Id ile belirtilen soru tipine ulaşılıp yeni soru tipi isminin güncellenmesini sağlayan metot. #61",
            tags = {"Question Type Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Güncellenecek soru tipi bilgileri. questionType",
                    required = true
            ),
            parameters = {
            @Parameter(
                    name = "id",
                    description = "Güncellenecek sorunun ID'si",
                    required = true
            )
        },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Soru tipi başarıyla güncellendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen Id'ye sahip soru tipi bulunamadı."
                    )
            }
    )
    public ResponseEntity<Void> updateQuestionType(@RequestBody @Valid UpdateQuestionTypeRequestDto dto,@PathVariable Long id) {
        questionTypeService.updateQuestionType(dto,id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-id/{id}")
    @Operation(
            summary = "Soru Tipi Bulma",
            description = "Id ile belirtilen soru tipine ulaşıp görüntülenmesini sağlayan metot. #62",
            tags = {"Question Type Controller"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Bulunacak soru tipinin Id'si.",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Soru tipi başarıyla bulundu ve görüntülendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen Id'ye sahip soru tipi bulunamadı."
                    )
            }
    )
    public ResponseEntity<QuestionTypeFindByIdResponseDto> findById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(questionTypeService.findById(id));

    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/get-all-question-type")
    @Operation(
            summary = "Tüm Soru Tiplerini Görüntüleme",
            description = "Tüm question type'larını görüntülemeyi sağlayan metot. #63",
            tags = {"Question Type Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tüm soru tipleri başarıyla görüntülendi."
                    )
            }
    )
    public ResponseEntity<List<AllQuestionTypeResponseDto>> findAll() {
        List<AllQuestionTypeResponseDto> responseDtoList = questionTypeService.findAll();
        return ResponseEntity.ok(responseDtoList);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("delete/{id}")
    @Operation(
            summary = "Soru Tipi Silme",
            description = "ID numarası ile ulaşılan question type'ını silmeyi sağlayan metot. #64",
            tags = {"Question Type Controller"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Silinecek soru tipinin ID numarası",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Soru tipi başarıyla silindi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen ID numarasına sahip soru tipi bulunamadı."
                    )
            }
    )
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(questionTypeService.delete(id));
    }


}
