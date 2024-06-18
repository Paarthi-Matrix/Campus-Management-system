package src.com.ideas2it.cms.service;

import java.util.List;

import src.com.ideas2it.cms.customexception.GradeDatabaseException;
import src.com.ideas2it.cms.dao.GradeDAO;
import src.com.ideas2it.cms.model.Grade;

/**
 *
 * <p>
 *
 * The `GradeService` class is responsible for managing the interactions 
 * between the application and the grade-related data in the database. 
 * It provides methods to get a preferred grade, update the number of 
 * students and vacancy availability, and retrieve the number of students 
 * in a specific grade.
 *
 * </p>
 *
 */

public class GradeService {

    private GradeDAO gradeDao = new GradeDAO();

    /**
     * <p>
     * This method fetches the preferred grade based on the user's input.
     * </p>
     *
     * @param gradePreference
     *        The preferred grade specified by the user.
     *
     * @return Grade
     *         Returns the grade with available vacancies that matches the preference.
     *         If no such grade is found, returns `null`. Also returns `null` if `HibernateDbConnectionException` arises.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     */

    public Grade getPreferedGrade(int gradePreference) {
        try {
            return gradeDao.getPreferedGrade(gradePreference);
        } catch (GradeDatabaseException e) {          
            return null;
        }  
    }

    /**
     *
     * <p>
     *
     * This method updates the number of students and vacancy availability for a given grade.
     *
     * </p>
     *
     * @param gradeIDAllocated
     *        The allocated grade ID for which the number of students and vacancy availability need to be updated.
     *
     * @param action
     *        A boolean value indicating the action to be performed. If `true`, increments the number of students and 
     *        decrements the vacancy. If `false`, decrements the number of students and increments the vacancy.
     *
     * @throws GradeDatabaseException
     *         If an error occurs while updating the number of students and vacancy availability.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public void updateNoOfStudentsAndVacancyAvailablity(String gradeIDAllocated, boolean action) {

        gradeDao.updateNoOfStudentsAndVacancyAvailablity(gradeIDAllocated, action);
               
    }

    /**
     *
     * <p>
     *
     * This method retrieves the number of students in a specified grade.
     *
     * </p>
     *
     * @param gradeAllocated
     *        The grade ID for which the number of students needs to be retrieved.
     *
     * @return int
     *         Returns the number of students in the specified grade. If the grade is not found, returns -1.
     *
     * @throws GradeDatabaseException
     *         If an error occurs while fetching the number of studentsfrom grade database.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public int getNumberOfStudents(String gradeAllocated) {
        return gradeDao.getNumberOfStudents(gradeAllocated);
    }

    /**
     * <p>
     *
     * This method fetches the preferred grade based on the user's input.
     *
     * </p>
     *
     * @param gradePreference
     *        The preferred grade specified by the user.
     *
     * @return Grade
     *         Returns the grade with available vacancies that matches the preference.
     *         If no such grade is found, returns `null`. Also returns `null` if `HibernateDbConnectionException` arises.
     *
     * @throws GradeDatabaseException
     *         Arises while geting the entire grade detalis.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public List<Grade> getGradeInfo(String requestedGrade) {

       return gradeDao.getGradeInfo(requestedGrade);
    }
}