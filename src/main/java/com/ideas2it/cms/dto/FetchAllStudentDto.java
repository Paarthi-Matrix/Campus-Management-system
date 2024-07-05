package com.ideas2it.cms.dto;

import com.ideas2it.cms.model.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllStudentDto {
    private String studentName;
    private String dateOfBirth;
    private String bloodGroup;
    private String standard;
    private String section;
    private String rollNumber;
    private int age;
}
