package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.entity.Category;
import com.bilgeadam.basurveyapp.services.TrieService;
import com.bilgeadam.basurveyapp.services.TrieServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kategori")
@RequiredArgsConstructor
public class TrieController {
    private final TrieServiceImpl trieService;
    @PostMapping("/insert")
    @Operation(
            summary = "Kategori Ekleme",
            description = "Belirtilen kategoriyi trie veri yapısına ve veritabanına ekleyen metot. #130",
            tags = {"Trie Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Eklenecek kategori veya kategorilerin listesini içeren istek gövdesi.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "array", example = "[\"Kategori1\", \"Kategori2\"]")
                    )
            )
    )
    public void insertCategory(@RequestBody String[] categories) {
        trieService.insertCategory(categories);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Kategori Arama",
            description = "Belirtilen kategorinin trie veri yapısında olup olmadığını kontrol eden metot. #131",
            tags = {"Trie Controller"},
            parameters = {
                    @Parameter(
                            name = "categories",
                            description = "Aranacak kategori dizisi",
                            in = ParameterIn.QUERY,
                            style = ParameterStyle.FORM,
                            array = @ArraySchema(schema = @Schema(type = "string"))
                    )
            }
    )
    public boolean searchCategory(@RequestParam String[] categories) {
        return trieService.searchCategory(categories);
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Kategori Silme",
            description = "Belirtilen kategoriyi trie veri yapısından silen metot.  #132",
            tags = {"Trie Controller"},
            parameters = {
                    @Parameter(
                            name = "categories",
                            description = "Silinecek kategori dizisi",
                            in = ParameterIn.QUERY,
                            style = ParameterStyle.FORM,
                            array = @ArraySchema(schema = @Schema(type = "string")))
            }
    )
    public void deleteCategory(@RequestParam String[] categories) {
        trieService.deleteCategory(categories);
    }

    @GetMapping("/list")
    @Operation(
            summary = "Kategorileri Listeleme",
            description = "Trie veri yapısındaki tüm kategorileri listeler.  #133",
            tags = {"Trie Controller"}
    )
    public List<Category> getCategories() {
        return trieService.getCategories();
    }
}
