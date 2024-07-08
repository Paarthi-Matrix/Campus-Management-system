package com.ideas2it.cms.dto;

import com.ideas2it.cms.helper.SpecialClassesEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestDto implements StudentDto{
    private String studentName;
    private String bloodGroup;
    private String dateOfBirth;
    private String gradePreferred;
}
