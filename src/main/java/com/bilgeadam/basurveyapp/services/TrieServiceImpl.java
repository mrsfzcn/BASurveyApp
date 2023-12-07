package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Category;
import com.bilgeadam.basurveyapp.entity.TrieNode;
import com.bilgeadam.basurveyapp.repositories.ICategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TrieServiceImpl implements TrieService{
    private final ICategoryRepository categoryRepository;

    private TrieNode root = new TrieNode();

    @Override
    public void insertCategory(String[] categories) {
        TrieNode currentNode = root;
        StringBuilder fullPath = new StringBuilder();

        for (String category : categories) {
            currentNode = currentNode.getChildren().computeIfAbsent(category, k -> new TrieNode());
            fullPath.append('/').append(category);

            if (!categoryRepository.existsByName(fullPath.toString())) {
                Category dbCategory = new Category();
                dbCategory.setName(fullPath.toString());
                categoryRepository.save(dbCategory);
            }
        }

        currentNode.setEndOfCategory(true);
    }

    @Override
    public boolean searchCategory(String[] categories) {
        TrieNode currentNode = root;

        for (String category : categories) {
            currentNode = currentNode.getChildren().get(category);

            if (currentNode == null) {
                return false;
            }
        }

        return currentNode.isEndOfCategory();
    }

    @Override
    public void deleteCategory(String[] categories) {
        deleteCategoryHelper(root, categories, 0);
    }

    private boolean deleteCategoryHelper(TrieNode currentNode, String[] categories, int level) {
        if (currentNode == null) {
            return false;
        }

        if (level == categories.length) {
            if (!currentNode.isEndOfCategory()) {
                return false;
            }

            String fullPath = String.join("/", categories);
            categoryRepository.deleteByName(fullPath);
            currentNode.setEndOfCategory(false);

            return currentNode.getChildren().isEmpty();
        }

        boolean shouldDeleteCurrentNode = deleteCategoryHelper(
                currentNode.getChildren().get(categories[level]), categories, level + 1
        );

        if (shouldDeleteCurrentNode) {
            currentNode.getChildren().remove(categories[level]);
            return currentNode.getChildren().isEmpty();
        }

        return false;
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        traverseTrie(root, "", categories);
        return categories;
    }

    private void traverseTrie(TrieNode currentNode, String currentPath, List<Category> categories) {
        if (currentNode.isEndOfCategory()) {
            Category category = categoryRepository.findByName(currentPath);
            categories.add(category);
        }

        for (String key : currentNode.getChildren().keySet()) {
            traverseTrie(currentNode.getChildren().get(key), currentPath + "/" + key, categories);
        }
    }
}
