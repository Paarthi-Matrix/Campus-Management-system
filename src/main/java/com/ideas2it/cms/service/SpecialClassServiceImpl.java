package com.ideas2it.cms.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ideas2it.cms.customexception.SpecialClassException;
import com.ideas2it.cms.helper.SpecialClassesEnum;
import com.ideas2it.cms.model.SpecialClass;

import com.ideas2it.cms.repository.SpecialclassRepo;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class SpecialClassServiceImpl implements SpecialClassService{

    private static final Logger logger = LogManager.getLogger(SpecialClassServiceImpl.class);

    @Autowired
    private SpecialclassRepo specialclassRepo;

    /**
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
    public void UpdateVacancyOfSpecialClass(List<SpecialClassesEnum> specialClassPreference, boolean action) {
        Set<SpecialClass> specialClasses = new HashSet<>();
        if (specialClassPreference != null && !specialClassPreference.isEmpty()) {
            specialClasses = new HashSet<>(specialclassRepo.findByClassType(specialClassPreference));
        } else {
            logger.warn("The specialClassPreference is empty or null ");
            return;
        }

        int number = action ? 1 : -1;
        for (SpecialClass specialClass : specialClasses) {
            specialClass.setVacancy(specialClass.getVacancy() - number);
            specialClass.setNumberOfStudents(specialClass.getNumberOfStudents() + number);
            specialclassRepo.save(specialClass);
        }
    }
}
