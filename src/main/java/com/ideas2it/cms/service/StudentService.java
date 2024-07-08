package com.ideas2it.cms.service;

import com.ideas2it.cms.dto.*;

import java.util.List;

public interface StudentService {
    StudentResponceDto addStudent(StudentRequestDto studentRequestDto);
    DeleteStudentResponceDto deleteStudentByRollNumber(String rollNumber);
    List<FetchStudentByGradeDto> getStudentByGrade(String standard);

}
