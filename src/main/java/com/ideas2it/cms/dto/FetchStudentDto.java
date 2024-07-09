package com.ideas2it.cms.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FetchStudentDto {
    private String studentName;
    private String rollNumber;
    private String bloodGroup;
    private String dateOfBirth;
    private int age;
    private String standard;
    private String section;
    private boolean isStudentAvailable = true;

    public void setIsStudentAvailable(boolean value) {
        this.isStudentAvailable = value;
    }

    public boolean getIsStudentAvailable() {
        return  isStudentAvailable;
    }
}
