package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Category;
import com.bilgeadam.basurveyapp.entity.TrieNode;
import com.bilgeadam.basurveyapp.repositories.ICategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrieServiceImplTest {

    @InjectMocks
    private TrieServiceImpl trieService;

    @Mock
    private ICategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insertCategory() {
        String[] categories = {"category", "subCategory"};
        when(categoryRepository.existsByName("/category")).thenReturn(false);
        when(categoryRepository.existsByName("/category/subCategory")).thenReturn(false);
        trieService.insertCategory(categories);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void searchCategory() {
        String[] categories = {"category", "subCategory"};
        TrieNode rootNode = createTrieNode(categories);
        trieService.insertCategory(categories);
        boolean result = trieService.searchCategory(categories);
        assertTrue(result);
    }

    @Test
    void deleteCategory() {
        String[] categories = {"category", "subCategory"};
        TrieNode rootNode = createTrieNode(categories);
        trieService.insertCategory(categories);
        when(categoryRepository.existsByName("/category")).thenReturn(true);
        when(categoryRepository.existsByName("/category/subCategory")).thenReturn(true);
        trieService.deleteCategory(categories);
        verify(categoryRepository, times(1)).deleteByName("/category");
        verify(categoryRepository, times(1)).deleteByName("/category/subCategory");
    }

    @Test
    void getCategories() {
        String[] categories = {"category", "subCategory"};
        TrieNode rootNode = createTrieNode(categories);
        trieService.insertCategory(categories);
        List<Category> result = trieService.getCategories();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("/category", result.get(0).getName());
    }

    private TrieNode createTrieNode(String[] categories) {
        TrieNode rootNode = new TrieNode();
        TrieNode currentNode = rootNode;
        for (String category : categories) {
            currentNode = currentNode.getChildren().computeIfAbsent(category, k -> new TrieNode());
            currentNode.setEndOfCategory(true);
        }
        return rootNode;
    }
}
