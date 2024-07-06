package com.ideas2it.cms.helper;

import com.ideas2it.cms.dto.*;
import com.ideas2it.cms.model.Grade;
import com.ideas2it.cms.model.Student;
import com.ideas2it.cms.model.UniformMeasurement;
import com.ideas2it.cms.util.DateUtil;

public class EntityDtoConverter {

    public static UniformMeasurement toUniformMeasurement(StudentRequestDto studentRequestDto, String rollNumber) {
        UniformMeasurement uniformMeasurement = new UniformMeasurement();
        uniformMeasurement.setRollNumber(rollNumber);
        uniformMeasurement.setPantSize(studentRequestDto.getUniformRequestDto().getPantSize());
        uniformMeasurement.setShirtSize(studentRequestDto.getUniformRequestDto().getShirtSize());
        uniformMeasurement.setShoeSize(studentRequestDto.getUniformRequestDto().getShoeSize());
        return uniformMeasurement;
    }

    public static StudentResponceDto toStudentResponceDto(Student student, Grade grade, String rollNumber) {
        StudentResponceDto studentResponceDto = new StudentResponceDto();
        studentResponceDto.setStudentName(student.getStudentName());
        studentResponceDto.setStandard(student.getGrade().getStandard());
        studentResponceDto.setSection(student.getGrade().getSection());
        studentResponceDto.setBloodGroup(student.getBloodGroup());
        studentResponceDto.setDateOfBirth(student.getDateOfBirth());
        studentResponceDto.setRollNumber(rollNumber);
        studentResponceDto.setAge(DateUtil.calculateDifferenceOfTwoDates(student.getDateOfBirth(), null, "Year"));
        studentResponceDto.setGrade(grade);
        return studentResponceDto;
    }

    public static DeleteStudentResponceDto toDeleteStudentResponceDto(Student student) {
        DeleteStudentResponceDto deleteStudentResponceDto = new DeleteStudentResponceDto();
        deleteStudentResponceDto.setStudentName(student.getStudentName());
        return deleteStudentResponceDto;
    }

    public static FetchStudentByGradeDto toFetchStudentByGradeDto(Student student) {
        FetchStudentByGradeDto fetchStudentByGradeDto = new FetchStudentByGradeDto();
        fetchStudentByGradeDto.setStudentName(student.getStudentName());
        fetchStudentByGradeDto.setRollNumber(student.getRollNumber());
        fetchStudentByGradeDto.setBloodGroup(student.getBloodGroup());
        fetchStudentByGradeDto.setDateOfBirth(student.getDateOfBirth());
        fetchStudentByGradeDto.setAge(DateUtil.calculateDifferenceOfTwoDates(student.getDateOfBirth(), null, "years"));
        return fetchStudentByGradeDto;
    }

    public static FetchAllStudentDto toFetchAllStudentDto(Student student) {
        FetchAllStudentDto fetchAllStudentDto = new FetchAllStudentDto();
        fetchAllStudentDto.setStudentName(student.getStudentName());
        fetchAllStudentDto.setRollNumber(student.getRollNumber());
        fetchAllStudentDto.setBloodGroup(student.getBloodGroup());
        fetchAllStudentDto.setDateOfBirth(student.getDateOfBirth());
        fetchAllStudentDto.setAge(DateUtil.calculateDifferenceOfTwoDates(student.getDateOfBirth(), null, "years"));
        return fetchAllStudentDto;
    }
}
