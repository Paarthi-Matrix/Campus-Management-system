package src.com.ideas2it.cms.service;

import java.util.HashMap;
import java.sql.SQLException;
import java.util.Set;

import src.com.ideas2it.cms.customexception.DatabaseConnectionException;
import src.com.ideas2it.cms.customexception.SpecialClassException;
import src.com.ideas2it.cms.dao.SpecialClassDAO;
import src.com.ideas2it.cms.model.SpecialClass;


/**
 *
 * <p>
 *
 * The `SpecialClassService` class provides services to access and manipulate
 * special class-related data. It interacts with the `SpecialClassDAO` to perform
 * database operations. The class includes methods to retrieve special classes
 * based on preferences and update the vacancy and number of students for special classes.
 *
 * </p>
 *
 */

public class SpecialClassService {

    private SpecialClassDAO specialClassDAO = new SpecialClassDAO();

    /**
     *
     * <p>
     * Retrieves special classes based on the provided preferences by calling the DAO method.
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
     */

    public Set<SpecialClass> retrieveSpecialClasses(int[] specialClassPreference) {
        return specialClassDAO.retrieveSpecialClasses(specialClassPreference);
    }

    /**
     *
     * <p>
     *
     * Updates the vacancy and number of students for the special classes.
     * This method can be used for both increment and decrement of both vacancy and number of students.
     *
     * </p>
     *
     * @param specialClassPreference 
     *        The array of special class IDs to be updated.
     *
     * @param action 
     *        The action to be performed: true for increasing the number of students,
     *        false for decreasing the number of students.
     *
     * @throws SpecialClassException 
     *         Arises if an error occurs while updating vacancy and number of students.
     *
     */

    public void updateVacancyAndNunmberOfStudents(int[] specialClassPreference, boolean action) {
        specialClassDAO.updateVacancyAndNunmberOfStudents(specialClassPreference, action);
    }
}
