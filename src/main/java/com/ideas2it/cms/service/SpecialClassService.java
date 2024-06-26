package com.ideas2it.cms.service;

import java.util.Set;

import com.ideas2it.cms.customexception.SpecialClassException;
import com.ideas2it.cms.dao.SpecialClassDAO;
import com.ideas2it.cms.model.SpecialClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * <p>
 * The `SpecialClassService` class provides services to access and manipulate
 * special class-related data. It interacts with the `SpecialClassDAO` to perform
 * database operations. The class includes methods to retrieve special classes
 * based on preferences and update the vacancy and number of students for special classes.
 * </p>
 *
 */

public class SpecialClassService {

    private static final Logger logger = LoggerFactory.getLogger(SpecialClassService.class);
    private SpecialClassDAO specialClassDAO = new SpecialClassDAO();

    /**
     *
     * <p>
     * Updates the vacancy and number of students for the special classes.
     * This method can be used for both increment and decrement of both vacancy and number of students.
     * </p>
     *
     * @param specialClassPreference 
     *        The array of special class IDs to be updated.
     * @param action 
     *        The action to be performed: true for increasing the number of students,
     *        false for decreasing the number of students.
     * @throws SpecialClassException 
     *         Arises if an error occurs while updating vacancy and number of students.
     */
    public Set<SpecialClass> getAndUpdateVacancyOfSpecialClass(int[] specialClassPreference, boolean action) {
        if (action) {
            logger.info("Updating the number of student and " +
                    "vacancy for special classes after performing addition " +
                    "of student record" );
        } else {
            logger.info("Updating the number of student and " +
                    "vacancy for grade after performing deletion " +
                    "of a student record ");
        }
        return specialClassDAO.getAndUpdateVacancyOfSpecialClass(specialClassPreference, action);
    }
}
