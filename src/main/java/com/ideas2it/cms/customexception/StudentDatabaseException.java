package com.ideas2it.cms.customexception;

/**
 * <p>
 * A custom made Runtime exeption.
 * This exception is thrown while performing CRUD operations on student database. 
 * </p>
 *
 */
public class StudentDatabaseException extends RuntimeException {
    
    public StudentDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
