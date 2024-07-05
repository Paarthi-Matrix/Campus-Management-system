package com.ideas2it.cms.util;

import com.ideas2it.cms.model.SpecialClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConversionUtil {

    public static List<Integer> convertSetToList(Set<SpecialClass> specialClasses) {
        List<Integer> specialClassPreferenceList = new ArrayList<>();
        for (SpecialClass specialClass : specialClasses) {
            specialClassPreferenceList.add(specialClass.getSpecialClassId());
        }
        return specialClassPreferenceList;
    }

    public static Set<SpecialClass> convertArrayListToSet(List<SpecialClass> specialClassList) {
        Set<SpecialClass> specialClasses = new HashSet<>();
        specialClasses.addAll(specialClassList);
        return specialClasses;
    }
}


