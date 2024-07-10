package com.ideas2it.cms.controller;

import java.util.List;

import com.ideas2it.cms.customexception.*;
import com.ideas2it.cms.dto.*;
import com.ideas2it.cms.helper.EntityDtoConverter;
import com.ideas2it.cms.service.StudentServiceImpl;
import com.ideas2it.cms.util.BloodgroupUtil;
import com.ideas2it.cms.util.DateUtil;
import com.ideas2it.cms.helper.RequestValidator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * AdminController handles the CRUD operations for the Student entity.
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
@RequestMapping("/v1/admin")
public class AdminController {

    private static Logger logger = LogManager.getLogger(AdminController.class);
    @Autowired
    private StudentServiceImpl studentServiceImpl;

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
    @PostMapping("/students")
    private ApiResponseDto<StudentResponceDto> addStudent(@RequestBody StudentRequestDto studentRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        StudentResponceDto studentResponceDto;
        logger.debug("The application entered the insertion phase");

        try{
            RequestValidator.insertStudentRequestValidator(studentRequestDto);
            studentResponceDto = studentServiceImpl.addStudent(studentRequestDto);
        } catch (PreCondtionValidationException e) {
            studentResponceDto = EntityDtoConverter.toStudentResponceDto(studentRequestDto);
            return ApiResponseDto.statusBadRequest(studentResponceDto, e);
        } catch (GradeNotFoundException e) {
            studentResponceDto = EntityDtoConverter.toStudentResponceDto(studentRequestDto);
            return ApiResponseDto.statusNoContent(studentResponceDto, e);
        } catch (SpecialClassNotfoundException e) {
            StudentResponceDto studentResponseDtoAsParam = e.getStudentResponceDto();
            return ApiResponseDto.statusNoContent(studentResponseDtoAsParam, e);
        }

       // headers.add("Student-Rollnumber", studentResponceDto.getRollNumber());
        return ApiResponseDto.statusCreated(studentResponceDto);
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
    @DeleteMapping("/students/{rollNumber}")
    public ApiResponseDto<DeleteStudentResponceDto> deleteStudent(@PathVariable String rollNumber) {
        HttpHeaders headers = new HttpHeaders();
        logger.debug("The application entered the deleting the record phase");
        DeleteStudentResponceDto deleteStudentResponceDto;
        try {
            deleteStudentResponceDto = studentServiceImpl.deleteStudentByRollNumber(rollNumber);
        } catch (StudentNotFoundException e) {
            deleteStudentResponceDto = EntityDtoConverter.toDeleteStudentResponceDto(rollNumber);
            return ApiResponseDto.statusNoContent(deleteStudentResponceDto, e);
        }

        logger.info("Student " + deleteStudentResponceDto.getStudentName() +
                    " with roll number " + rollNumber +
                    " is deleted successfully!");
        //headers.add("Deleted student name", deleteStudentResponceDto.getStudentName());
        return ApiResponseDto.statusOk(deleteStudentResponceDto);
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
    @GetMapping("/students/{gradeId}/grades")
    private ApiResponseDto<List<FetchStudentByGradeDto>> getStudentByGrade(@PathVariable String gradeId) {
        logger.debug("The application entered the process of fetching of getting the grade by student.");
        List<FetchStudentByGradeDto> fetchStudentByGradeDtos = null;
        try {
            fetchStudentByGradeDtos = studentServiceImpl.getStudentByGrade(gradeId);
        } catch (GradeNotFoundException e) {
            return  ApiResponseDto.statusNoContent(fetchStudentByGradeDtos, e);
        }
        return ApiResponseDto.statusOk(fetchStudentByGradeDtos);
    }

    /**
     * <p>
     * Fetches all students with pagination support.
     * </p>
     * <p>
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
    @GetMapping("/students")
    private ApiResponseDto<Page<FetchAllStudentDto>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<FetchAllStudentDto> fetchAllStudentDtos = studentServiceImpl.getAllStudents(page, size);
        return ApiResponseDto.statusOk(fetchAllStudentDtos);
    }

    /**
     * <p>
     * Fetches a student by their roll number.
     * </p>
     * <p>
     * Note:
     * <ul>
     *     <li>This method fetches the student associated with the given roll number.</li>
     *     <li>Returns a 200 OK status with the student details.</li>
     * </ul>
     * </p>
     * @param rollNumber
     *        Roll number of the student to be fetched.
     * @return ResponseEntity<FetchStudentDto>
     *         The HTTP response entity containing the student details and status.
     */
    @GetMapping("/students/{rollNumber}")
    private ApiResponseDto<FetchStudentDto> getStudentByRollNumber(@PathVariable String rollNumber) {
        FetchStudentDto fetchStudentDto = null;
        try{
            fetchStudentDto = studentServiceImpl.getStudentByRollNumber(rollNumber);
        } catch (StudentNotFoundException e) {
            return ApiResponseDto.statusNoContent(fetchStudentDto, e);
        }
        return ApiResponseDto.statusOk(fetchStudentDto);
    }


    /**
     * <p>
     *     Updates the student details
     * </p>
     * <p>
     * Note:
     * <ul>
     *   <li>This method updates the student details</li>
     *   <li>Returns a 200 OK status with the student details.</li>
     * </ul>
     * </p>
     * @param updateRequestDto
     * @param rollNumber
     *        Roll number of the student.
     * @return ResponseEntity<?>
     */
    @PutMapping("/students/{rollNumber}")
    private ApiResponseDto<UpdateResponceDto> updateStudent(@RequestBody UpdateRequestDto updateRequestDto, @PathVariable String rollNumber) {
        HttpHeaders headers = new HttpHeaders();
        logger.debug("The application entered the update phase");
        UpdateResponceDto updateResponceDto = null;
        try {
            RequestValidator.insertStudentRequestValidator(updateRequestDto);
            updateResponceDto = studentServiceImpl.updateStudent(updateRequestDto, rollNumber);
        }  catch (PreCondtionValidationException e) {
            return ApiResponseDto.statusBadRequest(updateResponceDto , e);
        } catch (EntityNotFoundException e) {
            logger.info(e.getMessage());
            return ApiResponseDto.statusNoContent(updateResponceDto, e);
        }
        headers.add("Student-rollNumber", rollNumber);
        updateResponceDto.setStatus("Student updated successfully!");
        return ApiResponseDto.statusCreated(updateResponceDto);
    }
}