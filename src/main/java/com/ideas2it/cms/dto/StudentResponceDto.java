package com.ideas2it.cms.dto;

import com.ideas2it.cms.helper.SpecialClassesEnum;
import com.ideas2it.cms.model.Grade;
import com.ideas2it.cms.model.SpecialClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponceDto {
    private String studentName;
    private String standard;
    private String section;
    private String rollNumber;
    private int age;
    private boolean isGradeAvailable = true;
    private List<SpecialClassesEnum> specialClassesWithoutVacancy = null;
    public boolean getIsGradeAvailable() {
        return isGradeAvailable;
    }
    public void setIsGradeAvailable(boolean state) {
        this.isGradeAvailable = state;
    }
}
