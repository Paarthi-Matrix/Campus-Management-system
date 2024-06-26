package src.com.ideas2it.cms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import src.com.ideas2it.cms.customexception.StudentDatabaseException;
import src.com.ideas2it.cms.customexception.HibernateDbConnectionException;
import src.com.ideas2it.cms.dao.StudentDAO;
import src.com.ideas2it.cms.model.Grade;
import src.com.ideas2it.cms.model.SpecialClass;
import src.com.ideas2it.cms.model.UniformMeasurement;
import src.com.ideas2it.cms.service.GradeService;
import src.com.ideas2it.cms.service.SpecialClassService;
import src.com.ideas2it.cms.model.Student;



/**
 *
 * <p>
 *
 * Provides services to access the Student DAO.
 * All business logic related to students and their roll number generation are handled here.
 *
 * </p>
 *
 */

public class StudentService {

    private GradeService gradeService = new GradeService();
    private StudentDAO studentDao = new StudentDAO();
    private SpecialClassService specialClassService = new SpecialClassService();

    /**
     *
     * <p>
     *
     * Adds a new student to the database with the provided details.
     *
     * </p>
     *
     * @param studentName 
     *        The name of the student. First name followed by Last name.
     *
     * @param dateOfBirth 
     *        The date of birth of the student. Should be in formate of (dd/MM/yyyy). 
     *
     * @param bloodGroup 
     *        The blood group of the student. Everything should be uppercase.
     * 
     * @param gradeAllocated
     *        The grade allocated to the student.
     *
     * @param grade 
     *        The Grade object associated with the student.
     *
     * @return Student 
     *         Returns the added student object. Returns `null` if `HibernateDbConnectionException` arises.
     *
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while adding the student.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public Student addStudent(String studentName, String dateOfBirth, String bloodGroup, String gradeAllocated, Grade grade) {

        int rollNumberSuffix;
        String rollNumber;
        Student student = null;

        try {
            rollNumberSuffix = gradeService.getNumberOfStudents(gradeAllocated);
            rollNumber = generateRollNumber(gradeAllocated, rollNumberSuffix);
            student = new Student(studentName, rollNumber, bloodGroup, dateOfBirth);
            student.setGrade(grade);
            studentDao.addStudent(student);
            gradeService.updateNoOfStudentsAndVacancyAvailablity(gradeAllocated, true);
            return student;
        } catch (StudentDatabaseException e) {
            return null;
        }
    }

    /**
     *
     * <p>
     *
     * Generates a roll number for the student based on the grade ID and roll number suffix.
     * If the student belongs to 5th grade and "A" section and the student is the 4th student to get added in that grade and section-
     * then his/her roll number is 5A004 and so on.
     *
     * </p>
     *
     * @param gradeIDAllocated 
     *        The grade ID allocated to the student.
     *
     * @param rollNumberSuffix 
     *        The suffix for the roll number.
     *
     * @return String 
     *         The generated roll number.
     *
     */

    private String generateRollNumber(String gradeIDAllocated, int rollNumberSuffix) {

        if (rollNumberSuffix + 1 >= 100) {
            return gradeIDAllocated + String.format("%01d", ++rollNumberSuffix);
        } else if (rollNumberSuffix + 1 >= 10) {
            return gradeIDAllocated + String.format("%02d", ++rollNumberSuffix);
        } else {
            return gradeIDAllocated + String.format("%03d", ++rollNumberSuffix);
        }
    }

    /**
     *
     * <p>
     *
     * Retrieves and deletes a student by their roll number.
     *
     * </p>
     *
     * @param rollNumber 
     *        The roll number of the student to be retrieved and deleted.
     *
     * @return Student 
     *         Returns the deleted student object if found, else null.
     *
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while retrieving or deleting the student.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public Student getAndDeleteStudentByRollNumber(String rollNumber) {
        Student student = studentDao.getAndDeleteStudentByRollNumber(rollNumber);
        if (null == student) {
            return null;
        }
        gradeService.updateNoOfStudentsAndVacancyAvailablity(rollNumber.substring(0, 2), false);
        return student;
    }

    /**
     *
     * <p>
     *
     * Retrieves a list of students by their grade.
     *
     * </p>
     *
     * @param requestedGrade 
     *        The grade to filter by students.
     *
     * @return List<Student> 
     *         The list of students in the specified grade. If there are no students, returns null.
     *
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while retrieving the students.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public List<Student> getStudentByGrade(String requestedGrade) {
        return studentDao.getStudentByGrade(requestedGrade);
    }

    /**
     *
     * <p>
     *
     * Associates a student to special classes based on their preferences.
     *
     * </p>
     *
     * @param student 
     *        The student object to be associated with special classes.
     *
     * @param specialClassPreference 
     *        The array of special class IDs preferred by the student.
     *
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while associating the student to special classes.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public void associateStudentToSpecialClass(Student student, int[] specialClassPreference) {
        Set<SpecialClass> specialClasses = specialClassService.retrieveSpecialClasses(specialClassPreference);
        student.setSpecialClass(specialClasses);
        studentDao.associateStudentToSpecialClass(student);
        specialClassService.updateVacancyAndNunmberOfStudents(specialClassPreference, true);
    }

    /**
     *
     * <p>
     *
     * Adds uniform measurements to a student.
     *
     * </p>
     *
     * @param student 
     *        The student object to be updated with uniform measurements.
     *
     * @param shirtSize 
     *        The shirt size for the uniform. The shirt size varies from (XM, S, M, L, XL, XXL).
     *
     * @param pantSize 
     *        The pant size for the uniform. Size must be in centi meters as number.
     *
     * @param shoeSize 
     *        The shoe size for the uniform. Shoe size varies from 7 to 10.
     *
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while adding the uniform measurements.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public void addUniformMeasurementToStudent(Student student, String shirtSize, int pantSize, int shoeSize) {
        UniformMeasurement uniformMeasurement = new UniformMeasurement(student.getRollNumber(), shirtSize, pantSize, shoeSize);
        student.setUniformMeasurement(uniformMeasurement);
        studentDao.addUniformMeasurementToStudent(student);
    }

    public Grade getPreferedGrade(int gradePreference) {
        return gradeService.getPreferedGrade(gradePreference);
    }
}
