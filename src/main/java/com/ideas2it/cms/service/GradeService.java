package com.ideas2it.cms.service;

import com.ideas2it.cms.model.Grade;

import java.util.List;

public interface GradeService {
    int getNumberOfStudents(String gradeId);
    void updateNoOfStudentsAndVacancyAvailablity(String gradeIdAllocated, boolean action);
}
