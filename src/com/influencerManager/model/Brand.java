package com.influencerManager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Brand user in the Influencer Manager platform.
 * Brands can create campaigns and partner with influencers.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class Brand extends User {
    private static final long serialVersionUID = 1L;
    
    private String companyName;
    private String industry;
    private String website;
    private String description;
    private double budget;
    private List<Campaign> activeCampaigns;
    private List<Campaign> pastCampaigns;
    private int totalCampaigns;
    private double totalSpent;
    
    /**
     * Default constructor.
     */
    public Brand() {
        super();
        this.role = "Brand";
        this.activeCampaigns = new ArrayList<>();
        this.pastCampaigns = new ArrayList<>();
        this.totalCampaigns = 0;
        this.totalSpent = 0.0;
    }
    
    /**
     * Constructor with basic user information.
     * 
     * @param username The username
     * @param email The email address
     * @param password The password
     */
    public Brand(String username, String email, String password) {
        super(username, email, password);
        this.role = "Brand";
        this.activeCampaigns = new ArrayList<>();
        this.pastCampaigns = new ArrayList<>();
        this.totalCampaigns = 0;
        this.totalSpent = 0.0;
    }
    
    /**
     * Constructor with all brand attributes.
     * 
     * @param username The username
     * @param email The email address
     * @param password The password
     * @param companyName The company name
     * @param industry The industry
     * @param website The website
     * @param description The brand description
     * @param budget The marketing budget
     */
    public Brand(String username, String email, String password, 
                String companyName, String industry, String website, 
                String description, double budget) {
        super(username, email, password);
        this.role = "Brand";
        this.companyName = companyName;
        this.industry = industry;
        this.website = website;
        this.description = description;
        this.budget = budget;
        this.activeCampaigns = new ArrayList<>();
        this.pastCampaigns = new ArrayList<>();
        this.totalCampaigns = 0;
        this.totalSpent = 0.0;
    }
    
    // Getters and setters
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getBudget() {
        return budget;
    }
    
    public void setBudget(double budget) {
        this.budget = budget;
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
    
    public double getTotalSpent() {
        return totalSpent;
    }
    
    /**
     * Create a new campaign.
     * 
     * @param name The campaign name
     * @param description The campaign description
     * @param budget The campaign budget
     * @param startDate The start date
     * @param endDate The end date
     * @return The created Campaign object
     */
    public Campaign createCampaign(String name, String description, double budget, 
                                 String startDate, String endDate) {
        Campaign campaign = new Campaign(name, this, description);
        campaign.setBudget(budget);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        
        activeCampaigns.add(campaign);
        totalCampaigns++;
        
        return campaign;
    }
    
    /**
     * Add an existing campaign to this brand's list.
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
     * @param spent The amount spent on the campaign
     */
    public void completeCampaign(Campaign campaign, double spent) {
        if (activeCampaigns.remove(campaign)) {
            pastCampaigns.add(campaign);
            totalSpent += spent;
        }
    }
    
    /**
     * Calculate remaining budget.
     * 
     * @return The remaining budget
     */
    public double getRemainingBudget() {
        double allocated = 0.0;
        for (Campaign campaign : activeCampaigns) {
            allocated += campaign.getBudget();
        }
        return budget - allocated;
    }
    
    /**
     * Returns a string representation of the Brand's profile.
     * 
     * @return String containing the Brand's profile information
     */
    @Override
    public String getProfileInfo() {
        return toString();
    }
    
    /**
     * Returns a string representation of the Brand object.
     * 
     * @return String representation of the Brand
     */
    @Override
    public String toString() {
        return super.toString() +
               "\nCompany Name: " + (companyName != null ? companyName : "Not specified") +
               "\nIndustry: " + (industry != null ? industry : "Not specified") +
               "\nWebsite: " + (website != null ? website : "Not specified") +
               "\nDescription: " + (description != null ? description : "Not specified") +
               "\nTotal Budget: $" + String.format("%.2f", budget) +
               "\nRemaining Budget: $" + String.format("%.2f", getRemainingBudget()) +
               "\nActive Campaigns: " + activeCampaigns.size() +
               "\nCompleted Campaigns: " + pastCampaigns.size() +
               "\nTotal Spent: $" + String.format("%.2f", totalSpent);
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
