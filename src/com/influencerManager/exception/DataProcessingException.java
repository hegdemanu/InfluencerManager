package com.influencerManager.exception;

/**
 * Exception thrown for data processing errors.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class DataProcessingException extends Exception {
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new DataProcessingException with null as its detail message.
     */
    public DataProcessingException() {
        super();
    }
    
    /**
     * Constructs a new DataProcessingException with the specified detail message.
     * 
     * @param message The detail message
     */
    public DataProcessingException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new DataProcessingException with the specified cause.
     * 
     * @param cause The cause
     */
    public DataProcessingException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Constructs a new DataProcessingException with the specified detail message and cause.
     * 
     * @param message The detail message
     * @param cause The cause
     */
    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
