package com.ideas2it.cms.util;

import com.ideas2it.cms.model.SpecialClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Utility class for converting between different collection types.
 * This class provides methods to convert a set of `SpecialClass` objects to a list of their IDs,
 * and to convert a list of `SpecialClass` objects to a set.
 * </p>
 * <p>
 * The primary functionalities include:
 * <ul>
 *     <li>Converting a set of `SpecialClass` objects to a list of their integer IDs.</li>
 *     <li>Converting a list of `SpecialClass` objects to a set.</li>
 * </ul>
 * </p>
 * <p>
 * This class can be useful in scenarios where different collection types are needed for different operations,
 * such as persisting data or performing set operations.
 * </p>
 */
public class ConversionUtil {

    /**
     * Converts a set of `SpecialClass` objects to a list of their integer IDs.
     *
     * @param `specialClasses`
     *         The set of `SpecialClass` objects to be converted.
     * @return `List<Integer>`
     *         A list of integer IDs representing the `SpecialClass` objects.
     */
    public static List<Integer> convertSetToList(Set<SpecialClass> specialClasses) {
        List<Integer> specialClassPreferenceList = new ArrayList<>();
        for (SpecialClass specialClass : specialClasses) {
            specialClassPreferenceList.add(specialClass.getSpecialClassId());
        }
        return specialClassPreferenceList;
    }

    /**
     * Converts a list of `SpecialClass` objects to a set.
     *
     * @param `specialClassList` The list of `SpecialClass` objects to be converted.
     * @return `Set<SpecialClass>` A set of `SpecialClass` objects.
     */
    public static Set<SpecialClass> convertArrayListToSet(List<SpecialClass> specialClassList) {
        Set<SpecialClass> specialClasses = new HashSet<>();
        specialClasses.addAll(specialClassList);
        return specialClasses;
    }

    /**
     * Converts a string to an integer.
     *
     * @param str the string to convert
     * @return the integer value of the string, or null if the string cannot be converted
     */
    public static int stringToInt(String str) {
        if (str == null || str.isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
