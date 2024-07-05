package com.ideas2it.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FetchStudentByGradeDto {
    private String studentName;
    private String dateOfBirth;
    private String bloodGroup;
    private String rollNumber;
    private int age;
}
