package com.ideas2it.cms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ideas2it.cms.customexception.StudentDatabaseException;
import com.ideas2it.cms.dto.*;
import com.ideas2it.cms.service.StudentServiceImpl;
import com.ideas2it.cms.util.BloodgroupUtil;
import com.ideas2it.cms.util.DateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * StudentController handles the CRUD operations for the Student entity.
 * It manages the endpoints to add, delete, and fetch student details.
 * The controller ensures data validation for student details such as
 * date of birth and blood group before processing the requests.
 * It uses StudentServiceImpl to perform the business logic.
 */

@RestController
@RequestMapping("/student")
public class StudentController {

    private static final Logger logger = LogManager.getLogger(StudentController.class);

    @Autowired
    private StudentServiceImpl studentServiceImpl;
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Add a new student to the database.
     * The student details are gathered by means of StudentRequestDto.
     * NOTE: The date must be a valid date of birth in format of dd/MM/yyyy.
     *       The blood group must be a valid blood group.
     *       If this is violated the application will throw a status code of 400 BAD_REQUEST.
     *       Refer BloodGroup and Date util classes.
     * If the preferred grade contains no vacancy in it,
     * the application will throw status code of 409 CONFLICT.
     * If every operation is successful it will give status code 201 CREATED.
     *
     * @param `StudentRequestDto`
     *         Refer StudentRequestDto for further information.
     * @see StudentRequestDto
     * @see BloodgroupUtil
     * @see DateUtil
     * @return ResponseEntity
     *         The headers and response code differs according the certain constraints.
     *
     */
    @PostMapping("/add-student")
    public ResponseEntity<String> addStudent(@RequestBody StudentRequestDto studentRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        logger.info("The application entered the insertion phase");
        //Check for valid date
        if (!DateUtil.checkValidDate(studentRequestDto.getDateOfBirth(), "dd/MM/yyyy")) {
            logger.warn("User entered invalid date {}", studentRequestDto.getDateOfBirth());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format");
        }
        //check for valid blood group
        if (null == BloodgroupUtil.validateBloodGroup(studentRequestDto.getBloodGroup())) {
            logger.warn("User entered invalid blood group {}", studentRequestDto.getBloodGroup()
                        + "Must be a valid available blood group ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format");
        }

        StudentResponceDto studentResponceDto = studentServiceImpl.addStudent(studentRequestDto);
        if (null == studentResponceDto) {
            headers.add("X-Conflict-Reason", "No vacancy in preferred grade");
            return ResponseEntity.status(HttpStatus.CONFLICT).headers(headers).body("No vacancy is available in the preferred grade");
        }
        logger.info(studentResponceDto.getStudentName() + " is added to the student database and assigned to " +
                studentResponceDto.getStandard() + "th standard " + studentResponceDto.getSection() + " section..!" +
                "Now Associating the student to the respective special class!");
        int[] specialClassPreference = studentRequestDto.getSpecialClassRequestDto().getSpecialClassPreference().stream().mapToInt(i -> i).toArray();;
        try {
            logger.info("Association of student {} to the preferred special classes", studentRequestDto.getStudentName());
            studentServiceImpl.associateStudentToSpecialClass(studentResponceDto, specialClassPreference);
        } catch (StudentDatabaseException e) {
            logger.error(e.getMessage());
        }
        logger.info("Student {} successfully associated to special class!", studentResponceDto.getStudentName());
        try {
            studentServiceImpl.addUniformMeasurementToStudent(studentRequestDto, studentResponceDto);
        } catch (StudentDatabaseException e) {
            logger.error(e.getMessage());
        }
        logger.info("Student {} uniform details entered successfully!", studentRequestDto.getStudentName());

        headers.add("Student-Rollnumber", studentResponceDto.getRollNumber());
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body("Student added successfully.");
    }

    /**
     * Deletes a student from the database based on roll number.
     * The student details are gathered by means of DeleteStudentRequestDto.
     * If the student is not found, it logs a warning message.
     * If the student is deleted successfully, it logs an info message.
     *
     * @param deleteStudentRequestDto The student details for deleting the student.
     */
    @DeleteMapping("/delete-student")
    public ResponseEntity<String> deleteStudent(@RequestBody DeleteStudentRequestDto deleteStudentRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        logger.debug("The application entered the deleting the record phase");
        DeleteStudentResponceDto deleteStudentResponceDto  = studentServiceImpl.deleteStudentByRollNumber(deleteStudentRequestDto.getRollNumber());

        if (null == deleteStudentResponceDto) {
            logger.warn("No such student with roll number {} is found in the database!", deleteStudentRequestDto.getRollNumber());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No such student with roll number " + deleteStudentRequestDto.getRollNumber() +
                    " is found in the database!");
        } else {
            logger.info("Student " + deleteStudentResponceDto.getStudentName() +
                    " with roll number " + deleteStudentRequestDto.getRollNumber() +
                    " is deleted successfully!");
            headers.add("Deleted student name", deleteStudentResponceDto.getStudentName());
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body("Student deleted successfully.");
        }

    }

    @GetMapping("/getStudentByGrade/{gradeId}")
    public ResponseEntity<List<FetchStudentByGradeDto>> getStudentByGrade(@PathVariable String gradeId) {
        logger.debug("The application entered the process of fetching of getting the grade by student.");
        List<FetchStudentByGradeDto> fetchStudentByGradeDtos = studentServiceImpl.getStudentByGrade(gradeId);
        return new ResponseEntity<>(fetchStudentByGradeDtos, HttpStatus.OK);
    }

    @GetMapping("/fetchAllStudents")
    public ResponseEntity<List<FetchAllStudentDto>> getAllStudents() {
        HttpHeaders headers = new HttpHeaders();
        List<FetchAllStudentDto> fetchAllStudentDtos = studentServiceImpl.getAllStudents();
        return new ResponseEntity<>(fetchAllStudentDtos, HttpStatus.OK);
    }
}