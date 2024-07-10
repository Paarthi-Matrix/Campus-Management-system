package com.ideas2it.cms.customexception;

import com.ideas2it.cms.dto.StudentResponceDto;

public class SpecialClassNotfoundException extends RuntimeException{
    private StudentResponceDto studentResponceDto;
    public SpecialClassNotfoundException(String message, StudentResponceDto studentResponceDto) {
        super(message);
        this.studentResponceDto = studentResponceDto;
    }
    public StudentResponceDto getStudentResponceDto() {
        return studentResponceDto;
    }
}
