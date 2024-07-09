package com.ideas2it.cms.helper;

import com.ideas2it.cms.dto.*;
import com.ideas2it.cms.model.Grade;
import com.ideas2it.cms.model.Student;
import com.ideas2it.cms.model.UniformMeasurement;
import com.ideas2it.cms.util.DateUtil;

import java.util.List;

public class EntityDtoConverter {

    public static UniformMeasurement toUniformMeasurement(StudentRequestDto studentRequestDto, String rollNumber) {
        UniformMeasurement uniformMeasurement = new UniformMeasurement();
        uniformMeasurement.setRollNumber(rollNumber);
        uniformMeasurement.setPantSize(studentRequestDto.getUniformMeasurement().getPantSize());
        uniformMeasurement.setShirtSize(studentRequestDto.getUniformMeasurement().getShirtSize());
        uniformMeasurement.setShoeSize(studentRequestDto.getUniformMeasurement().getShoeSize());
        return uniformMeasurement;
    }

    public static StudentResponceDto toStudentResponceDto(Student student, Grade grade, String rollNumber) {
        StudentResponceDto studentResponceDto = new StudentResponceDto();
        studentResponceDto.setStudentName(student.getStudentName());
        studentResponceDto.setStandard(student.getGrade().getStandard());
        studentResponceDto.setSection(student.getGrade().getSection());
        studentResponceDto.setRollNumber(rollNumber);
        studentResponceDto.setAge(DateUtil.calculateDifferenceOfTwoDates(student.getDateOfBirth(), null, "Years"));
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

    public static FetchStudentDto toFetchStudentDto(Student student) {
        FetchStudentDto fetchStudentDto = new FetchStudentDto();
        fetchStudentDto.setStudentName(student.getStudentName());
        fetchStudentDto.setRollNumber(student.getRollNumber());
        fetchStudentDto.setDateOfBirth(student.getDateOfBirth());
        fetchStudentDto.setBloodGroup(student.getBloodGroup());
        fetchStudentDto.setStandard(student.getGrade().getStandard());
        fetchStudentDto.setSection(student.getGrade().getSection());
        fetchStudentDto.setAge(DateUtil.calculateDifferenceOfTwoDates(student.getDateOfBirth(), null, "Years"));
        return fetchStudentDto;
    }

    public static UpdateResponceDto toUpdateResponceDto(Student student) {
        UpdateResponceDto updateResponceDto = new UpdateResponceDto();
        updateResponceDto.setStudentName(student.getStudentName());
        updateResponceDto.setBloodGroup(student.getBloodGroup());
        updateResponceDto.setDateOfBirth(student.getDateOfBirth());
        updateResponceDto.setRollNumber(student.getRollNumber());
        return updateResponceDto;
    }
    public static StudentResponceDto toStudentResponceDto(Student student, Grade grade, String rollNumber, List<SpecialClassesEnum> specialClassesWithoutVacancy) {
        StudentResponceDto studentResponceDto = new StudentResponceDto();
        studentResponceDto.setStudentName(student.getStudentName());
        studentResponceDto.setStandard(student.getGrade().getStandard());
        studentResponceDto.setSection(student.getGrade().getSection());
        studentResponceDto.setRollNumber(rollNumber);
        studentResponceDto.setAge(DateUtil.calculateDifferenceOfTwoDates(student.getDateOfBirth(), null, "Year"));
        studentResponceDto.setSpecialClassesWithoutVacancy(specialClassesWithoutVacancy);
        return studentResponceDto;
    }
}
