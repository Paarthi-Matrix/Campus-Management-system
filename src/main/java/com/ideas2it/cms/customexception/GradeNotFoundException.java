package com.ideas2it.cms.customexception;

public class GradeNotFoundException extends RuntimeException{
    public GradeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public GradeNotFoundException(String message) {
        super(message);
    }
}
