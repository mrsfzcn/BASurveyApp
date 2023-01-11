package com.bilgeadam.basurveyapp.repository;

import com.bilgeadam.basurveyapp.entity.ResponseNumeric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResponseNumericRepository extends JpaRepository<ResponseNumeric, Long> {
//    @Query("SELECT r FROM ResponseNumeric r WHERE r.user.oid = ?1 AND r.questionNumeric.oid = ?2")
//    List<ResponseNumeric> findAllResponseNumericOfStudentInClass(Long userOid, Long questionNumericOid);
}
