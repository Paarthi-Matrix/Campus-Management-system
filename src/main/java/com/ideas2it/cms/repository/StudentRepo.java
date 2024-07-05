package com.ideas2it.cms.repository;

import com.ideas2it.cms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {
    Student findByRollNumber(String rollNumber);
    void deleteByRollNumber(String rollNumber);
    @Modifying
    @Transactional
    @Query("FROM Student student WHERE student.grade.gradeId = :gradeId")
    List<Student> findByGradeId(@Param("gradeId")  String gradeId);
}
