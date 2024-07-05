package com.ideas2it.cms.dto;

import com.ideas2it.cms.model.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetStudentByGradeResponseDto {
    List<Student> students;
}
