package com.influencerManager.exception;

/**
 * Exception thrown for authentication-related errors.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class AuthenticationException extends Exception {
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new AuthenticationException with null as its detail message.
     */
    public AuthenticationException() {
        super();
    }
    
    /**
     * Constructs a new AuthenticationException with the specified detail message.
     * 
     * @param message The detail message
     */
    public AuthenticationException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new AuthenticationException with the specified cause.
     * 
     * @param cause The cause
     */
    public AuthenticationException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Constructs a new AuthenticationException with the specified detail message and cause.
     * 
     * @param message The detail message
     * @param cause The cause
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
