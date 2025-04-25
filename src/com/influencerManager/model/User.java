package com.influencerManager.model;

import java.io.Serializable;

/**
 * Abstract base class representing a user in the Influencer Manager platform.
 * This class provides common attributes and functionality for all types of users.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String username;
    protected String email;
    protected String password;
    protected String role;
    protected boolean isActive;
    protected String creationDate;
    protected String lastLoginDate;
    
    /**
     * Default constructor.
     */
    public User() {
        this.isActive = true;
        this.creationDate = java.time.LocalDate.now().toString();
    }
    
    /**
     * Constructor with basic user information.
     * 
     * @param username The username
     * @param email The email address
     * @param password The password
     */
    public User(String username, String email, String password) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    /**
     * Constructor with all user attributes.
     * 
     * @param username The username
     * @param email The email address
     * @param password The password
     * @param role The user role
     * @param isActive Whether the user is active
     * @param creationDate The account creation date
     * @param lastLoginDate The last login date
     */
    public User(String username, String email, String password, String role, 
               boolean isActive, String creationDate, String lastLoginDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.creationDate = creationDate;
        this.lastLoginDate = lastLoginDate;
    }
    
    // Getters and setters
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    protected void setRole(String role) {
        this.role = role;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    public String getCreationDate() {
        return creationDate;
    }
    
    public String getLastLoginDate() {
        return lastLoginDate;
    }
    
    public void updateLastLoginDate() {
        this.lastLoginDate = java.time.LocalDate.now().toString();
    }
    
    /**
     * Updates user information.
     * 
     * @param email The new email
     * @param password The new password
     */
    public void updateInfo(String email, String password) {
        if (email != null && !email.isEmpty()) {
            this.email = email;
        }
        
        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
    }
    
    /**
     * Abstract method that each user type must implement.
     * 
     * @return A string representation of the user profile
     */
    public abstract String getProfileInfo();
    
    /**
     * Returns a string representation of the User object.
     * 
     * @return String representation of the User
     */
    @Override
    public String toString() {
        return "Username: " + username +
               "\nEmail: " + email +
               "\nRole: " + role + 
               "\nAccount Status: " + (isActive ? "Active" : "Inactive") +
               "\nCreation Date: " + creationDate +
               "\nLast Login: " + (lastLoginDate != null ? lastLoginDate : "Never");
    }
    
    /**
     * Compares this User to another object for equality.
     * 
     * @param obj The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User other = (User) obj;
        return username.equals(other.username);
    }
    
    /**
     * Generates a hash code for this User.
     * 
     * @return A hash code value
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
