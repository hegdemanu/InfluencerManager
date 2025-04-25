package com.influencerManager.model;

import java.io.Serializable;
import java.util.*;

import com.influencerManager.interfaces.Manageable;
import com.influencerManager.interfaces.Analyzable;

/**
 * Represents a marketing campaign in the Influencer Manager platform.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class Campaign implements Serializable, Manageable, Analyzable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    private User brand;
    private String description;
    private double budget;
    private String startDate;
    private String endDate;
    private String status;
    private List<Influencer> invitedInfluencers;
    private List<Influencer> acceptedInfluencers;
    private Map<String, String> influencerStatuses;
    private Map<String, List<String>> contentUrls;
    private Map<String, Map<String, Integer>> engagementMetrics;
    private Date createdAt;
    private Date updatedAt;
    
    /**
     * Default constructor.
     */
    public Campaign() {
        this.id = UUID.randomUUID().toString();
        this.status = "Draft";
        this.invitedInfluencers = new ArrayList<>();
        this.acceptedInfluencers = new ArrayList<>();
        this.influencerStatuses = new HashMap<>();
        this.contentUrls = new HashMap<>();
        this.engagementMetrics = new HashMap<>();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    
    /**
     * Constructor with basic campaign information.
     * 
     * @param name The campaign name
     * @param brand The brand running the campaign
     * @param description The campaign description
     */
    public Campaign(String name, User brand, String description) {
        this();
        this.name = name;
        this.brand = brand;
        this.description = description;
    }
    
    /**
     * Constructor with comprehensive campaign information.
     * 
     * @param name The campaign name
     * @param brand The brand running the campaign
     * @param description The campaign description
     * @param budget The campaign budget
     * @param startDate The start date
     * @param endDate The end date
     */
    public Campaign(String name, User brand, String description, 
                  double budget, String startDate, String endDate) {
        this(name, brand, description);
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    // Getters and setters
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
        this.updatedAt = new Date();
    }
    
    public User getBrand() {
        return brand;
    }
    
    public void setBrand(User brand) {
        this.brand = brand;
        this.updatedAt = new Date();
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = new Date();
    }
    
    public double getBudget() {
        return budget;
    }
    
    public void setBudget(double budget) {
        this.budget = budget;
        this.updatedAt = new Date();
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
        this.updatedAt = new Date();
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
        this.updatedAt = new Date();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = new Date();
    }
    
    public List<Influencer> getInvitedInfluencers() {
        return invitedInfluencers;
    }
    
    public List<Influencer> getAcceptedInfluencers() {
        return acceptedInfluencers;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * Invite an influencer to the campaign.
     * 
     * @param influencer The influencer to invite
     */
    public void inviteInfluencer(Influencer influencer) {
        if (!invitedInfluencers.contains(influencer)) {
            invitedInfluencers.add(influencer);
            influencerStatuses.put(influencer.getUsername(), "Invited");
            this.updatedAt = new Date();
        }
    }
    
    /**
     * Accept an influencer's participation in the campaign.
     * 
     * @param influencer The influencer to accept
     */
    public void acceptInfluencer(Influencer influencer) {
        if (invitedInfluencers.contains(influencer) && !acceptedInfluencers.contains(influencer)) {
            acceptedInfluencers.add(influencer);
            influencerStatuses.put(influencer.getUsername(), "Accepted");
            influencer.addCampaign(this);
            this.updatedAt = new Date();
        }
    }
    
    /**
     * Add an influencer directly to the campaign.
     * 
     * @param influencer The influencer to add
     */
    public void addInfluencer(Influencer influencer) {
        invitedInfluencers.add(influencer);
        acceptedInfluencers.add(influencer);
        influencerStatuses.put(influencer.getUsername(), "Accepted");
        influencer.addCampaign(this);
        this.updatedAt = new Date();
    }
    
    /**
     * Remove an influencer from the campaign.
     * 
     * @param influencer The influencer to remove
     * @return true if successful, false otherwise
     */
    public boolean removeInfluencer(Influencer influencer) {
        boolean removed = invitedInfluencers.remove(influencer);
        acceptedInfluencers.remove(influencer);
        influencerStatuses.remove(influencer.getUsername());
        contentUrls.remove(influencer.getUsername());
        engagementMetrics.remove(influencer.getUsername());
        
        if (removed) {
            this.updatedAt = new Date();
        }
        
        return removed;
    }
    
    /**
     * Get the status of an influencer in this campaign.
     * 
     * @param influencer The influencer
     * @return The status string
     */
    public String getInfluencerStatus(Influencer influencer) {
        return influencerStatuses.getOrDefault(influencer.getUsername(), "Not Invited");
    }
    
    /**
     * Update the status of an influencer in this campaign.
     * 
     * @param influencer The influencer
     * @param status The new status
     */
    public void updateInfluencerStatus(Influencer influencer, String status) {
        if (invitedInfluencers.contains(influencer)) {
            influencerStatuses.put(influencer.getUsername(), status);
            this.updatedAt = new Date();
        }
    }
    
    /**
     * Add a content URL for an influencer.
     * 
     * @param influencer The influencer
     * @param url The content URL
     */
    public void addContentUrl(Influencer influencer, String url) {
        if (acceptedInfluencers.contains(influencer)) {
            String username = influencer.getUsername();
            
            if (!contentUrls.containsKey(username)) {
                contentUrls.put(username, new ArrayList<>());
            }
            
            contentUrls.get(username).add(url);
            this.updatedAt = new Date();
        }
    }
    
    /**
     * Get content URLs for an influencer.
     * 
     * @param influencer The influencer
     * @return List of content URLs
     */
    public List<String> getContentUrls(Influencer influencer) {
        return contentUrls.getOrDefault(influencer.getUsername(), new ArrayList<>());
    }
    
    /**
     * Update engagement metrics for an influencer.
     * 
     * @param influencer The influencer
     * @param likes Number of likes
     * @param comments Number of comments
     * @param shares Number of shares
     */
    public void updateEngagementMetrics(Influencer influencer, int likes, int comments, int shares) {
        if (acceptedInfluencers.contains(influencer)) {
            String username = influencer.getUsername();
            
            Map<String, Integer> metrics = new HashMap<>();
            metrics.put("likes", likes);
            metrics.put("comments", comments);
            metrics.put("shares", shares);
            
            engagementMetrics.put(username, metrics);
            this.updatedAt = new Date();
        }
    }
    
    /**
     * Get engagement metrics for an influencer.
     * 
     * @param influencer The influencer
     * @return Map of engagement metrics
     */
    public Map<String, Integer> getEngagementMetrics(Influencer influencer) {
        return engagementMetrics.getOrDefault(influencer.getUsername(), new HashMap<>());
    }
    
    /**
     * Calculate the total engagement for the campaign.
     * 
     * @return Total engagement count
     */
    public int calculateTotalEngagement() {
        int total = 0;
        
        for (Map<String, Integer> metrics : engagementMetrics.values()) {
            total += metrics.getOrDefault("likes", 0);
            total += metrics.getOrDefault("comments", 0);
            total += metrics.getOrDefault("shares", 0);
        }
        
        return total;
    }
    
    /**
     * Calculate the average cost per engagement.
     * 
     * @return Cost per engagement or 0 if no engagement
     */
    public double calculateCostPerEngagement() {
        int totalEngagement = calculateTotalEngagement();
        
        if (totalEngagement > 0) {
            return budget / totalEngagement;
        }
        
        return 0.0;
    }
    
    /**
     * Start the campaign.
     */
    @Override
    public void start() {
        if (status.equals("Draft") || status.equals("Scheduled")) {
            status = "Active";
            this.updatedAt = new Date();
        }
    }
    
    /**
     * Pause the campaign.
     */
    @Override
    public void pause() {
        if (status.equals("Active")) {
            status = "Paused";
            this.updatedAt = new Date();
        }
    }
    
    /**
     * Resume the campaign.
     */
    @Override
    public void resume() {
        if (status.equals("Paused")) {
            status = "Active";
            this.updatedAt = new Date();
        }
    }
    
    /**
     * End the campaign.
     */
    @Override
    public void end() {
        status = "Completed";
        this.updatedAt = new Date();
        
        // In a real app, this would trigger payment processing, etc.
    }
    
    /**
     * Cancel the campaign.
     */
    @Override
    public void cancel() {
        status = "Cancelled";
        this.updatedAt = new Date();
    }
    
    /**
     * Get the performance metrics for the campaign.
     * 
     * @return Map of metrics
     */
    @Override
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("total_influencers", acceptedInfluencers.size());
        metrics.put("total_posts", getTotalPosts());
        metrics.put("total_engagement", calculateTotalEngagement());
        metrics.put("cost_per_engagement", calculateCostPerEngagement());
        metrics.put("total_likes", getTotalMetric("likes"));
        metrics.put("total_comments", getTotalMetric("comments"));
        metrics.put("total_shares", getTotalMetric("shares"));
        
        return metrics;
    }
    
    /**
     * Get total posts across all influencers.
     * 
     * @return Total post count
     */
    private int getTotalPosts() {
        int total = 0;
        
        for (List<String> urls : contentUrls.values()) {
            total += urls.size();
        }
        
        return total;
    }
    
    /**
     * Get total for a specific metric.
     * 
     * @param metricName The metric name
     * @return Total for that metric
     */
    private int getTotalMetric(String metricName) {
        int total = 0;
        
        for (Map<String, Integer> metrics : engagementMetrics.values()) {
            total += metrics.getOrDefault(metricName, 0);
        }
        
        return total;
    }
    
    /**
     * Generate a performance report.
     * 
     * @return Performance report as a string
     */
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        Map<String, Object> metrics = getMetrics();
        
        report.append("Performance Report for Campaign: ").append(name).append("\n");
        report.append("Status: ").append(status).append("\n");
        report.append("Duration: ").append(startDate).append(" to ").append(endDate).append("\n");
        report.append("Budget: $").append(String.format("%.2f", budget)).append("\n\n");
        
        report.append("Overall Metrics:\n");
        report.append("- Total Influencers: ").append(metrics.get("total_influencers")).append("\n");
        report.append("- Total Posts: ").append(metrics.get("total_posts")).append("\n");
        report.append("- Total Engagement: ").append(metrics.get("total_engagement")).append("\n");
        report.append("- Cost Per Engagement: $").append(String.format("%.2f", (double)metrics.get("cost_per_engagement"))).append("\n");
        report.append("- Total Likes: ").append(metrics.get("total_likes")).append("\n");
        report.append("- Total Comments: ").append(metrics.get("total_comments")).append("\n");
        report.append("- Total Shares: ").append(metrics.get("total_shares")).append("\n\n");
        
        report.append("Influencer Performance:\n");
        for (Influencer influencer : acceptedInfluencers) {
            String username = influencer.getUsername();
            report.append("- ").append(username).append(":\n");
            
            List<String> urls = contentUrls.getOrDefault(username, new ArrayList<>());
            report.append("  * Posts: ").append(urls.size()).append("\n");
            
            Map<String, Integer> infMetrics = engagementMetrics.getOrDefault(username, new HashMap<>());
            report.append("  * Likes: ").append(infMetrics.getOrDefault("likes", 0)).append("\n");
            report.append("  * Comments: ").append(infMetrics.getOrDefault("comments", 0)).append("\n");
            report.append("  * Shares: ").append(infMetrics.getOrDefault("shares", 0)).append("\n");
            
            int totalEngagement = infMetrics.getOrDefault("likes", 0) + 
                                infMetrics.getOrDefault("comments", 0) + 
                                infMetrics.getOrDefault("shares", 0);
            report.append("  * Total Engagement: ").append(totalEngagement).append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Returns a string representation of the Campaign object.
     * 
     * @return String representation of the Campaign
     */
    @Override
    public String toString() {
        return "Campaign: " + name +
               "\nID: " + id +
               "\nBrand: " + (brand instanceof Brand ? ((Brand)brand).getCompanyName() : brand.getUsername()) +
               "\nDescription: " + description +
               "\nBudget: $" + String.format("%.2f", budget) +
               "\nDuration: " + (startDate != null ? startDate : "TBD") + 
               " to " + (endDate != null ? endDate : "TBD") +
               "\nStatus: " + status +
               "\nInvited Influencers: " + invitedInfluencers.size() +
               "\nAccepted Influencers: " + acceptedInfluencers.size() +
               "\nTotal Posts: " + getTotalPosts() +
               "\nTotal Engagement: " + calculateTotalEngagement() +
               "\nCreated: " + createdAt +
               "\nLast Updated: " + updatedAt;
    }
    
    /**
     * Overloaded method to format budget with specified decimal places.
     * This demonstrates method overloading requirement.
     * 
     * @param decimalPlaces Number of decimal places
     * @return Formatted budget string
     */
    public String formatBudget(int decimalPlaces) {
        return String.format("%." + decimalPlaces + "f", budget);
    }
    
    /**
     * Overloaded method to format budget with default decimal places (2).
     * This demonstrates method overloading requirement.
     * 
     * @return Formatted budget string
     */
    public String formatBudget() {
        return formatBudget(2);
    }
}
