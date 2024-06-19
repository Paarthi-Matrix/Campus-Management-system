package src.com.ideas2it.cms.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

import src.com.ideas2it.cms.customexception.HibernateDbConnectionException;
import src.com.ideas2it.cms.customexception.SpecialClassException;
import src.com.ideas2it.cms.helper.HibernateDbConnection;
import src.com.ideas2it.cms.model.SpecialClass;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * <p>
 *
 * The `SpecialClassDAO` class is responsible for managing the interactions
 * between the application and the special class-related data in the database.
 * It provides methods to retrieve special classes based on preferences and
 * update the vacancy and number of students for special classes.
 *
 * </p>
 */
 
public class SpecialClassDAO {

    private HibernateDbConnection hibernateDbConnection = HibernateDbConnection.getInstance();
    private SessionFactory sessionFactory = hibernateDbConnection.getSessionFactory();

    /**
     *
     * <p>
     * Retrieves special classes from the database based on the provided preferences.
     * </p>
     *
     * @param specialClassPreference 
     *        The array of special class IDs to be retrieved.
     *
     * @return Set<SpecialClass> 
     *         The set of special classes matching the preferences.
     *
     * @throws SpecialClassException 
     *         Arises if an error occurs while retrieving special classes.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public Set<SpecialClass> retrieveSpecialClasses(int[] specialClassPreference) {
        Transaction transaction = null;
        Set<SpecialClass> specialClasses = new HashSet<>();

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            if (specialClassPreference != null && specialClassPreference.length > 0) {
                List<Integer> preferenceList = Arrays.stream(specialClassPreference).boxed().collect(Collectors.toList());
                Query<SpecialClass> query = session.createQuery(
                        "FROM SpecialClass WHERE specialClassId IN :specialClassPreference", SpecialClass.class);
                query.setParameter("specialClassPreference", preferenceList);
                specialClasses.addAll(query.getResultList());
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            String errorMessage = "An error occurred while retrieving special classes " +
                                  convertSpecialClassArrayToString(specialClassPreference);
            throw new SpecialClassException(errorMessage, e);
        }
        return specialClasses;
    }

    /**
     * <p>
     * Updates the vacancy and number of students for the special classes
     * based on the provided preferences and action.
     * </p>
     *
     * @param specialClassPreference 
     *        The array of special class IDs to be updated.
     *
     * @param action 
     *        The action to be performed: true for increasing the number of students and decreasing vacancy,
     *        false for decreasing the number of students and increasing vacancy.
     *
     * @throws SpecialClassException 
     *         Arises if an error occurs while updating vacancy and number of students.
     *
     * @throws HibernateDbConnectionException
     *         Arises if unable to connect and get the `SessionFactory`.
     *
     */

    public void updateVacancyAndNunmberOfStudents(int[] specialClassPreference, boolean action) {

        Transaction transaction = null;
        Set<SpecialClass> specialClasses = new HashSet<>();

        try  (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            if (specialClassPreference != null && specialClassPreference.length > 0) {
                List<Integer> preferenceList = Arrays.stream(specialClassPreference).boxed().collect(Collectors.toList());
                Query<SpecialClass> query = session.createQuery(
                        "FROM SpecialClass WHERE specialClassId IN :specialClassPreference", SpecialClass.class);
                query.setParameter("specialClassPreference", preferenceList);
                specialClasses.addAll(query.getResultList());
                transaction.commit();

                if (specialClasses != null) {
                    int number = action ? 1 : -1;
                    for (SpecialClass specialClass : specialClasses) {
                        specialClass.setVacancy(specialClass.getVacancy() - number);
                        specialClass.setNumberOfStudents(specialClass.getNumberOfStudents() + number);
                    }
                }
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            String errorMessage = "An error occurred while updating vacancy and number of students for special class " +
                                  convertSpecialClassArrayToString(specialClassPreference);
            throw new SpecialClassException(errorMessage, e);
        } 
    }

    /**
     * <p>
     * Converts the special class preferences array to a string representation.
     * </p>
     *
     * @param specialClassPreference 
     *        The array of special class IDs to be converted.
     * @return String 
     *         The string representation of the special class preferences.
     *
     */

    private String convertSpecialClassArrayToString(int[] specialClassPreference) {
        StringBuilder specialClassString = new StringBuilder();
        for (int id : specialClassPreference) {
            specialClassString.append(id).append(" ");
        }
        return specialClassString.toString().trim();
    }
}
