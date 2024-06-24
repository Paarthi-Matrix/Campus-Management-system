package com.ideas2it.cms.dao;

import java.util.ArrayList;
import java.util.List;

import com.ideas2it.cms.customexception.HibernateDbConnectionException;
import com.ideas2it.cms.customexception.GradeDatabaseException;
import com.ideas2it.cms.helper.HibernateDbConnection;
import com.ideas2it.cms.model.Grade;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


/**
 * <p>
 * Responsible for interacting with database related to grade.
 * The interactions includes CREATE, READ and DELETE.
 * </p>
 */

public class GradeDAO {

    private HibernateDbConnection hibernateDbConnection = HibernateDbConnection.getInstance();
    private SessionFactory sessionFactory = hibernateDbConnection.getSessionFactory();

    /**
     * <p>
     * This method gets prefered `grade` given by user. It also checks for vacancy and returns the grade-
     * with available section.
     * </p>
     *
     * @param gradePreference
     *        This is the prefered grade given by user. The `grade` are being fetched according to this param.
     * @return Grade
     *         If the prefered grade exist and vacancy is less than 0, returns the corresponding `Grade`.
     *         else returns `null`.
     * @throws GradeDatabaseException
     *         This exception arises while attempting to fetch and process the grade with preference.
     */
    public Grade getPreferedGrade(int gradePreference) {
        Transaction transaction = null;
        Grade grade = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Grade> query = session.createQuery("FROM Grade WHERE gradeId LIKE :gradeId", Grade.class);
            query.setParameter("gradeId", gradePreference + "%");
            List<Grade> grades = query.getResultList();

            for (Grade gradeObj : grades) {   /* find first grade with available vacancies */
                if (gradeObj.getVacancy() > 0) {
                    grade = gradeObj;
                    break;
                }
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            String errorMessage = "An error occurred while attempting to fetch" +
                                  " and process the grade with preference " +
                                   gradePreference;
            throw new GradeDatabaseException(errorMessage, e);
        }
        return grade;
    }

    /**
     *
     * <p>
     * This method gets prefered `grade` given by user. It also checks for vacancy and returns the grade-
     * with available section.
     * </p>
     *
     * @param requestedGrade
     *        This is the prefered grade given by user. The `grade` are being fetched according to this param.
     * @return List<Grade>
     *         If the prefered grade exist and vacancy is less than 0, returns the corresponding `Grade`.
     *         else returns `null`.
     * @throws GradeDatabaseException
     *         This exception arises while attempting to fetch and process the grade with preference.
     */
     public List<Grade> getGradeInfo(String requestedGrade) {
        Transaction transaction = null;
        List<Grade> grades = new ArrayList<>();

        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            Query<Grade> query = session.createQuery("FROM Grade WHERE gradeId LIKE :gradeId", Grade.class);
            query.setParameter("gradeId", requestedGrade + "%");
            grades = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            String errorMessage = "An error occurred while attempting to fetch the grade with preference " +
                                   requestedGrade;
            throw new GradeDatabaseException(errorMessage, e);
        }
        return grades;
    }


    /**
     * <p>
     * This method updates the section and vacancy availability for a given grade.
     * It either increments or decrements the vacancy and number of students based on the action parameter.
     * </p>
     *
     * @param gradeIdAllocated
     *        The allocated `gradeId` of the grade whose vacancy availability and number of student need to be updated.
     * @param action
     *        If true, increments the number of students and decrements the vacancy.
     *        If false, decrements the number of students and increments the vacancy.
     * @throws GradeDatabaseException
     *         This exception arises while updating vacancy and Number of students for prefered grade.
     *
     */
    public void updateNoOfStudentsAndVacancyAvailablity(String gradeIdAllocated, boolean action) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            Grade grade = session.get(Grade.class, gradeIdAllocated);
            if(null != grade) {
                int number = action ? 1 : -1;
                grade.setVacancy(grade.getVacancy() - number);
                grade.setNumberOfStudents(grade.getNumberOfStudents() + number);
                session.update(grade);
            }
            transaction.commit();
        } catch(Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            String errorMessage = "An error occurred while updating vacancy " +
                                  " and Number of students for grade " +
                                   gradeIdAllocated;
            throw new GradeDatabaseException(errorMessage, e);
        }
    }

    /**
     * <p>
     * This method is used to get the number of students for a particular `grade`.
     * </p>
     *
     * @param gradeId
     *        The `gradeId` is the coresponding grade for fetching the number of students.
     * @return number of Students.
     *         Returns number of students as integer when the `gradeId` is precent.
     *         Else returns -1.
     * @throws GradeDatabaseException
     *         This exception arises while fetching the number of students for prefered grade`.
     */
    public int getNumberOfStudents(String gradeId) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            Grade grade = session.get(Grade.class, gradeId);
            if(null == grade) {
                return -1;
            }
            transaction.commit();
            return grade.getNumberOfStudents();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            String errorMessage = "An error occurred while fetching the " +
                                  " number of students for prefered grade " +
                                   gradeId;
            throw new GradeDatabaseException(errorMessage, e);
        }
    }
}