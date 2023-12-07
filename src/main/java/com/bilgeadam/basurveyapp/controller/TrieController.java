package com.bilgeadam.basurveyapp.controller;

import com.bilgeadam.basurveyapp.entity.Category;
import com.bilgeadam.basurveyapp.services.TrieService;
import com.bilgeadam.basurveyapp.services.TrieServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kategori")
@RequiredArgsConstructor
public class TrieController {
    private final TrieServiceImpl trieService;
    @PostMapping("/insert")
    public void insertCategory(@RequestBody String[] categories) {
        trieService.insertCategory(categories);
    }

    @GetMapping("/search")
    public boolean searchCategory(@RequestParam String[] categories) {
        return trieService.searchCategory(categories);
    }

    @DeleteMapping("/delete")
    public void deleteCategory(@RequestParam String[] categories) {
        trieService.deleteCategory(categories);
    }

    @GetMapping("/list")
    public List<Category> getCategories() {
        return trieService.getCategories();
    }
}
