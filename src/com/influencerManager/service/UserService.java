package com.influencerManager.service;

import com.influencerManager.model.*;
import com.influencerManager.exception.DataProcessingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class to handle user-related operations.
 * This class manages all user entities and provides operations like add, find, update, and delete.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class UserService {
    private Map<String, User> users;
    
    /**
     * Default constructor.
     */
    public UserService() {
        this.users = new HashMap<>();
    }
    
    /**
     * Constructor with initial user set.
     * 
     * @param initialUsers List of users to initialize with
     */
    public UserService(List<User> initialUsers) {
        this();
        if (initialUsers != null) {
            for (User user : initialUsers) {
                addUser(user);
            }
        }
    }
    
    /**
     * Add a user to the service.
     * 
     * @param user The user to add
     * @return true if successful, false if the username already exists
     */
    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }
        
        String username = user.getUsername();
        
        if (users.containsKey(username)) {
            return false;
        }
        
        users.put(username, user);
        return true;
    }
    
    /**
     * Get a user by username.
     * 
     * @param username The username to search for
     * @return The user if found, null otherwise
     */
    public User getUserByUsername(String username) {
        return users.get(username);
    }
    
    /**
     * Find users by email.
     * 
     * @param email The email to search for
     * @return List of users with the given email
     */
    public List<User> findUsersByEmail(String email) {
        return users.values().stream()
            .filter(u -> u.getEmail().equals(email))
            .collect(Collectors.toList());
    }
    
    /**
     * Delete a user by username.
     * 
     * @param username The username of the user to delete
     * @return true if successful, false if user not found
     */
    public boolean deleteUser(String username) {
        return users.remove(username) != null;
    }
    
    /**
     * Update a user's information.
     * 
     * @param username The username of the user to update
     * @param email The new email (optional)
     * @param password The new password (optional)
     * @return true if successful, false if user not found
     */
    public boolean updateUser(String username, String email, String password) {
        User user = users.get(username);
        
        if (user != null) {
            user.updateInfo(email, password);
            return true;
        }
        
        return false;
    }
    
    /**
     * Get all users in the system.
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    /**
     * Get all users of a specific type.
     * 
     * @param userType The class type to filter by
     * @return List of users of the specified type
     */
    public <T extends User> List<T> getUsersByType(Class<T> userType) {
        return users.values().stream()
            .filter(userType::isInstance)
            .map(userType::cast)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all influencers in the system.
     * 
     * @return List of all influencers
     */
    public List<Influencer> getAllInfluencers() {
        return getUsersByType(Influencer.class);
    }
    
    /**
     * Get all brands in the system.
     * 
     * @return List of all brands
     */
    public List<Brand> getAllBrands() {
        return getUsersByType(Brand.class);
    }
    
    /**
     * Get all advertisers in the system.
     * 
     * @return List of all advertisers
     */
    public List<Advertiser> getAllAdvertisers() {
        return getUsersByType(Advertiser.class);
    }
    
    /**
     * Get all admins in the system.
     * 
     * @return List of all admins
     */
    public List<Admin> getAllAdmins() {
        return getUsersByType(Admin.class);
    }
    
    /**
     * Search for influencers based on criteria.
     * 
     * @param niche Niche category (optional)
     * @param minFollowers Minimum follower count (optional)
     * @param maxRate Maximum rate per post (optional)
     * @return List of matching influencers
     */
    public List<Influencer> searchInfluencers(String niche, int minFollowers, double maxRate) {
        List<Influencer> influencers = getAllInfluencers();
        List<Influencer> results = new ArrayList<>();
        
        for (Influencer influencer : influencers) {
            boolean matches = true;
            
            // Check niche if provided
            if (niche != null && !niche.isEmpty()) {
                if (influencer.getNiche() == null || !influencer.getNiche().toLowerCase().contains(niche.toLowerCase())) {
                    matches = false;
                }
            }
            
            // Check minimum followers if provided
            if (minFollowers > 0) {
                if (influencer.getTotalFollowers() < minFollowers) {
                    matches = false;
                }
            }
            
            // Check maximum rate if provided
            if (maxRate < Double.MAX_VALUE) {
                if (influencer.getRate() > maxRate) {
                    matches = false;
                }
            }
            
            if (matches) {
                results.add(influencer);
            }
        }
        
        return results;
    }
    
    /**
     * Get number of users in the system.
     * 
     * @return The user count
     */
    public int getUserCount() {
        return users.size();
    }
    
    /**
     * Check if a username exists.
     * 
     * @param username The username to check
     * @return true if it exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return users.containsKey(username);
    }
    
    /**
     * Check if an email is already in use.
     * 
     * @param email The email to check
     * @return true if in use, false otherwise
     */
    public boolean emailInUse(String email) {
        return users.values().stream()
            .anyMatch(u -> u.getEmail().equals(email));
    }
    
    /**
     * Set the entire user database (replaces existing users).
     * 
     * @param userList The new list of users
     * @throws DataProcessingException If there's an issue with the data
     */
    public void setAllUsers(List<User> userList) throws DataProcessingException {
        if (userList == null) {
            throw new DataProcessingException("User list cannot be null");
        }
        
        // Reset the users map
        users.clear();
        
        // Add all users from the list
        for (User user : userList) {
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                throw new DataProcessingException("User with empty username found");
            }
            
            users.put(user.getUsername(), user);
        }
    }
    
    /**
     * Retrieve influencers in a specific niche.
     * 
     * @param niche The niche to search for
     * @return List of influencers in that niche
     */
    public List<Influencer> getInfluencersByNiche(String niche) {
        if (niche == null || niche.isEmpty()) {
            return new ArrayList<>();
        }
        
        return getAllInfluencers().stream()
            .filter(i -> i.getNiche() != null && i.getNiche().toLowerCase().contains(niche.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    /**
     * Retrieve brands in a specific industry.
     * 
     * @param industry The industry to search for
     * @return List of brands in that industry
     */
    public List<Brand> getBrandsByIndustry(String industry) {
        if (industry == null || industry.isEmpty()) {
            return new ArrayList<>();
        }
        
        return getAllBrands().stream()
            .filter(b -> b.getIndustry() != null && b.getIndustry().toLowerCase().contains(industry.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get top influencers by follower count.
     * 
     * @param limit Maximum number of influencers to return
     * @return List of top influencers
     */
    public List<Influencer> getTopInfluencers(int limit) {
        return getAllInfluencers().stream()
            .sorted((i1, i2) -> Integer.compare(i2.getTotalFollowers(), i1.getTotalFollowers()))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Get top brands by budget.
     * 
     * @param limit Maximum number of brands to return
     * @return List of top brands
     */
    public List<Brand> getTopBrands(int limit) {
        return getAllBrands().stream()
            .sorted((b1, b2) -> Double.compare(b2.getBudget(), b1.getBudget()))
            .limit(limit)
            .collect(Collectors.toList());
    }
}
