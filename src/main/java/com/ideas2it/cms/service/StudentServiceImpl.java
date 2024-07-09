package com.ideas2it.cms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ideas2it.cms.customexception.EntityNotFoundException;
import com.ideas2it.cms.customexception.StudentDatabaseException;
import com.ideas2it.cms.dto.*;
import com.ideas2it.cms.helper.EntityDtoConverter;
import com.ideas2it.cms.helper.SpecialClassesEnum;
import com.ideas2it.cms.model.Grade;
import com.ideas2it.cms.model.SpecialClass;
import com.ideas2it.cms.model.UniformMeasurement;
import com.ideas2it.cms.model.Student;

import com.ideas2it.cms.repository.SpecialclassRepo;
import com.ideas2it.cms.repository.StudentRepo;
import com.ideas2it.cms.util.ConversionUtil;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 *
 * <p>
 * Provides services to access the Student DAO.
 * All business logic related to students and their roll number generation are handled here.
 * </p>
 *
 */

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LogManager.getLogger(StudentServiceImpl.class);
    @Autowired
    private GradeServiceImpl gradeServiceImpl;
    @Autowired
    private SpecialClassServiceImpl specialClassServiceImpl;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    SpecialclassRepo specialclassRepo;

    /**
     * <p>
     * Adds a new student to the database.
     * </p>
     * <p>
     * This method handles the addition of a new student by performing the following steps:
     * <ul>
     *     <li>Checks for available vacancy in the preferred grade.</li>
     *     <li>Generates a unique roll number for the student.</li>
     *     <li>Associates the student with the preferred grade and special classes.</li>
     *     <li>Updates the vacancy status for the grade and special classes.</li>
     *     <li>Saves the student entity to the database.</li>
     * </ul>
     * </p>
     *
     * @param studentRequestDto
     *        The DTO containing the student's details for the request.
     * @return StudentResponceDto containing the details of the added student or information about any conflicts.
     *
     */
    @Transactional
    public StudentResponceDto addStudent(StudentRequestDto studentRequestDto) {
        int rollNumberSuffix;
        String rollNumber;
        Student student = null;

        Grade grade = gradeServiceImpl.getPreferedGrade(studentRequestDto.getGradePreferred());
        if (grade == null) {
            logger.info("No vacancy is available for the preferred grade {} " +
                            "Adding student {} to the database aborted",
                    studentRequestDto.getGradePreferred(), studentRequestDto.getStudentName());
            StudentResponceDto studentResponceDto = new StudentResponceDto();
            studentResponceDto.setIsGradeAvailable(false);
            return studentResponceDto;
        }
        rollNumberSuffix = grade.getNumberOfStudents();
        rollNumber = generateRollNumber(grade.getStandard() + grade.getSection(),
                      rollNumberSuffix);
        student = new Student(studentRequestDto.getStudentName(), rollNumber, studentRequestDto.getBloodGroup(), studentRequestDto.getDateOfBirth());
        student.setGrade(grade);

        gradeServiceImpl.updateNoOfStudentsAndVacancyAvailablity(grade.getGradeId(), true);
        logger.debug("Association of student {} to the preferred special classes", studentRequestDto.getStudentName());
        List<SpecialClassesEnum> specialClassPreferences = studentRequestDto.getSpecialClassEnums();
        System.out.println(specialClassPreferences.isEmpty());
        List<SpecialClass> specialClasses = specialclassRepo.findByClassType(specialClassPreferences);
        System.out.println("special classes...." + specialClasses.size());
        List<SpecialClassesEnum> specialClassesWithoutVacancy = checkAndGetSpecialClassWithoutVacancy(specialClasses);
        if(!specialClassesWithoutVacancy.isEmpty()) {
            EntityDtoConverter.toStudentResponceDto(student, grade, rollNumber, specialClassesWithoutVacancy);
            StudentResponceDto studentResponceDto = new StudentResponceDto();
            studentResponceDto.setSpecialClassesWithoutVacancy(specialClassesWithoutVacancy);
            return studentResponceDto;
        }
        student.setSpecialClasses(ConversionUtil.convertArrayListToSet(specialClasses));
        specialClassServiceImpl.UpdateVacancyOfSpecialClass(specialClassPreferences, true);

        logger.info("Association of student {} to the preferred special classes is successful", studentRequestDto.getStudentName());
        logger.debug("Now setting uniform measurement to the preferred special classes");
        UniformMeasurement uniformMeasurement =EntityDtoConverter.toUniformMeasurement(studentRequestDto, rollNumber);
        student.setUniformMeasurement(uniformMeasurement);
        studentRepo.save(student);
        logger.info("Student {} added to database successfully!", studentRequestDto.getStudentName());

        return EntityDtoConverter.toStudentResponceDto(student, grade, rollNumber);
    }

    /**
     * <p>
     *   Deletes a student by roll number.
     * </p>
     * <p>
     * This method performs the deletion of a student entity based on the provided roll number.
     * It performs the following steps:
     * <ul>
     *     <li>Finds the student by roll number.</li>
     *     <li>If the student exists, retrieves and converts associated special classes to a list of enums.</li>
     *     <li>Deletes the student by roll number.</li>
     *     <li>Updates the number of students and vacancy availability for the associated grade.</li>
     *     <li>Updates the vacancy status for the associated special classes.</li>
     *     <li>Returns a response DTO with the details of the deleted student.</li>
     * </ul>
     * </p>
     *
     * @param rollNumber The roll number of the student to be deleted.
     * @return A DeleteStudentResponceDto containing the details of the deleted student, or null if no student was found.
     */
    @Transactional
    public DeleteStudentResponceDto deleteStudentByRollNumber(String rollNumber) {
        Student student = studentRepo.findByRollNumber(rollNumber); // Find the student first
        if (student == null) {
            return null;
        }
        Set<SpecialClass> specialClasses = student.getSpecialClass();
        List<SpecialClassesEnum> associatedSpecialClassEnums = ConversionUtil.convertSetToList(specialClasses);
        studentRepo.deleteByRollNumber(rollNumber);
        gradeServiceImpl.updateNoOfStudentsAndVacancyAvailablity(student.getGrade().getGradeId(), false);
        specialClassServiceImpl.UpdateVacancyOfSpecialClass(associatedSpecialClassEnums, false);
        return EntityDtoConverter.toDeleteStudentResponceDto(student);
    }

    /**
     * <p>
     *    Retrieves a list of students by the specified grade.
     * </p>
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Finds students associated with the given grade ID.</li>
     *     <li>Converts each student entity to a FetchStudentByGradeDto object.</li>
     *     <li>Returns a list of FetchStudentByGradeDto objects representing the students in the specified grade.</li>
     * </ul>
     * </p>
     *
     * @param requestedGrade
     *         The ID of the grade for which to retrieve students.
     * @return List<FetchStudentByGradeDto>
     *         A list of FetchStudentByGradeDto objects representing students in the specified grade.
     */
    public List<FetchStudentByGradeDto> getStudentByGrade(String requestedGrade) {
        List<Student> students= studentRepo.findByGradeId(requestedGrade);
        if(ObjectUtils.isEmpty(students)) {
            return null;
        }
        List<FetchStudentByGradeDto> fetchStudentByGradeDtos = new ArrayList<>();
        for (Student student : students) {
            fetchStudentByGradeDtos.add(EntityDtoConverter.toFetchStudentByGradeDto(student));
        }
        return fetchStudentByGradeDtos;
    }

    /**
     * <p>
     * Retrieves a paginated list of all students.
     *</p>
     * <p>
     * This method retrieves a list of all students from the database, with pagination support.
     * It uses the given page number and size to create a Pageable object, fetches the students,
     * and maps the student entities to FetchAllStudentDto objects.
     * </p>
     *
     * @param page
     *        The page number to fetch.
     * @param size
     *        The number of records per page.
     * @return A Page of FetchAllStudentDto objects representing the paginated list of students.
     */
    @Transactional(readOnly = true)
    public Page<FetchAllStudentDto> getAllStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentRepo.findAll(pageable);
        return students.map(EntityDtoConverter::toFetchAllStudentDto);
    }

    /**
     * <p>
     * Retrieves a student by their roll number.
     *</p>
     * <p>
     * This method fetches a student from the database using the given roll number,
     * and converts the student entity to a FetchStudentDto object.
     * </p>
     *
     * @param rollNumber
     *        The roll number of the student to fetch.
     * @return A FetchStudentDto object representing the fetched student.
     */
    @Transactional(readOnly = true)
    public FetchStudentDto getStudentByRollNumber(String rollNumber) {
        Student student = studentRepo.findByRollNumber(rollNumber);
        if(ObjectUtils.isEmpty(student)) {
            FetchStudentDto fetchStudentDto = new FetchStudentDto();
            fetchStudentDto.setIsStudentAvailable(false);
            return fetchStudentDto;
        }
        return  EntityDtoConverter.toFetchStudentDto(student);
    }

    /**
     * <p>
     * Updates a student's details using the provided UpdateRequestDto and roll number.
     * </p>
     * <p>
     * This method fetches the student from the database using the given roll number,
     * updates the student's details with the information provided in the UpdateRequestDto,
     * saves the updated student entity, and converts the updated entity to an UpdateResponceDto object.
     * If the student with given roll number is not found, an EntityNotFoundException is thrown.
     * </p>
     *
     * @param updateRequestDto
     *        The DTO containing the updated student details.
     * @param rollNumber
     *        The roll number of the student to update.
     * @return An UpdateResponceDto object representing the updated student.
     * @throws EntityNotFoundException if the student with the given roll number is not found.
     */
    @Transactional
    public UpdateResponceDto updateStudent(UpdateRequestDto updateRequestDto, String rollNumber) {
        Student student = studentRepo.findByRollNumber(rollNumber);
        if(null == student) {
           String errorMessage = "Conflict in the data while updating." +
                   " No Student with roll number" + rollNumber +
                   "is found in the database." +
                   " Check the roll number of the student!";
           throw new EntityNotFoundException(errorMessage);
        }
        student.setStudentName(updateRequestDto.getStudentName());
        student.setDateOfBirth(updateRequestDto.getDateOfBirth());
        student.setBloodGroup(updateRequestDto.getBloodGroup());

        studentRepo.save(student);
        return EntityDtoConverter.toUpdateResponceDto(student);
    }
    /**
     * <p>
     * Generates a roll number for the student based on the grade ID and roll number suffix.
     * If the student belongs to 5th grade and "A" section and the student is the 4th student to get added in that grade and section-
     * then his/her roll number is 5A004 and so on.
     * </p>
     * <p>
     *    The roll number is generated based on
     * </p>
     *
     * @param gradeIDAllocated
     *        The grade ID allocated to the student.
     * @param rollNumberSuffix
     *        The suffix for the roll number.
     * @return String
     *         The generated roll number.
     *
     */
    private synchronized String generateRollNumber(String gradeIDAllocated, int rollNumberSuffix) {
        logger.debug("Generating the roll number for grade {}", gradeIDAllocated);
        if (rollNumberSuffix + 1 >= 100) {
            logger.info("Roll number generated successfully");
            return gradeIDAllocated + String.format("%01d", ++rollNumberSuffix);
        } else if (rollNumberSuffix + 1 >= 10) {
            logger.info("Roll number generated successfully");
            return gradeIDAllocated + String.format("%02d", ++rollNumberSuffix);
        } else {
            logger.info("Roll number generated successfully");
            return gradeIDAllocated + String.format("%03d", ++rollNumberSuffix);
        }
    }

    /**
     * Checks the given list of special classes and returns a list of special classes that have no vacancy.
     *
     * @param specialClasses the list of special classes to check for vacancy
     * @return a list of SpecialClassesEnum indicating which special classes have no vacancy
     */
    private List<SpecialClassesEnum> checkAndGetSpecialClassWithoutVacancy(List<SpecialClass> specialClasses) {
        List<SpecialClassesEnum> specialClassWithoutVacancy = new ArrayList<>();
        for (SpecialClass specialClass : specialClasses) {
            if(specialClass.getVacancy() == 0) {
                logger.info("No vacancy is available for the preferred special class {} " +
                                "Adding student to the database aborted",
                        specialClass.getClassName());
                specialClassWithoutVacancy.add(specialClass.getClassName());
            }
        }
        return specialClassWithoutVacancy;
    }
}
