package com.ideas2it.cms.repository;

import com.ideas2it.cms.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface GradeRepo extends JpaRepository<Grade, String> {

    @Modifying
    @Transactional
    @Query("UPDATE Grade g SET g.numberOfStudents = g.numberOfStudents + :delta, g.vacancy = g.vacancy - :delta WHERE g.gradeId = :gradeId")
    void updateNumberOfStudentsAndVacancy(@Param("gradeId") String gradeId, @Param("delta") int delta);
    List<Grade> findByStandard(String standard);
    int findNumberOfStudentsByGradeId(String gradeAllocated);
}
