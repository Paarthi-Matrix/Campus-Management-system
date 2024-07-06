package com.ideas2it.cms.controller;

import java.util.List;
import java.util.Scanner;

import com.ideas2it.cms.dto.*;
import com.ideas2it.cms.helper.DateValidationResult;
import com.ideas2it.cms.service.StudentServiceImpl;
import com.ideas2it.cms.util.BloodgroupUtil;
import com.ideas2it.cms.util.ConversionUtil;
import com.ideas2it.cms.util.DateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * StudentController handles the CRUD operations for the Student entity.
 * It manages the endpoints to add, delete, and fetch student details.
 * The controller ensures data validation for student details such as
 * date of birth and blood group before processing the requests.
 * It uses StudentServiceImpl to perform the business logic.
 * </p>
 *
 * <p>
 * The RESTful endpoints in this controller provide functionalities to:
 * <ul>
 *     <li>Add a new student.</li>
 *     <li>Delete an existing student by roll number.</li>
 *     <li>Fetch students by grade.</li>
 *     <li>Fetch all students with pagination support.</li>
 * </ul>
 *</p>
 * This controller follows Spring's RESTful API conventions and leverages
 * ResponseEntity to provide appropriate HTTP responses and status codes.
 */
@RestController
@RequestMapping("/cms/api/v1/students")
public class StudentController {

    private static final Logger logger = LogManager.getLogger(StudentController.class);

    @Autowired
    private StudentServiceImpl studentServiceImpl;
    private static Scanner scanner = new Scanner(System.in);

    /**
     * <p>
     * Add a new student to the database.
     * The student details are gathered by means of StudentRequestDto.
     * </p>
     * <code>
     * Note:
     * <ul>
     *     <li>The date must be a valid date of birth in the format of dd/MM/yyyy.
     *       - Date of birth must not be in the future, age must not be too old (>18) must not be too young(<3).</li>
     *     <li>The blood group must be a valid blood group.</li>
     *     <li>The preferred grade must be in the range of 1 to 12 only.</li>
     *     <li>If the above validations fail, the application will throw a status code of 400 BAD_REQUEST.</li>
     *     <li>If the preferred grade contains no vacancy, the application will throw a status code of 409 CONFLICT.</li>
     *     <li>If the operation is successful, it will return a status code of 201 CREATED.</li>
     * </ul>
     * </code>
     * @param `StudentRequestDto`
     *         Refer StudentRequestDto for further information.
     * @see StudentRequestDto
     * @see BloodgroupUtil
     * @see DateUtil
     * @see `StudentServiceImpl.validInputRequest()`
     * @return ResponseEntity
     *         The headers and response code differs according the certain constraints.
     *
     */
    @PostMapping
    private ResponseEntity<String> addStudent(@RequestBody StudentRequestDto studentRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        logger.info("The application entered the insertion phase");
        ResponseEntity<String> responseEntity = validInputRequest(studentRequestDto);
        if(null != responseEntity) {
            return responseEntity;
        }
        StudentResponceDto studentResponceDto = studentServiceImpl.addStudent(studentRequestDto);
        if (null == studentResponceDto) {
            headers.add("X-Conflict-Reason", "No vacancy in preferred grade");
            return ResponseEntity.status(HttpStatus.CONFLICT).headers(headers).body("No vacancy is available in the preferred grade");
        }
        headers.add("Student-Rollnumber", studentResponceDto.getRollNumber());
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body("Student added successfully.");
    }

