package com.influencerManager.service;

import com.influencerManager.model.*;
import com.influencerManager.exception.DataProcessingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class to handle campaign-related operations.
 * This class manages all campaign entities and provides operations like add, find, update, and delete.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class CampaignService {
    private Map<String, Campaign> campaigns;
    
    /**
     * Default constructor.
     */
    public CampaignService() {
        this.campaigns = new HashMap<>();
    }
    
    /**
     * Constructor with initial campaign set.
     * 
     * @param initialCampaigns List of campaigns to initialize with
     */
    public CampaignService(List<Campaign> initialCampaigns) {
        this();
        if (initialCampaigns != null) {
            for (Campaign campaign : initialCampaigns) {
                addCampaign(campaign);
            }
        }
    }
    
    /**
     * Add a campaign to the service.
     * 
     * @param campaign The campaign to add
     * @return true if successful, false if the campaign ID already exists
     */
    public boolean addCampaign(Campaign campaign) {
        if (campaign == null) {
            return false;
        }
        
        String id = campaign.getId();
        
        if (campaigns.containsKey(id)) {
            return false;
        }
        
        campaigns.put(id, campaign);
        return true;
    }
    
    /**
     * Get a campaign by ID.
     * 
     * @param id The campaign ID to search for
     * @return The campaign if found, null otherwise
     */
    public Campaign getCampaignById(String id) {
        return campaigns.get(id);
    }
    
    /**
     * Delete a campaign by ID.
     * 
     * @param id The ID of the campaign to delete
     * @return true if successful, false if campaign not found
     */
    public boolean deleteCampaign(String id) {
        return campaigns.remove(id) != null;
    }
    
    /**
     * Update a campaign's information.
     * 
     * @param campaign The campaign with updated information
     * @return true if successful, false if campaign not found
     */
    public boolean updateCampaign(Campaign campaign) {
        if (campaign == null || !campaigns.containsKey(campaign.getId())) {
            return false;
        }
        
        campaigns.put(campaign.getId(), campaign);
        return true;
    }
    
    /**
     * Get all campaigns in the system.
     * 
     * @return List of all campaigns
     */
    public List<Campaign> getAllCampaigns() {
        return new ArrayList<>(campaigns.values());
    }
    
    /**
     * Get campaigns for a specific brand.
     * 
     * @param brand The brand to filter by
     * @return List of campaigns for the brand
     */
    public List<Campaign> getCampaignsForBrand(Brand brand) {
        if (brand == null) {
            return new ArrayList<>();
        }
        
        return campaigns.values().stream()
            .filter(c -> c.getBrand() != null && c.getBrand().equals(brand))
            .collect(Collectors.toList());
    }
    
    /**
     * Get campaigns for a specific influencer.
     * 
     * @param influencer The influencer to filter by
     * @return List of campaigns for the influencer
     */
    public List<Campaign> getCampaignsForInfluencer(Influencer influencer) {
        if (influencer == null) {
            return new ArrayList<>();
        }
        
        return campaigns.values().stream()
            .filter(c -> c.getAcceptedInfluencers().contains(influencer))
            .collect(Collectors.toList());
    }
    
    /**
     * Get campaign offers for a specific influencer.
     * These are campaigns where the influencer is invited but not yet accepted.
     * 
     * @param influencer The influencer to filter by
     * @return List of campaign offers for the influencer
     */
    public List<Campaign> getCampaignOffersForInfluencer(Influencer influencer) {
        if (influencer == null) {
            return new ArrayList<>();
        }
        
        return campaigns.values().stream()
            .filter(c -> 
                c.getInvitedInfluencers().contains(influencer) && 
                !c.getAcceptedInfluencers().contains(influencer))
            .collect(Collectors.toList());
    }
    
    /**
     * Get campaigns with a specific status.
     * 
     * @param status The status to filter by
     * @return List of campaigns with the status
     */
    public List<Campaign> getCampaignsByStatus(String status) {
        if (status == null || status.isEmpty()) {
            return new ArrayList<>();
        }
        
        return campaigns.values().stream()
            .filter(c -> c.getStatus().equalsIgnoreCase(status))
            .collect(Collectors.toList());
    }
    
    /**
     * Get active campaigns.
     * 
     * @return List of active campaigns
     */
    public List<Campaign> getActiveCampaigns() {
        return getCampaignsByStatus("Active");
    }
    
    /**
     * Get completed campaigns.
     * 
     * @return List of completed campaigns
     */
    public List<Campaign> getCompletedCampaigns() {
        return getCampaignsByStatus("Completed");
    }
    
    /**
     * Get campaign count.
     * 
     * @return The number of campaigns
     */
    public int getCampaignCount() {
        return campaigns.size();
    }
    
    /**
     * Set the entire campaign database (replaces existing campaigns).
     * 
     * @param campaignList The new list of campaigns
     * @throws DataProcessingException If there's an issue with the data
     */
    public void setAllCampaigns(List<Campaign> campaignList) throws DataProcessingException {
        if (campaignList == null) {
            throw new DataProcessingException("Campaign list cannot be null");
        }
        
        // Reset the campaigns map
        campaigns.clear();
        
        // Add all campaigns from the list
        for (Campaign campaign : campaignList) {
            if (campaign.getId() == null || campaign.getId().isEmpty()) {
                throw new DataProcessingException("Campaign with empty ID found");
            }
            
            campaigns.put(campaign.getId(), campaign);
        }
    }
    
    /**
     * Get campaigns in a specific date range.
     * 
     * @param startDate Start date in "YYYY-MM-DD" format
     * @param endDate End date in "YYYY-MM-DD" format
     * @return List of campaigns in the date range
     */
    public List<Campaign> getCampaignsInDateRange(String startDate, String endDate) {
        if (startDate == null || endDate == null) {
            return new ArrayList<>();
        }
        
        return campaigns.values().stream()
            .filter(c -> {
                // Very simple date comparison (YYYY-MM-DD format)
                // In a real app, would use proper date parsing
                String campaignStart = c.getStartDate();
                String campaignEnd = c.getEndDate();
                
                if (campaignStart == null || campaignEnd == null) {
                    return false;
                }
                
                return campaignStart.compareTo(startDate) >= 0 && campaignEnd.compareTo(endDate) <= 0;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Get campaigns by name (partial match).
     * 
     * @param name The name to search for
     * @return List of campaigns with matching names
     */
    public List<Campaign> getCampaignsByName(String name) {
        if (name == null || name.isEmpty()) {
            return new ArrayList<>();
        }
        
        return campaigns.values().stream()
            .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get campaigns by budget range.
     * 
     * @param minBudget Minimum budget
     * @param maxBudget Maximum budget
     * @return List of campaigns in the budget range
     */
    public List<Campaign> getCampaignsByBudgetRange(double minBudget, double maxBudget) {
        return campaigns.values().stream()
            .filter(c -> c.getBudget() >= minBudget && c.getBudget() <= maxBudget)
            .collect(Collectors.toList());
    }
    
    /**
     * Get campaigns with high engagement.
     * 
     * @param engagementThreshold Minimum engagement count to be considered "high"
     * @return List of campaigns with high engagement
     */
    public List<Campaign> getCampaignsWithHighEngagement(int engagementThreshold) {
        return campaigns.values().stream()
            .filter(c -> c.calculateTotalEngagement() >= engagementThreshold)
            .collect(Collectors.toList());
    }
    
    /**
     * Get the top performing campaigns.
     * 
     * @param limit Maximum number of campaigns to return
     * @return List of top performing campaigns
     */
    public List<Campaign> getTopPerformingCampaigns(int limit) {
        return campaigns.values().stream()
            .sorted((c1, c2) -> Integer.compare(c2.calculateTotalEngagement(), c1.calculateTotalEngagement()))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Remove all campaigns.
     */
    public void clearAllCampaigns() {
        campaigns.clear();
    }
}
