package src.com.ideas2it.cms.customexception;

/**
 *
 * <p>
 *
 * A custom made Runtime exeption.
 * This exception is thrown while associating and disassociating students from their respective special class. 
 *
 * </p>
 *
 */

public class SpecialClassException extends RuntimeException {

     public SpecialClassException(String message, Throwable cause) {      
        super(message, cause); 
     }

}