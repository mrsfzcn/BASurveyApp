package com.bilgeadam.basurveyapp.repositories;

import com.bilgeadam.basurveyapp.entity.Classroom;
import com.bilgeadam.basurveyapp.repositories.irepository.IClassroomRepository;
import com.bilgeadam.basurveyapp.repositories.utilities.RepositoryExtension;
import org.springframework.stereotype.Component;

@Component
public class ClassroomRepositoryImpl extends RepositoryExtension<Classroom, Long> {
    private final IClassroomRepository classroomRepository;

    public ClassroomRepositoryImpl(IClassroomRepository classroomRepository) {
        super(classroomRepository);
        this.classroomRepository = classroomRepository;
    }

}
