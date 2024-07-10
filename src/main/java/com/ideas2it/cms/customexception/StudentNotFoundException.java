package com.ideas2it.cms.customexception;

import com.ideas2it.cms.dto.FetchStudentDto;
import com.ideas2it.cms.dto.StudentResponceDto;

public class StudentNotFoundException extends RuntimeException {

    private FetchStudentDto fetchStudentDto;
    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public StudentNotFoundException(String message) {
        super(message);
    }
    public StudentNotFoundException(String message, FetchStudentDto fetchStudentDto) {
        super(message);
        this.fetchStudentDto = fetchStudentDto;
    }

    public FetchStudentDto getFetchStudentDto() {
        return fetchStudentDto;
    }
}
