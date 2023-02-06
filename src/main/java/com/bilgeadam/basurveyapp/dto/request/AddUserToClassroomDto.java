package com.bilgeadam.basurveyapp.dto.request;

public class AddUserToClassroomDto {

    private Long classroomOid;
    private Long userOid;

    public Long getClassroomOid() {
        return classroomOid;
    }

    public Long getUserOid() {
        return userOid;
    }

}
