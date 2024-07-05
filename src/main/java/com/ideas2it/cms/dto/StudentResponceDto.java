package com.ideas2it.cms.dto;

import com.ideas2it.cms.model.Grade;
import com.ideas2it.cms.model.SpecialClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponceDto {
    private String studentName;
    private String dateOfBirth;
    private String bloodGroup;
    private String standard;
    private String section;
    private String rollNumber;
    private Grade grade;
    private int age;
    private Set<SpecialClass> specialClasses;
}
