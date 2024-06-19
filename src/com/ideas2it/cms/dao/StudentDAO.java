package src.com.ideas2it.cms.dao;

import java.util.ArrayList;
import java.util.List;

import src.com.ideas2it.cms.customexception.StudentDatabaseException;
import src.com.ideas2it.cms.customexception.HibernateDbConnectionException;
import src.com.ideas2it.cms.helper.HibernateDbConnection;
import src.com.ideas2it.cms.model.Student;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * <p>
 *
 * The `StudentDAO` class is responsible for managing the interactions
 * between the application and the student-related data in the database.
 * It provides methods to add a student, get and delete a student by roll number,
 * retrieve students by grade, associate a student to a special class,
 * and add uniform measurements to a student.
 *
 * </p>
 */

public class StudentDAO {

    private HibernateDbConnection hibernateDbConnection = HibernateDbConnection.getInstance();
    private SessionFactory sessionFactory = hibernateDbConnection.getSessionFactory();

    /**
     *
     * <p>
     * Adds a new student to the database.
     * </p>
     *
     * @param student 
     *        The student object to be added to the database.
     *
     * @return Student 
     *         Returns the added student object.
     *
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while adding the student.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public Student addStudent(Student student) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(student);
            transaction.commit();
            return student;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            String errorMessage = "An error occurred while attempting to add student " +
                                   student.getStudentName() + " with roll number " + student.getRollNumber() + " to the database. " +
                                   "Please try again!";
            throw new StudentDatabaseException(errorMessage, e);
        } 
    }

    /**
     *
     * <p>
     * Retrieves and deletes a student from the database by their roll number.
     * </p>
     *
     * @param rollNumber 
     *        The roll number of the student to be retrieved and deleted.
     *
     * @return Student 
     *         Returns the `student` object if found, else null.
     *
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while retrieving or deleting the student.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public Student getAndDeleteStudentByRollNumber(String rollNumber) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Student> query = session.createQuery("FROM Student WHERE rollNumber = :rollNumber", Student.class);
            query.setParameter("rollNumber", rollNumber);
            Student student = (Student) query.uniqueResult();
            if (student != null) {
                session.delete(student);
                transaction.commit();
            }
            return student;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            String errorMessage = "An error occurred while attempting to delete student " +
                                  "with roll number " + rollNumber + " from the database. " +
                                  "Please try again!";
            throw new StudentDatabaseException(errorMessage, e);
        } 
    }

    /**
     *
     * <p>
     * Retrieves a list of students from the database by their grade.
     * </p>
     *
     * @param requestedGrade 
     *        The grade to filter by students.
     *
     * @return List<Student> 
     *         The list of students in the specified grade. If there is no students `null` is returned.
     *
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while retrieving the students.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public List<Student> getStudentByGrade(String requestedGrade) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Student> query = session.createQuery("FROM Student s WHERE s.grade.gradeId LIKE :gradeId", Student.class);
            query.setParameter("gradeId", requestedGrade + "%");
            List<Student> students = query.getResultList();
            return students;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            String errorMessage = "An error occurred while attempting to retrieve students for grade " +
                                  requestedGrade + ". Please try again!";
            throw new StudentDatabaseException(errorMessage, e);
        }
    }

    /**
     * 
     * <p>
     * Associates a student to a special class in the database.
     * </p>
     *
     * @param student 
     *        The student object to be associated with a special class.
     *
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while associating the student.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public void associateStudentToSpecialClass(Student student) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            String errorMessage = "An error occurred while associating student to special class " +
                                  "for student " + student.getStudentName() + " with roll number " +
                                  student.getRollNumber() + ". Please try again!";
            throw new StudentDatabaseException(errorMessage, e);
        }
    }

    /**
     *
     * <p>
     * Adds uniform measurements to a student in the database.
     * </p>
     *
     * @param student 
     *        The student object with updated uniform measurements.
     *
     * @throws StudentDatabaseException 
     *         Arises if an error occurs while adding the uniform measurements.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public void addUniformMeasurementToStudent(Student student) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            String errorMessage = "An error occurred while adding uniform measurements for student " +
                                  student.getStudentName() + " with roll number " +
                                  student.getRollNumber() + ". Please try again!";
            throw new StudentDatabaseException(errorMessage, e);
        }
    }
}
