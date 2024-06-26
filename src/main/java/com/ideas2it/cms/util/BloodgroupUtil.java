package com.ideas2it.cms.util;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BloodgroupUtil {
    private static final Logger logger = LoggerFactory.getLogger(BloodgroupUtil.class);
    private static final Set<String> VALID_BLOOD_GROUPS = new HashSet<>();

    static {
        VALID_BLOOD_GROUPS.add("A+VE");
        VALID_BLOOD_GROUPS.add("A-VE");
        VALID_BLOOD_GROUPS.add("B+VE");
        VALID_BLOOD_GROUPS.add("B-VE");
        VALID_BLOOD_GROUPS.add("AB+VE");
        VALID_BLOOD_GROUPS.add("AB-VE");
        VALID_BLOOD_GROUPS.add("O+VE");
        VALID_BLOOD_GROUPS.add("O-VE");
    }

    public static String validateBloodGroup(String bloodGroup) {
        logger.info("Validating blood group!");
        if (bloodGroup == null || bloodGroup.isEmpty()) {
            logger.warn("The given blood group is null!");
            throw new IllegalArgumentException("Blood group cannot be null or empty");
        }
        String upperCaseBloodGroup = bloodGroup.trim().toUpperCase();

        if (VALID_BLOOD_GROUPS.contains(upperCaseBloodGroup)) {
            return upperCaseBloodGroup;
        } else {
            logger.warn("User gave invalid blood group {}", bloodGroup);
            return null;
        }
    }
}
