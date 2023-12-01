package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Category;

import java.util.List;

public interface TrieService {
    void insertCategory(String[] categories);
    boolean searchCategory(String[] categories);
    void deleteCategory(String[] categories);
    List<Category> getCategories();
}
