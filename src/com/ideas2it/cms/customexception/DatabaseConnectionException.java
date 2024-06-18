package src.com.ideas2it.cms.customexception;

/**
 *
 * <p>
 *
 * A custom made Runtime exeption.
 * This exception is thrown while connecting to the database through JDBC. 
 *
 * </p>
 *
 */

public class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }  

}