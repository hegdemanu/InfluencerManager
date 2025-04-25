package com.influencerManager.service;

import com.influencerManager.model.*;

import java.util.*;

/**
 * Service class to provide AI-driven recommendations for influencers and campaigns.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class RecommendationService {
    private UserService userService;
    private CampaignService campaignService;
    private Random random;  // For simulating AI behavior in demo
    
    /**
     * Default constructor.
     */
    public RecommendationService() {
        this.random = new Random();
    }
    
    /**
     * Constructor with dependent services.
     * 
     * @param userService The user service
     * @param campaignService The campaign service
     */
    public RecommendationService(UserService userService, CampaignService campaignService) {
        this();
        this.userService = userService;
        this.campaignService = campaignService;
    }
    
    /**
     * Set the user service.
     * 
     * @param userService The user service
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Set the campaign service.
     * 
     * @param campaignService The campaign service
     */
    public void setCampaignService(CampaignService campaignService) {
        this.campaignService = campaignService;
    }
    
    /**
     * Get recommended influencers for a campaign.
     * 
     * @param campaign The campaign
     * @return List of recommended influencers
     */
    public List<Influencer> getRecommendedInfluencers(Campaign campaign) {
        if (userService == null || campaign == null) {
            return new ArrayList<>();
        }
        
        List<Influencer> allInfluencers = userService.getAllInfluencers();
        List<Influencer> recommendations = new ArrayList<>();
        
        // Get the brand to check for budget constraints
        User brandUser = campaign.getBrand();
        if (!(brandUser instanceof Brand)) {
            return recommendations;
        }
        
        Brand brand = (Brand) brandUser;
        double campaignBudget = campaign.getBudget();
        
        // In a real recommendation engine, would use ML algorithms
        // For demo, use a simple matching algorithm based on:
        // 1. Industry/niche match
        // 2. Budget constraints
        // 3. Previous collaboration success
        
        // Get the brand's industry
        String brandIndustry = brand.getIndustry();
        
        for (Influencer influencer : allInfluencers) {
            int score = 0;
            
            // Skip already invited influencers
            if (campaign.getInvitedInfluencers().contains(influencer)) {
                continue;
            }
            
            // 1. Industry/niche match (highest weight)
            if (brandIndustry != null && influencer.getNiche() != null) {
                if (brandIndustry.equalsIgnoreCase(influencer.getNiche())) {
                    score += 100; // Perfect match
                } else if (isRelatedNiche(brandIndustry, influencer.getNiche())) {
                    score += 50; // Related match
                }
            }
            
            // 2. Budget constraints
            if (influencer.getRate() <= campaignBudget * 0.2) { // Assume max 20% of budget per influencer
                score += 50;
            } else if (influencer.getRate() <= campaignBudget * 0.4) {
                score += 25;
            }
            
            // 3. Follower count (higher is better but diminishing returns)
            int followers = influencer.getTotalFollowers();
            if (followers > 1000000) { // 1M+
                score += 40;
            } else if (followers > 100000) { // 100K+
                score += 30;
            } else if (followers > 10000) { // 10K+
                score += 20;
            } else {
                score += 10;
            }
            
            // 4. Previous collaboration (if any)
            if (hasPreviousCollaboration(brand, influencer)) {
                score += 30;
            }
            
            // Add some randomness to simulate AI variability
            score += random.nextInt(20);
            
            // Store score with influencer for sorting
            influencer.getClass(); // Just to use the variable
            ScoreInfluencer scoredInfluencer = new ScoreInfluencer(influencer, score);
            
            // Add to recommendations if score is high enough
            if (score >= 50) {
                recommendations.add(scoredInfluencer.influencer);
            }
        }
        
        // Sort by score (highest first) and limit to top 10
        recommendations.sort((i1, i2) -> {
            ScoreInfluencer si1 = new ScoreInfluencer(i1, getInfluencerScore(i1, campaign));
            ScoreInfluencer si2 = new ScoreInfluencer(i2, getInfluencerScore(i2, campaign));
            return Integer.compare(si2.score, si1.score);
        });
        
        // Limit to top 10
        int limit = Math.min(10, recommendations.size());
        return recommendations.subList(0, limit);
    }
    
    /**
     * Get recommended campaigns for an influencer.
     * 
     * @param influencer The influencer
     * @return List of recommended campaigns
     */
    public List<Campaign> getRecommendedCampaigns(Influencer influencer) {
        if (campaignService == null || influencer == null) {
            return new ArrayList<>();
        }
        
        List<Campaign> allCampaigns = campaignService.getAllCampaigns();
        List<Campaign> recommendations = new ArrayList<>();
        
        // For each campaign, calculate match score
        for (Campaign campaign : allCampaigns) {
            // Skip campaigns where influencer is already invited or accepted
            if (campaign.getInvitedInfluencers().contains(influencer)) {
                continue;
            }
            
            // Skip completed or cancelled campaigns
            if (campaign.getStatus().equals("Completed") || campaign.getStatus().equals("Cancelled")) {
                continue;
            }
            
            int score = 0;
            
            // Get the brand
            User brandUser = campaign.getBrand();
            if (!(brandUser instanceof Brand)) {
                continue;
            }
            
            Brand brand = (Brand) brandUser;
            
            // 1. Industry/niche match
            if (brand.getIndustry() != null && influencer.getNiche() != null) {
                if (brand.getIndustry().equalsIgnoreCase(influencer.getNiche())) {
                    score += 100; // Perfect match
                } else if (isRelatedNiche(brand.getIndustry(), influencer.getNiche())) {
                    score += 50; // Related match
                }
            }
            
            // 2. Budget match
            if (influencer.getRate() <= campaign.getBudget() * 0.2) {
                score += 50;
            } else if (influencer.getRate() <= campaign.getBudget() * 0.4) {
                score += 25;
            }
            
            // 3. Previous collaboration
            if (hasPreviousCollaboration(brand, influencer)) {
                score += 30;
            }
            
            // Add some randomness
            score += random.nextInt(20);
            
            // Add to recommendations if score is high enough
            if (score >= 50) {
                recommendations.add(campaign);
            }
        }
        
        // Sort by score and limit to top 10
        recommendations.sort((c1, c2) -> {
            int score1 = getCampaignScore(c1, influencer);
            int score2 = getCampaignScore(c2, influencer);
            return Integer.compare(score2, score1); // Higher score first
        });
        
        // Limit to top 10
        int limit = Math.min(10, recommendations.size());
        return recommendations.subList(0, limit);
    }
    
    /**
     * Check if two niches/industries are related.
     * 
     * @param industry1 First industry
     * @param industry2 Second industry
     * @return true if related, false otherwise
     */
    private boolean isRelatedNiche(String industry1, String industry2) {
        // In a real app, would use a more sophisticated algorithm or database of related industries
        
        // Convert to lowercase for case-insensitive comparison
        industry1 = industry1.toLowerCase();
        industry2 = industry2.toLowerCase();
        
        // Define some related pairs
        Map<String, List<String>> relatedIndustries = new HashMap<>();
        relatedIndustries.put("fashion", Arrays.asList("clothing", "beauty", "accessories", "lifestyle"));
        relatedIndustries.put("technology", Arrays.asList("electronics", "gadgets", "software", "gaming"));
        relatedIndustries.put("food", Arrays.asList("cooking", "restaurant", "beverages", "nutrition"));
        relatedIndustries.put("fitness", Arrays.asList("health", "sports", "wellness", "nutrition"));
        relatedIndustries.put("travel", Arrays.asList("tourism", "hospitality", "adventure", "lifestyle"));
        
        // Check if either industry is related to the other
        for (Map.Entry<String, List<String>> entry : relatedIndustries.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            
            // Check if industry1 is the key and industry2 is in the values
            if (industry1.contains(key) && (values.stream().anyMatch(industry2::contains) || industry2.contains(key))) {
                return true;
            }
            
            // Check if industry2 is the key and industry1 is in the values
            if (industry2.contains(key) && (values.stream().anyMatch(industry1::contains) || industry1.contains(key))) {
                return true;
            }
            
            // Check if both are in the values list
            for (String value : values) {
                if (industry1.contains(value) && industry2.contains(value)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Check if an influencer has previously collaborated with a brand.
     * 
     * @param brand The brand
     * @param influencer The influencer
     * @return true if they have collaborated, false otherwise
     */
    private boolean hasPreviousCollaboration(Brand brand, Influencer influencer) {
        if (campaignService == null) {
            return false;
        }
        
        // Get all completed campaigns for this brand
        List<Campaign> brandCampaigns = campaignService.getCampaignsForBrand(brand);
        
        // Check if the influencer was part of any of these campaigns
        for (Campaign campaign : brandCampaigns) {
            if (campaign.getAcceptedInfluencers().contains(influencer)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Calculate a match score for an influencer and campaign.
     * 
     * @param influencer The influencer
     * @param campaign The campaign
     * @return The match score
     */
    private int getInfluencerScore(Influencer influencer, Campaign campaign) {
        if (campaign == null || influencer == null) {
            return 0;
        }
        
        int score = 0;
        
        // Get the brand
        User brandUser = campaign.getBrand();
        if (!(brandUser instanceof Brand)) {
            return score;
        }
        
        Brand brand = (Brand) brandUser;
        
        // 1. Industry/niche match
        if (brand.getIndustry() != null && influencer.getNiche() != null) {
            if (brand.getIndustry().equalsIgnoreCase(influencer.getNiche())) {
                score += 100; // Perfect match
            } else if (isRelatedNiche(brand.getIndustry(), influencer.getNiche())) {
                score += 50; // Related match
            }
        }
        
        // 2. Budget match
        if (influencer.getRate() <= campaign.getBudget() * 0.2) {
            score += 50;
        } else if (influencer.getRate() <= campaign.getBudget() * 0.4) {
            score += 25;
        }
        
        // 3. Follower count
        int followers = influencer.getTotalFollowers();
        if (followers > 1000000) { // 1M+
            score += 40;
        } else if (followers > 100000) { // 100K+
            score += 30;
        } else if (followers > 10000) { // 10K+
            score += 20;
        } else {
            score += 10;
        }
        
        // 4. Previous collaboration
        if (hasPreviousCollaboration(brand, influencer)) {
            score += 30;
        }
        
        // Add some randomness
        score += random.nextInt(20);
        
        return score;
    }
    
    /**
     * Calculate a match score for a campaign and influencer.
     * 
     * @param campaign The campaign
     * @param influencer The influencer
     * @return The match score
     */
    private int getCampaignScore(Campaign campaign, Influencer influencer) {
        return getInfluencerScore(influencer, campaign);
    }
    
    /**
     * Recommend an optimal budget for a campaign.
     * 
     * @param brand The brand
     * @param campaignType The type of campaign
     * @param targetInfluencers The number of influencers to target
     * @return The recommended budget
     */
    public double recommendCampaignBudget(Brand brand, String campaignType, int targetInfluencers) {
        if (userService == null) {
            return 0.0;
        }
        
        // Get average rate for influencers in the brand's industry
        String industry = brand.getIndustry();
        List<Influencer> relevantInfluencers = userService.getInfluencersByNiche(industry);
        
        if (relevantInfluencers.isEmpty()) {
            // If no direct matches, get all influencers
            relevantInfluencers = userService.getAllInfluencers();
        }
        
        double averageRate = relevantInfluencers.stream()
            .mapToDouble(Influencer::getRate)
            .average()
            .orElse(500.0); // Default if no data
        
        // Adjust based on campaign type
        double multiplier = 1.0;
        switch (campaignType.toLowerCase()) {
            case "product launch":
                multiplier = 2.0; // Higher budget for product launches
                break;
            case "awareness":
                multiplier = 1.5; // Medium budget for awareness
                break;
            case "engagement":
                multiplier = 1.2; // Slightly higher for engagement
                break;
            default:
                multiplier = 1.0; // Default for other types
        }
        
        // Calculate recommended budget
        double recommendedBudget = averageRate * targetInfluencers * multiplier;
        
        // Add buffer for additional expenses (20%)
        recommendedBudget *= 1.2;
        
        return recommendedBudget;
    }
    
    /**
     * Recommend the best social media platforms for a campaign.
     * 
     * @param brand The brand
     * @param targetAudience The target audience description
     * @return List of recommended platforms
     */
    public List<String> recommendPlatforms(Brand brand, String targetAudience) {
        List<String> recommendations = new ArrayList<>();
        
        // In a real app, would use analytics data and ML
        // For demo, use simple rules based on industry and target audience
        
        String industry = brand.getIndustry();
        if (industry == null) {
            industry = "";
        }
        
        if (targetAudience == null) {
            targetAudience = "";
        }
        
        industry = industry.toLowerCase();
        targetAudience = targetAudience.toLowerCase();
        
        // Add Instagram for most industries
        recommendations.add("Instagram");
        
        // Add platforms based on industry
        if (industry.contains("fashion") || industry.contains("beauty") || 
            industry.contains("lifestyle") || industry.contains("food")) {
            recommendations.add("TikTok");
            recommendations.add("Pinterest");
        }
        
        if (industry.contains("tech") || industry.contains("gaming") || 
            industry.contains("software") || industry.contains("business")) {
            recommendations.add("Twitter");
            recommendations.add("LinkedIn");
            recommendations.add("YouTube");
        }
        
        if (industry.contains("entertainment") || industry.contains("music")) {
            recommendations.add("TikTok");
            recommendations.add("YouTube");
            recommendations.add("Twitch");
        }
        
        // Add platforms based on target audience
        if (targetAudience.contains("young") || targetAudience.contains("teen") || 
            targetAudience.contains("gen z")) {
            recommendations.add("TikTok");
            recommendations.add("Snapchat");
            
            // Remove LinkedIn for very young audiences
            recommendations.remove("LinkedIn");
        }
        
        if (targetAudience.contains("professional") || targetAudience.contains("business") || 
            targetAudience.contains("corporate")) {
            recommendations.add("LinkedIn");
            recommendations.add("Twitter");
            
            // Remove Snapchat for professional audiences
            recommendations.remove("Snapchat");
        }
        
        // Remove duplicates
        recommendations = new ArrayList<>(new LinkedHashSet<>(recommendations));
        
        return recommendations;
    }
    
    /**
     * Inner class to pair an influencer with a score for sorting.
     */
    private static class ScoreInfluencer {
        Influencer influencer;
        int score;
        
        ScoreInfluencer(Influencer influencer, int score) {
            this.influencer = influencer;
            this.score = score;
        }
    }
}
