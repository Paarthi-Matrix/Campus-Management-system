package com.ideas2it.cms.service;

import java.util.List;

import com.ideas2it.cms.customexception.GradeDatabaseException;
import com.ideas2it.cms.dao.GradeDAO;
import com.ideas2it.cms.model.Grade;

import com.ideas2it.cms.repository.GradeRepo;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * <p>
 * The `GradeServiceImpl` class is responsible for managing the interactions
 * between the application and the grade-related data in the database. 
 * It provides methods to get a preferred grade, update the number of 
 * students and vacancy availability, and retrieve the number of students 
 * in a specific grade.
 * </p>
 *
 */

@Service
public class GradeServiceImpl implements GradeService {

    private static final Logger logger = LogManager.getLogger(GradeServiceImpl.class);

    @Autowired
    private GradeDAO gradeDao;

    @Autowired
    private GradeRepo gradeRepo;


    /**
     * <p>
     * This method fetches the preferred grade whose vacancy is > 0.
     * If the preferred grade is 7, it fetches all the grade starts with 7.(7A, 7B, 7C ...)
     * It returns the first occurrence grade whose vacancy is grater than 0(Zero).
     * </p>
     *
     * @param `gradePreference`
     *        The preferred grade specified by the user.
     * @return `Grade`
     *         Returns the grade with available vacancies that matches the preference.
     *         If no such grade is found, returns `null`. Also returns `null` if `HibernateDbConnectionException` arises.
     */
    public Grade getPreferedGrade(String standard) {
        System.out.println("Grade Preference: " + standard);

        List<Grade> grades = gradeRepo.findByStandard(standard);
        System.out.println("Grades Found: " + (grades == null ? "null" : grades.size()));

        if (grades == null || grades.isEmpty()) {
            System.out.println("No grades found with the given preference.");
            return null;
        }

        Grade grade = null;
        for (Grade gradeObj : grades) {
            System.out.println("Checking grade: " + gradeObj.getGradeId() + " with vacancy: " + gradeObj.getVacancy());
            if (gradeObj.getVacancy() > 0) {
                grade = gradeObj;
                break;
            }
        }

        return grade;
    }

    /**
     *
     * <p>
     * This method updates the number of students and vacancy availability for a given grade.
     * </p>
     *
     * @param gradeIDAllocated
     *        The allocated grade ID for which the number of students and vacancy availability need to be updated.
     * @param action
     *        A boolean value indicating the action to be performed. If `true`, increments the number of students and 
     *        decrements the vacancy. If `false`, decrements the number of students and increments the vacancy.
     * @throws GradeDatabaseException
     *         If an error occurs while updating the number of students and vacancy availability.
     */
    public void updateNoOfStudentsAndVacancyAvailablity(String gradeIDAllocated, boolean action) {
        if (action) {
            logger.info("Updating the number of student and " +
                    "vacancy for grade {} after performing addition " +
                    "of a student record", gradeIDAllocated );
            gradeRepo.updateNumberOfStudentsAndVacancy(gradeIDAllocated, 1);
        } else {
            logger.info("Updating the number of student and " +
                    "vacancy for grade {} after performing deletion " +
                    "of a student record ", gradeIDAllocated );
            gradeRepo.updateNumberOfStudentsAndVacancy(gradeIDAllocated, -1);
        }

        logger.info("Successfully updated the number of students and vacancy");
    }


    /**
     * <p>
     * This method retrieves the number of students in a specified grade.
     * </p>
     *
     * @param gradeAllocated
     *        The grade ID for which the number of students needs to be retrieved.
     * @return int
     *         Returns the number of students in the specified grade. If the grade is not found, returns -1.
     * @throws GradeDatabaseException
     *         If an error occurs while fetching the number of studentsfrom grade database.
     */
    public int getNumberOfStudents(String gradeAllocated) {
        logger.debug("Fetching number of students from the database");
        return gradeRepo.findNumberOfStudentsByGradeId(gradeAllocated);
    }

    /**
     * <p>
     * This method fetches the preferred grade based on the user's input.
     * </p>
     *
     * @param `gradePreference`
     *        The preferred grade specified by the user.
     * @return Grade
     *         Returns the grade with available vacancies that matches the preference.
     *         If no such grade is found, returns `null`. Also returns `null` if `HibernateDbConnectionException` arises.
     * @throws GradeDatabaseException
     *         Arises while geting the entire grade detalis.
     */
    public List<Grade> getGradeInfo(String requestedGrade) {
        logger.debug("Fetching grade info for {}", requestedGrade);
        return gradeDao.getGradeInfo(requestedGrade);
    }
}