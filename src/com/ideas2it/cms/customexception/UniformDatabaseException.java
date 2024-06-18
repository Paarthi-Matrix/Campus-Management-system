package src.com.ideas2it.cms.customexception;

/**
 *
 * <p>
 *
 * A custom made Runtime exeption.
 * This exception is thrown while inseting, deleting and updating the uniform measurements-
 * of students in database. 
 *
 * </p>
 *
 */

public class UniformDatabaseException extends RuntimeException {

    public UniformDatabaseException (String message, Throwable cause) {      
        super(message, cause);
    }

}