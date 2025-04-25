package com.influencerManager.util;

import com.influencerManager.model.User;
import com.influencerManager.service.UserService;
import com.influencerManager.exception.AuthenticationException;

/**
 * Utility class for handling user authentication.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class AuthenticationManager {
    private int maxLoginAttempts;
    private int loginTimeout; // seconds
    private int sessionTimeout; // minutes
    
    /**
     * Default constructor.
     */
    public AuthenticationManager() {
        this.maxLoginAttempts = 5;
        this.loginTimeout = 300; // 5 minutes
        this.sessionTimeout = 30; // 30 minutes
    }
    
    /**
     * Constructor with security parameters.
     * 
     * @param maxLoginAttempts Maximum number of login attempts before lockout
     * @param loginTimeout Lockout duration in seconds
     * @param sessionTimeout Session timeout in minutes
     */
    public AuthenticationManager(int maxLoginAttempts, int loginTimeout, int sessionTimeout) {
        this.maxLoginAttempts = maxLoginAttempts;
        this.loginTimeout = loginTimeout;
        this.sessionTimeout = sessionTimeout;
    }
    
    /**
     * Authenticate a user with username and password.
     * 
     * @param username The username
     * @param password The password
     * @param userService The user service
     * @return The authenticated user, or null if authentication fails
     * @throws AuthenticationException If authentication fails
     */
    public User authenticate(String username, String password, UserService userService) 
            throws AuthenticationException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new AuthenticationException("Username and password cannot be empty");
        }
        
        User user = userService.getUserByUsername(username);
        
        if (user == null) {
            throw new AuthenticationException("User not found");
        }
        
        if (!user.isActive()) {
            throw new AuthenticationException("Account is inactive");
        }
        
        if (!password.equals(user.getPassword())) {
            // In a real app, would increment failed attempts count
            throw new AuthenticationException("Invalid password");
        }
        
        // Update last login date
        user.updateLastLoginDate();
        
        return user;
    }
    
    /**
     * Validate password strength.
     * 
     * @param password The password to validate
     * @return true if password is strong enough, false otherwise
     */
    public boolean validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecialChar = true;
            }
        }
        
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
    
    /**
     * Generate a password hash.
     * In a real app, would use a proper hashing algorithm.
     * 
     * @param password The password to hash
     * @return The hashed password
     */
    public String hashPassword(String password) {
        // In a real app, would use bcrypt or similar
        // This is just a placeholder for demo purposes
        return "hashed_" + password;
    }
    
    /**
     * Verify a password against its hash.
     * 
     * @param password The password to verify
     * @param hash The hash to compare against
     * @return true if password matches hash, false otherwise
     */
    public boolean verifyPassword(String password, String hash) {
        // In a real app, would use bcrypt or similar
        // This is just a placeholder for demo purposes
        return hash.equals("hashed_" + password);
    }
    
    /**
     * Generate a new password reset token.
     * 
     * @param user The user
     * @return The reset token
     */
    public String generatePasswordResetToken(User user) {
        // In a real app, would store this token in a database with expiration
        // This is just a placeholder for demo purposes
        return "RESET_" + user.getUsername() + "_" + System.currentTimeMillis();
    }
    
    /**
     * Check if a session is valid.
     * 
     * @param sessionStartTime The session start time in milliseconds
     * @return true if session is still valid, false if timed out
     */
    public boolean isSessionValid(long sessionStartTime) {
        long currentTime = System.currentTimeMillis();
        long sessionDuration = currentTime - sessionStartTime;
        
        // Convert minutes to milliseconds
        long timeoutMillis = sessionTimeout * 60 * 1000L;
        
        return sessionDuration < timeoutMillis;
    }
    
    /**
     * Check if an account is locked out due to too many failed attempts.
     * 
     * @param failedAttempts Number of failed attempts
     * @param lastFailedAttemptTime Time of last failed attempt in milliseconds
     * @return true if account is locked, false otherwise
     */
    public boolean isAccountLocked(int failedAttempts, long lastFailedAttemptTime) {
        if (failedAttempts < maxLoginAttempts) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        long timeSinceLastAttempt = currentTime - lastFailedAttemptTime;
        
        // Convert seconds to milliseconds
        long timeoutMillis = loginTimeout * 1000L;
        
        return timeSinceLastAttempt < timeoutMillis;
    }
    
    /**
     * Get the maximum login attempts before lockout.
     * 
     * @return The max login attempts
     */
    public int getMaxLoginAttempts() {
        return maxLoginAttempts;
    }
    
    /**
     * Set the maximum login attempts before lockout.
     * 
     * @param maxLoginAttempts The max login attempts
     */
    public void setMaxLoginAttempts(int maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
    }
    
    /**
     * Get the login timeout in seconds.
     * 
     * @return The login timeout
     */
    public int getLoginTimeout() {
        return loginTimeout;
    }
    
    /**
     * Set the login timeout in seconds.
     * 
     * @param loginTimeout The login timeout
     */
    public void setLoginTimeout(int loginTimeout) {
        this.loginTimeout = loginTimeout;
    }
    
    /**
     * Get the session timeout in minutes.
     * 
     * @return The session timeout
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }
    
    /**
     * Set the session timeout in minutes.
     * 
     * @param sessionTimeout The session timeout
     */
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
