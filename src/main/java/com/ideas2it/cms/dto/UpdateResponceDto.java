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
public class UpdateResponceDto  {
    private String studentName;
    private String rollNumber;
    private String bloodGroup;
    private String dateOfBirth;
    private String status;
}
