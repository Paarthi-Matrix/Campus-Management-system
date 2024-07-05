package com.ideas2it.cms.dto;

import com.ideas2it.cms.model.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestDto {
    private String studentName;
    private String bloodGroup;
    private String dateOfBirth;
    private String gradePreferred;
    private UniformRequestDto uniformRequestDto;
    private SpecialClassRequestDto specialClassRequestDto;
}
