package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.repository.IClassroomRepository;
import com.bilgeadam.basurveyapp.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class ClassroomService extends ServiceManager<Classroom,Long> {
    private final IClassroomRepository classroomRepository;

    public ClassroomService(IClassroomRepository classroomRepository){
        super(classroomRepository);
        this.classroomRepository=classroomRepository;
    }
}
