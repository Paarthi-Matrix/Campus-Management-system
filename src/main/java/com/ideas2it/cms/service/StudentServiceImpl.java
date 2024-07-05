package com.ideas2it.cms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ideas2it.cms.customexception.StudentDatabaseException;
import com.ideas2it.cms.dao.StudentDAO;
import com.ideas2it.cms.dto.*;
import com.ideas2it.cms.helper.EntityDtoConverter;
import com.ideas2it.cms.model.Grade;
import com.ideas2it.cms.model.SpecialClass;
import com.ideas2it.cms.model.UniformMeasurement;
import com.ideas2it.cms.model.Student;

import com.ideas2it.cms.repository.SpecialclassRepo;
import com.ideas2it.cms.repository.StudentRepo;
import com.ideas2it.cms.util.ConversionUtil;
import com.ideas2it.cms.util.DateUtil;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private StudentDAO studentDao;
    @Autowired
    private SpecialClassServiceImpl specialClassServiceImpl;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    SpecialclassRepo specialclassRepo;

    /**
     * <p>
     * Adds a new student to the database with the provided details.
     * </p>
     *
     * @param `studentName`
     *        The name of the student. First name followed by Last name.
     * @param `dateOfBirth`
     *        The date of birth of the student. Should be in formate of (dd/MM/yyyy). 
     * @param `bloodGroup`
     *        The blood group of the student.
     *        They must be a valid blood group (A+VE, A-VE, B+VE, B-VE, AB+VE, AB-VE, O+VE, O-VE).
     *        CASE INSENSITIVE
     * @param `gradeAllocated`
     *        The grade allocated to the student.
     * @param `grade`
     *        The Grade object associated with the student.
     * @return Student 
     *         Returns the added student object. Returns `null` if `HibernateDbConnectionException` arises.
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while adding the student.
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
            return null;
        }
        rollNumberSuffix = grade.getNumberOfStudents();
        rollNumber = generateRollNumber(grade.getStandard() + grade.getSection(),
                rollNumberSuffix);
        student = new Student(studentRequestDto.getStudentName(), rollNumber, studentRequestDto.getBloodGroup(), studentRequestDto.getDateOfBirth());
        student.setGrade(grade);

        gradeServiceImpl.updateNoOfStudentsAndVacancyAvailablity(grade.getGradeId(), true);
        List<Integer> specialClassPreference = studentRequestDto.getSpecialClassRequestDto().getSpecialClassPreference();
        List<SpecialClass> specialClasses = specialclassRepo.findBySpecialClassId(specialClassPreference);
        student.setSpecialClasses(ConversionUtil.convertArrayListToSet(specialClasses));
        specialClassServiceImpl.UpdateVacancyOfSpecialClass(specialClassPreference, true);
        student.setUniformMeasurement(EntityDtoConverter.toUniformMeasurement(studentRequestDto));

        studentRepo.save(student);

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

    /**
     *
     * <p>
     * Generates a roll number for the student based on the grade ID and roll number suffix.
     * If the student belongs to 5th grade and "A" section and the student is the 4th student to get added in that grade and section-
     * then his/her roll number is 5A004 and so on.
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
    private String generateRollNumber(String gradeIDAllocated, int rollNumberSuffix) {
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
     *
     * <p>
     * Retrieves and deletes a student by their roll number.
     * </p>
     *
     * @param rollNumber 
     *        The roll number of the student to be retrieved and deleted.
     * @return Student 
     *         Returns the deleted student object if found, else null.
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while retrieving or deleting the student.
     *
     */
    @Transactional
    public DeleteStudentResponceDto deleteStudentByRollNumber(String rollNumber) {
        Student student = studentRepo.findByRollNumber(rollNumber); // Find the student first
        if (student == null) {
            return null;
        }
        Set<SpecialClass> specialClasses = student.getSpecialClass();
        List<Integer> associatedSpecialClasses = ConversionUtil.convertSetToList(specialClasses);
        studentRepo.deleteByRollNumber(rollNumber);
        gradeServiceImpl.updateNoOfStudentsAndVacancyAvailablity(student.getGrade().getGradeId(), false);
        specialClassServiceImpl.UpdateVacancyOfSpecialClass(associatedSpecialClasses, false);
        DeleteStudentResponceDto deleteStudentResponceDto = new DeleteStudentResponceDto();
        deleteStudentResponceDto.setStudentName(student.getStudentName());
        return deleteStudentResponceDto;
    }

    /**
     *
     * <p>
     * Retrieves a list of students by their grade.
     * </p>
     *
     * @param requestedGrade 
     *        The grade to filter by students.
     * @return List<Student> 
     *         The list of students in the specified grade. If there are no students, returns null.
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while retrieving the students.
     *
     */
    public List<FetchStudentByGradeDto> getStudentByGrade(String requestedGrade) {
        List<Student> students= studentRepo.findByGradeId(requestedGrade);

        List<FetchStudentByGradeDto> fetchStudentByGradeDtos = new ArrayList<>();
        for (Student student : students) {
            FetchStudentByGradeDto dto = new FetchStudentByGradeDto();
            dto.setStudentName(student.getStudentName());
            dto.setRollNumber(student.getRollNumber());
            dto.setBloodGroup(student.getBloodGroup());
            dto.setDateOfBirth(student.getDateOfBirth());
            dto.setAge(DateUtil.calculateDifferenceOfTwoDates(student.getDateOfBirth(), null, "years"));

            // Add the dto to the list
            fetchStudentByGradeDtos.add(dto);
        }
        return fetchStudentByGradeDtos;
    }


    /**
     * <p>
     * Adds uniform measurements to a student.
     * </p>
     * @param `student`
     *        The student object to be updated with uniform measurements.
     * @param `shirtSize`
     *        The shirt size for the uniform. The shirt size varies from (XM, S, M, L, XL, XXL).
     * @param `pantSize`
     *        The pant size for the uniform. Size must be in centi meters as number.
     * @param `shoeSize`
     *        The shoe size for the uniform. Shoe size varies from 7 to 10.
     * @throws `StudentDatabaseException`
     *         Arises if an error occurs while adding the uniform measurements.
     *
     */
    @Transactional
    public void addUniformMeasurementToStudent(StudentRequestDto studentRequestDto, StudentResponceDto studentResponceDto) {
        Student existingStudent = studentRepo.findByRollNumber(studentResponceDto.getRollNumber());

        if (existingStudent == null) {
            throw new IllegalArgumentException("Student with roll number " +
                    studentResponceDto.getRollNumber() + " not found.");
        }
        UniformMeasurement uniformMeasurement = new UniformMeasurement(
                studentResponceDto.getRollNumber(),
                studentRequestDto.getUniformRequestDto().getShirtSize(),
                studentRequestDto.getUniformRequestDto().getPantSize(),
                studentRequestDto.getUniformRequestDto().getShoeSize()
        );
        existingStudent.setUniformMeasurement(uniformMeasurement);
        studentRepo.save(existingStudent);
    }

    public List<FetchAllStudentDto> getAllStudents() {
        List<Student> students = studentRepo.findAll();

        List<FetchAllStudentDto> fetchAllStudentDtos = new ArrayList<>();
        for (Student student : students) {
            FetchAllStudentDto dto = new FetchAllStudentDto();
            dto.setStudentName(student.getStudentName());
            dto.setRollNumber(student.getRollNumber());
            dto.setBloodGroup(student.getBloodGroup());
            dto.setDateOfBirth(student.getDateOfBirth());
            dto.setAge(DateUtil.calculateDifferenceOfTwoDates(student.getDateOfBirth(), null, "years"));
            fetchAllStudentDtos.add(dto);
        }
        return fetchAllStudentDtos;
    }
}
