package com.ideas2it.cms.customexception;

/**
 *
 * <p>
 * A custom made Runtime exeption.
 * This exception is thrown while seting and configuring SessionFactory. 
 * </p>
 *
 */
public class HibernateDbConnectionException extends RuntimeException {

    public HibernateDbConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}