package src.com.ideas2it.cms.customexception;

/**
 *
 * <p>
 *
 * A custom made Runtime exeption.
 * This exception is thrown while inseting and updating grade into database. 
 *
 * </p>
 *
 */

public class GradeDatabaseException extends RuntimeException {

    public GradeDatabaseException (String message, Throwable cause) {
        
        super(message, cause);
    }

}