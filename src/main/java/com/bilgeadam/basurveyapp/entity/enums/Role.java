package com.bilgeadam.basurveyapp.entity.enums;

public enum Role {

    ADMIN("admin"),
    MANAGER("manager"),
    MASTER_TRAINER("master_trainer"),
    ASSISTANT_TRAINER("assistant_trainer"),
    STUDENT("student");

    private String role;
    Role(String role) {
        this.role = role;
    }
}
