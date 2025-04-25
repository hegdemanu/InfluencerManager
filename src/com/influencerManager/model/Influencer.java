package com.influencerManager.model;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Influencer user in the Influencer Manager platform.
 * Influencers have social media profiles and can participate in campaigns.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class Influencer extends User {
    private static final long serialVersionUID = 1L;
    
    private String niche;
    private String bio;
    private double rate;
    private Map<String, SocialMediaProfile> socialMediaProfiles;
    private List<Campaign> activeCampaigns;
    private List<Campaign> pastCampaigns;
    private List<String> contentCategories;
    private int totalCampaigns;
    private double totalEarnings;
    
    /**
     * Nested class for social media profile information.
     * This demonstrates the nested class requirement.
     */
    public static class SocialMediaProfile {
        private String platform;
        private String handle;
        private int followers;
        private int engagementRate;
        private Map<String, Object> metrics;
        
        public SocialMediaProfile(String platform, String handle, int followers) {
            this.platform = platform;
            this.handle = handle;
            this.followers = followers;
            this.engagementRate = 0;
            this.metrics = new HashMap<>();
        }
        
        // Getters and setters
        
        public String getPlatform() {
            return platform;
        }
        
        public String getHandle() {
            return handle;
        }
        
        public void setHandle(String handle) {
            this.handle = handle;
        }
        
        public int getFollowers() {
            return followers;
        }
        
        public void setFollowers(int followers) {
            this.followers = followers;
        }
        
        public int getEngagementRate() {
            return engagementRate;
        }
        
        public void setEngagementRate(int engagementRate) {
            this.engagementRate = engagementRate;
        }
        
        public void addMetric(String name, Object value) {
            metrics.put(name, value);
        }
        
        public Object getMetric(String name) {
            return metrics.get(name);
        }
        
        @Override
        public String toString() {
            return platform + ": @" + handle + " (" + followers + " followers, " + 
                   engagementRate + "% engagement)";
        }
    }
    
    /**
     * Default constructor.
     */
    public Influencer() {
        super();
        this.role = "Influencer";
        this.socialMediaProfiles = new HashMap<>();
        this.activeCampaigns = new ArrayList<>();
        this.pastCampaigns = new ArrayList<>();
        this.contentCategories = new ArrayList<>();
        this.totalCampaigns = 0;
        this.totalEarnings = 0.0;
    }
    
    /**
     * Constructor with basic user information.
     * 
     * @param username The username
     * @param email The email address
     * @param password The password
     */
    public Influencer(String username, String email, String password) {
        super(username, email, password);
        this.role = "Influencer";
        this.socialMediaProfiles = new HashMap<>();
        this.activeCampaigns = new ArrayList<>();
        this.pastCampaigns = new ArrayList<>();
        this.contentCategories = new ArrayList<>();
        this.totalCampaigns = 0;
        this.totalEarnings = 0.0;
    }
    
    /**
     * Constructor with all influencer attributes.
     * 
     * @param username The username
     * @param email The email address
     * @param password The password
     * @param niche The influencer's niche
     * @param bio The influencer's bio
     * @param rate The influencer's rate per post
     */
    public Influencer(String username, String email, String password, 
                     String niche, String bio, double rate) {
        super(username, email, password);
        this.role = "Influencer";
        this.niche = niche;
        this.bio = bio;
        this.rate = rate;
        this.socialMediaProfiles = new HashMap<>();
        this.activeCampaigns = new ArrayList<>();
        this.pastCampaigns = new ArrayList<>();
        this.contentCategories = new ArrayList<>();
        this.totalCampaigns = 0;
        this.totalEarnings = 0.0;
    }
    
    // Getters and setters
    
    public String getNiche() {
        return niche;
    }
    
    public void setNiche(String niche) {
        this.niche = niche;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public double getRate() {
        return rate;
    }
    
    public void setRate(double rate) {
        this.rate = rate;
    }
    
    public List<Campaign> getActiveCampaigns() {
        return activeCampaigns;
    }
    
    public List<Campaign> getPastCampaigns() {
        return pastCampaigns;
    }
    
    public int getTotalCampaigns() {
        return totalCampaigns;
    }
    
    public double getTotalEarnings() {
        return totalEarnings;
    }
    
    public void addContentCategory(String category) {
        if (!contentCategories.contains(category)) {
            contentCategories.add(category);
        }
    }
    
    public List<String> getContentCategories() {
        return contentCategories;
    }
    
    /**
     * Add a social media profile.
     * 
     * @param platform The social media platform
     * @param handle The handle or username on that platform
     * @param followers The number of followers
     */
    public void addSocialMedia(String platform, String handle, int followers) {
        socialMediaProfiles.put(platform.toLowerCase(), 
                               new SocialMediaProfile(platform, handle, followers));
    }
    
    /**
     * Update an existing social media profile.
     * 
     * @param platform The social media platform
     * @param handle The new handle
     * @param followers The new follower count
     * @return true if successful, false if the platform doesn't exist
     */
    public boolean updateSocialMedia(String platform, String handle, int followers) {
        SocialMediaProfile profile = socialMediaProfiles.get(platform.toLowerCase());
        
        if (profile != null) {
            profile.setHandle(handle);
            profile.setFollowers(followers);
            return true;
        }
        
        return false;
    }
    
    /**
     * Remove a social media profile.
     * 
     * @param platform The platform to remove
     * @return true if successful, false if the platform doesn't exist
     */
    public boolean removeSocialMedia(String platform) {
        return socialMediaProfiles.remove(platform.toLowerCase()) != null;
    }
    
    /**
     * Get the handle for a specific platform.
     * 
     * @param platform The platform
     * @return The handle, or null if the platform doesn't exist
     */
    public String getHandle(String platform) {
        SocialMediaProfile profile = socialMediaProfiles.get(platform.toLowerCase());
        return profile != null ? profile.getHandle() : null;
    }
    
    /**
     * Get the follower count for a specific platform.
     * 
     * @param platform The platform
     * @return The follower count, or 0 if the platform doesn't exist
     */
    public int getFollowerCount(String platform) {
        SocialMediaProfile profile = socialMediaProfiles.get(platform.toLowerCase());
        return profile != null ? profile.getFollowers() : 0;
    }
    
    /**
     * Calculate and return the total follower count across all platforms.
     * 
     * @return The total follower count
     */
    public int getTotalFollowers() {
        int total = 0;
        for (SocialMediaProfile profile : socialMediaProfiles.values()) {
            total += profile.getFollowers();
        }
        return total;
    }
    
    /**
     * Display all social media profiles.
     */
    public void displaySocialMediaProfiles() {
        System.out.println("\nSocial Media Profiles:");
        if (socialMediaProfiles.isEmpty()) {
            System.out.println("No social media profiles added yet.");
            return;
        }
        
        for (SocialMediaProfile profile : socialMediaProfiles.values()) {
            System.out.println(profile);
        }
    }
    
    /**
     * Add a campaign to the active campaigns list.
     * 
     * @param campaign The campaign to add
     */
    public void addCampaign(Campaign campaign) {
        if (!activeCampaigns.contains(campaign)) {
            activeCampaigns.add(campaign);
            totalCampaigns++;
        }
    }
    
    /**
     * Complete a campaign and move it to past campaigns.
     * 
     * @param campaign The campaign to complete
     * @param earnings The earnings from the campaign
     */
    public void completeCampaign(Campaign campaign, double earnings) {
        if (activeCampaigns.remove(campaign)) {
            pastCampaigns.add(campaign);
            totalEarnings += earnings;
        }
    }
    
    /**
     * Calculate engagement metrics.
     * 
     * @param platform The platform to calculate metrics for
     * @param likes Number of likes
     * @param comments Number of comments
     * @param shares Number of shares
     */
    public void calculateEngagement(String platform, int likes, int comments, int shares) {
        SocialMediaProfile profile = socialMediaProfiles.get(platform.toLowerCase());
        
        if (profile != null) {
            profile.addMetric("likes", likes);
            profile.addMetric("comments", comments);
            profile.addMetric("shares", shares);
            
            // Simple engagement rate calculation (can be more sophisticated in a real app)
            double engagement = (likes + comments * 2 + shares * 3);
            double engagementRate = (engagement / profile.getFollowers()) * 100;
            profile.setEngagementRate((int) engagementRate);
        }
    }
    
    /**
     * Returns a string representation of the Influencer's profile.
     * 
     * @return String containing the Influencer's profile information
     */
    @Override
    public String getProfileInfo() {
        StringBuilder sb = new StringBuilder(toString());
        
        sb.append("\n\nSocial Media Profiles:");
        for (SocialMediaProfile profile : socialMediaProfiles.values()) {
            sb.append("\n").append(profile);
        }
        
        sb.append("\n\nContent Categories: ");
        sb.append(String.join(", ", contentCategories));
        
        return sb.toString();
    }
    
    /**
     * Returns a string representation of the Influencer object.
     * 
     * @return String representation of the Influencer
     */
    @Override
    public String toString() {
        return super.toString() +
               "\nNiche: " + (niche != null ? niche : "Not specified") +
               "\nBio: " + (bio != null ? bio : "Not specified") +
               "\nRate: $" + rate + " per post" +
               "\nTotal Followers: " + getTotalFollowers() +
               "\nActive Campaigns: " + activeCampaigns.size() +
               "\nCompleted Campaigns: " + pastCampaigns.size() +
               "\nTotal Earnings: $" + String.format("%.2f", totalEarnings);
    }
}
