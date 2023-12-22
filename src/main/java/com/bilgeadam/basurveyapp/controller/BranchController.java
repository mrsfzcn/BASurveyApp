package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.dto.request.CreateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.request.FindByNameAndCityRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UpdateBranchRequestDto;
import com.bilgeadam.basurveyapp.dto.response.BranchModelResponseDto;
import com.bilgeadam.basurveyapp.dto.response.MessageResponseDto;
import com.bilgeadam.basurveyapp.entity.Branch;
import com.bilgeadam.basurveyapp.services.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/branch")
public class BranchController {

    private final BranchService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    @Operation(
            summary = "Şube Oluşturma",
            description = "Yeni bir şube oluşturan metot. Verilen API kimliği veya şube adı ve şehir kombinasyonu ile mevcut bir şubenin durumuna bakar.\n\n" +
                    "İşlem Adımları:\n" +
                    "1. Eğer şube mevcutsa ve aktifse, bir hata fırlatılır.\n" +
                    "2. Eğer şube mevcutsa ancak silinmişse, kullanıcıya silinmiş olduğu bilgisini içeren bir mesaj döner ve şube aktif etme metodunu kullanmaya yönlendirilir.\n" +
                    "3. Yeni bir şube oluşturulduğunda başarı mesajı döner. #6\n",
            tags = {"Branch Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Yeni şube bilgilerini içeren istek gövdesi. apiId-name-city",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Şube başarıyla oluşturuldu."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Eklemeye çalıştığınız şube zaten mevcut ve aktif durumda."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Eklemeye çalıştığınız şube mevcut fakat silinmiş. Lütfen şube aktif et metodunu kullanınız."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<MessageResponseDto> create(@RequestBody @Valid CreateBranchRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/get-data-from-base-api")
    @Operation(
            summary = "(Deprecated) Base API'den Veri Alma",
            description = "Base API'den tüm şube verilerini çeken ve veritabanıyla karşılaştıran metot.\n\n" +
                    "1. Silinmiş şubeleri tespit eder ve veritabanından kaldırır.\n" +
                    "2. Yeni eklenmiş şubeleri tespit eder ve veritabanına ekler. #7\n",
            tags = {"Branch Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Base API'den veri başarıyla alındı ve güncellendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Base API'den alınan veri bulunamadı veya şube ile ilgili herhangi bir data bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<List<BranchModelResponseDto>> getDataFromBaseApi() {
        return ResponseEntity.ok(service.getAllDataFromBaseApi());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/soft-delete-by/{oid}")
    @Operation(
            summary = "Şubeyi Yumuşak Silme",
            description = "OID parametresine göre bulunan şubeyi yumuşak bir şekilde silen metot.\n\n" +
                    "1. Şube bulunamazsa hata fırlatılır.\n" +
                    "2. Yumuşak silme işlemi başarıyla gerçekleşirse true döner, aksi takdirde false döner. #8\n",
            tags = {"Branch Controller"},
            parameters = {
                    @Parameter(name = "oid", description = "Silinecek şubenin OID değeri", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Şube başarıyla yumuşak bir şekilde silindi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen OID'ye sahip şube bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<Boolean> deleteBranchByOid(@PathVariable Long oid) {
        return ResponseEntity.ok(service.deleteBranchByOid(oid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-name/{name}")
    @Operation(
            summary = "İsimle Şube Bulma",
            description = "Verilen isme göre aktif şubeleri bulan metot. Eğer isme uygun herhangi bir şube bulunamazsa hata fırlatılır. #9",
            tags = {"Branch Controller"},
            parameters = {
                    @Parameter(name = "name", description = "Bulunmak istenen şubenin adı", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "İsme göre şubeler başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Herhangi bir şube bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<List<Branch>> findBranchesByName(@PathVariable String name) {
        return ResponseEntity.ok(service.findBranchesByName(name));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-name-and-city")
    @Operation(
            summary = "İsim ve Şehirle Şube Bulma",
            description = "Verilen isim ve şehre göre aktif şubeyi bulan metot. Eğer belirtilen isim ve şehirde şube bulunamazsa hata fırlatılır. #10",
            tags = {"Branch Controller"},
            parameters = {
            @Parameter(name = "dto", description = "İsim ve şehir bilgilerini içeren istek gövdesi. name-city", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "İsim ve şehire göre şube başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Herhangi bir şube bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<Branch> findByNameAndCity(@RequestBody @Valid FindByNameAndCityRequestDto dto) {
        return ResponseEntity.ok(service.findByNameAndCity(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-city/{city}")
    @Operation(
            summary = "Şehirle Şube Bulma",
            description = "Verilen şehre göre aktif şubeleri bulan metot. Eğer belirtilen şehirde şube bulunamazsa hata fırlatılır. #11",
            tags = {"Branch Controller"},
            parameters = {
                    @Parameter(name = "city", description = "Şehir adı", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Şehire göre şubeler başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Herhangi bir şube bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<List<Branch>> findByCity(@PathVariable String city) {
        return ResponseEntity.ok(service.findByCity(city));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/find-by-apiId/{apiId}")
    @Operation(
            summary = "API Kimliğiyle Şube Bulma",
            description = "Verilen API kimliğine göre aktif bir şubeyi bulan metot. Eğer belirtilen API kimliğine sahip şube bulunamazsa hata fırlatılır. #12",
            tags = {"Branch Controller"},
            parameters = {
                    @Parameter(name = "apiId", description = "Api Kimliğini içeren istek.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "API kimliğine göre şube başarıyla bulundu."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Herhangi bir şube bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<Branch> findByApiId(@PathVariable String apiId) {
        return ResponseEntity.ok(service.findByApiId(apiId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update")
    @Operation(
            summary = "Şube Güncelleme",
            description = "Verilen API kimliğine sahip aktif bir şubeyi güncelleyen metot. Eğer belirtilen API kimliğine sahip şube bulunamazsa veya güncelleme işlemi başarısız olursa hata fırlatılır. #13",
            tags = {"Branch Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Güncellenecek şube bilgilerini içeren istek gövdesi. apiId-name-city",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Şube güncelleme işlemi başarıyla gerçekleştirildi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Aradığınız şube bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Belirtilen isim ve şehirde zaten bir şube mevcut."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Bilinmeyen bir hata oluştu."
                    )
            }
    )
    public ResponseEntity<MessageResponseDto> updateBranchByApiId(@RequestBody @Valid UpdateBranchRequestDto dto) {
        return ResponseEntity.ok(service.updateBranchByApiId(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/active-branches")
    @Operation(
            summary = "Aktif Şubeleri Getir",
            description = "Sistemdeki tüm aktif şubeleri getiren metot. Eğer aktif şube bulunamazsa hata fırlatılır. #14",
            tags = {"Branch Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Aktif şubeler başarıyla getirildi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Herhangi bir aktif şube bulunamadı."
                    )
            }
    )
    public ResponseEntity<List<Branch>> activeBranches() {
        return ResponseEntity.ok(service.findAllActiveBranches());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/deleted-branches")
    @Operation(
            summary = "Silinmiş Şubeleri Getir",
            description = "Sistemdeki tüm silinmiş şubeleri getiren metot. Eğer silinmiş şube bulunamazsa hata fırlatılır. #15",
            tags = {"Branch Controller"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Silinmiş şubeler başarıyla getirildi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Herhangi bir silinmiş şube bulunamadı."
                    )
            }
    )
    public ResponseEntity<List<Branch>> deletedBranches() {
        return ResponseEntity.ok(service.findAllDeletedBranches());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/activate-branch/{oid}")
    @Operation(
            summary = "Şube Aktivasyonu",
            description = "Belirtilen şubenin aktifleştirilmesini sağlayan metot. Şube zaten aktifse hata fırlatılır. #16",
            tags = {"Branch Controller"},
            parameters = {
                    @Parameter(name = "oid", description = "Aktifleştirilecek şubenin OID değeri", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Şube başarıyla aktifleştirildi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen OID'ye sahip şube bulunamadı."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Belirtilen şube zaten aktif durumda."
                    )
            }
    )
    public ResponseEntity<MessageResponseDto> activateBranch(@PathVariable Long oid) {
        return ResponseEntity.ok(service.activateBranch(oid));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/refresh/{apiId}")
    @Operation(
            summary = "Şube Yenileme",
            description = "Belirtilen şubenin verilerini günceller. #17",
            tags = {"Branch Controller"},
            parameters = {
                    @Parameter(name = "apiId", description = "Yenilenecek şubenin apiId değeri", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Şube başarıyla yenilendi."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Belirtilen OID'ye sahip şube bulunamadı."
                    )
            }
    )
    public ResponseEntity<Branch> updateBranchByApiId(@PathVariable String apiId) {
        return ResponseEntity.ok(service.refreshSingleBranch(apiId));
    }
}
