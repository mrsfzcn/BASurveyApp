package com.bilgeadam.basurveyapp.entity;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    private Map<String, TrieNode> children = new HashMap<>();
    private boolean isEndOfCategory;
    public Map<String, TrieNode> getChildren() {
        return children;
    }
    public void setChildren(Map<String, TrieNode> children) {
        this.children = children;
    }

    public boolean isEndOfCategory() {
        return isEndOfCategory;
    }

    public void setEndOfCategory(boolean endOfCategory) {
        isEndOfCategory = endOfCategory;
    }
}
