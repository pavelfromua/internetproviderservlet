package my.project.internetprovider.exception;

/**
 * An exception that provides information on a database access error.
 *
 *
 */
public class DataProcessingException extends RuntimeException {
    public DataProcessingException(String message, Exception exception) {
        super(message, exception);
    }
}