    /**
     * <p>
     * Deletes a student from the database based on roll number.
     * </p>
     * <p>
     * Note:
     *  <ul>
     *      <li>If the student is not found, it logs a warning message and returns a 409 CONFLICT status.</li>
     *      <li>If the student is deleted successfully, it logs an info message and returns a 200 OK status.</li>
     *  </ul>
     * </p>
     *
     * @param rollNumber The student roll number for deleting the student.
     * @return ResponseEntity<String> The HTTP response entity containing status and headers.
     */
    @DeleteMapping("/{rollNumber}")
    public ResponseEntity<String> deleteStudent(@PathVariable String rollNumber) {
        HttpHeaders headers = new HttpHeaders();
        logger.debug("The application entered the deleting the record phase");
        DeleteStudentResponceDto deleteStudentResponceDto  = studentServiceImpl.deleteStudentByRollNumber(rollNumber);

        if (null == deleteStudentResponceDto) {
            logger.warn("No such student with roll number {} is found in the database!", rollNumber);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No such student with roll number " + rollNumber +
                    " is found in the database!");
        } else {
            logger.info("Student " + deleteStudentResponceDto.getStudentName() +
                    " with roll number " + rollNumber +
                    " is deleted successfully!");
            headers.add("Deleted student name", deleteStudentResponceDto.getStudentName());
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body("Student deleted successfully.");
        }

    }

    /**
     * <p>
     * Fetches students by the specified grade ID.
     *
     * Note:
     * <ul>
     *     <li>This method fetches the students associated with the given grade.</li>
     *     <li>Returns a 200 OK status with the list of students.</li>
     * </ul>
     * </p>
     * @param gradeId The ID of the grade to fetch students for.
     * @return ResponseEntity<List<FetchStudentByGradeDto>> The HTTP response entity containing the list of students and status.
     */
    @GetMapping("/{gradeId}")
    private ResponseEntity<List<FetchStudentByGradeDto>> getStudentByGrade(@PathVariable String gradeId) {
        logger.debug("The application entered the process of fetching of getting the grade by student.");
        List<FetchStudentByGradeDto> fetchStudentByGradeDtos = studentServiceImpl.getStudentByGrade(gradeId);
        return new ResponseEntity<>(fetchStudentByGradeDtos, HttpStatus.OK);
    }

    /**
     * <p>
     * Fetches all students with pagination support.
     *
     * Note:
     * <ul>
     *     <li>This method returns a paginated list of students.</li>
     *     <li>Default pagination values are page = 0 and size = 10.</li>
     *     <li>Returns a 200 OK status with the page of students.</li>
     * </ul>
     * </p>
     * @param page The page number to fetch.
     * @param size The number of records per page.
     * @return ResponseEntity<Page<FetchAllStudentDto>> The HTTP response entity containing the paginated list of students and status.
     */
    @GetMapping
    private ResponseEntity<Page<FetchAllStudentDto>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<FetchAllStudentDto> fetchAllStudentDtos = studentServiceImpl.getAllStudents(page, size);
        return new ResponseEntity<>(fetchAllStudentDtos, HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<Page<FetchStudentDto>> getStudentByRollNumber(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<FetchStudentDto> fetchStudentDtos = studentServiceImpl.getStudentByRollNumber(page, size);
        return new ResponseEntity<>(fetchStudentDtos, HttpStatus.OK);
    }
    /**
     * <p>
     * Validates the input request for adding a student.
     * </p>
     * <p>
     * Note:
     * <ul>
     *     <li>Checks for valid blood group using BloodgroupUtil.</li>
     *     <li>Validates date of birth using DateUtil.</li>
     *     <li>Ensures the preferred grade is within the valid range.</li>
     *     <li>Returns appropriate ResponseEntity for invalid inputs or null if the input is valid.</li>
     * </ul>
     * </p>
     * @param studentRequestDto The student request data transfer object containing student details.
     * @return ResponseEntity<String> The HTTP response entity containing validation errors or null if valid.
     */
    private ResponseEntity<String> validInputRequest(StudentRequestDto studentRequestDto) {
        if (null == BloodgroupUtil.validateBloodGroup(studentRequestDto.getBloodGroup())) {
            logger.warn("User entered invalid blood group {}", studentRequestDto.getBloodGroup()
                    + " Must be a valid available blood group ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( studentRequestDto.getBloodGroup()
                    + " is not a valid blood group");
        }
        DateValidationResult validationResult = DateUtil.checkValidDateAndAge(studentRequestDto.getDateOfBirth(), "dd/MM/yyyy");
        switch (validationResult) {
            case VALID_DATE:
                break;
            case INVALID_DATE:
                logger.warn("User entered invalid date format: {}", studentRequestDto.getDateOfBirth());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(studentRequestDto.getDateOfBirth() +
                        " is not a valid date format ");
            case FUTURE_DATE:
                logger.warn("User entered a future date: {}", studentRequestDto.getDateOfBirth());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(studentRequestDto.getDateOfBirth() +
                        " is a future date and not valid ");
            case OVER_18:
                logger.warn("{} User's age is is not under constrains. Must be below 18! ", studentRequestDto.getDateOfBirth());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(studentRequestDto.getDateOfBirth() +
                        " User's age is is not under constrains. Must be below 18! ");
            case UNDER_3:
                logger.warn("{} User's age is is not under constrains. Must be at least 3 years old! ", studentRequestDto.getDateOfBirth());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(studentRequestDto.getDateOfBirth() +
                        " User's age is is not under constrains. Must be at least 3 years old! ");
        }

        if (studentRequestDto.getGradePreferred().isEmpty()) {
            logger.warn("The preferred grade is null ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(studentRequestDto.getDateOfBirth() +
                    " The preferred grade is null ");
        }
        if (ConversionUtil.stringToInt(studentRequestDto.getGradePreferred()) < 1
                || ConversionUtil.stringToInt(studentRequestDto.getGradePreferred()) > 12) {
            logger.warn("The preferred grade must be in range of 1 to 12 only ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(studentRequestDto.getGradePreferred() +
                    " The preferred grade must be in range of 1 to 12 only ");
        }
        return  null;
    }


}