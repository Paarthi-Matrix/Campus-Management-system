package com.ideas2it.cms.customexception;

public class PreCondtionValidationException extends RuntimeException {
    public PreCondtionValidationException(String message) {
        super(message);
    }
    public PreCondtionValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
