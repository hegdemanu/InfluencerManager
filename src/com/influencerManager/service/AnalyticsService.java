package com.influencerManager.service;

import com.influencerManager.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Service class to handle analytics and reporting operations.
 * This class provides methods for analyzing campaign performance,
 * user statistics, and generating various reports.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class AnalyticsService {
    private UserService userService;
    private CampaignService campaignService;
    
    /**
     * Default constructor.
     */
    public AnalyticsService() {
        // Default constructor for when services will be provided later
    }
    
    /**
     * Constructor with dependent services.
     * 
     * @param userService The user service
     * @param campaignService The campaign service
     */
    public AnalyticsService(UserService userService, CampaignService campaignService) {
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
     * Display campaign performance metrics.
     * 
     * @param campaign The campaign to analyze
     */
    public void displayCampaignPerformance(Campaign campaign) {
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }
        
        System.out.println("\n===== Campaign Performance: " + campaign.getName() + " =====");
        Map<String, Object> metrics = campaign.getMetrics();
        
        System.out.println("Total Influencers: " + metrics.get("total_influencers"));
        System.out.println("Total Posts: " + metrics.get("total_posts"));
        System.out.println("Total Engagement: " + metrics.get("total_engagement"));
        System.out.println("Cost Per Engagement: $" + String.format("%.4f", (double)metrics.get("cost_per_engagement")));
        System.out.println("Total Likes: " + metrics.get("total_likes"));
        System.out.println("Total Comments: " + metrics.get("total_comments"));
        System.out.println("Total Shares: " + metrics.get("total_shares"));
        
        System.out.println("\nInfluencer Performance:");
        for (Influencer influencer : campaign.getAcceptedInfluencers()) {
            System.out.println("\n" + influencer.getUsername() + ":");
            
            // Display content URLs
            List<String> urls = campaign.getContentUrls(influencer);
            if (!urls.isEmpty()) {
                System.out.println("Content URLs:");
                for (String url : urls) {
                    System.out.println("- " + url);
                }
            } else {
                System.out.println("No content posted yet");
            }
            
            // Display engagement metrics
            Map<String, Integer> engagementMetrics = campaign.getEngagementMetrics(influencer);
            if (!engagementMetrics.isEmpty()) {
                System.out.println("Engagement Metrics:");
                System.out.println("- Likes: " + engagementMetrics.getOrDefault("likes", 0));
                System.out.println("- Comments: " + engagementMetrics.getOrDefault("comments", 0));
                System.out.println("- Shares: " + engagementMetrics.getOrDefault("shares", 0));
                
                int totalEngagement = engagementMetrics.getOrDefault("likes", 0) + 
                                    engagementMetrics.getOrDefault("comments", 0) + 
                                    engagementMetrics.getOrDefault("shares", 0);
                
                System.out.println("- Total Engagement: " + totalEngagement);
            } else {
                System.out.println("No engagement metrics reported yet");
            }
        }
    }
    
    /**
     * Display influencer analytics.
     * 
     * @param influencer The influencer to analyze
     */
    public void displayInfluencerAnalytics(Influencer influencer) {
        if (influencer == null) {
            System.out.println("Influencer not found");
            return;
        }
        
        System.out.println("\n===== Influencer Analytics: " + influencer.getUsername() + " =====");
        
        // Display overall metrics
        System.out.println("Total Followers: " + influencer.getTotalFollowers());
        System.out.println("Total Campaigns: " + influencer.getTotalCampaigns());
        System.out.println("Total Earnings: $" + String.format("%.2f", influencer.getTotalEarnings()));
        
        // Display social media profiles
        System.out.println("\nSocial Media Profiles:");
        influencer.displaySocialMediaProfiles();
        
        // Display active campaigns
        System.out.println("\nActive Campaigns:");
        List<Campaign> activeCampaigns = influencer.getActiveCampaigns();
        if (activeCampaigns.isEmpty()) {
            System.out.println("No active campaigns");
        } else {
            for (Campaign campaign : activeCampaigns) {
                System.out.println("- " + campaign.getName() + " (Status: " + 
                                  campaign.getInfluencerStatus(influencer) + ")");
                
                // If this influencer has reported metrics for this campaign, show them
                Map<String, Integer> metrics = campaign.getEngagementMetrics(influencer);
                if (!metrics.isEmpty()) {
                    int likes = metrics.getOrDefault("likes", 0);
                    int comments = metrics.getOrDefault("comments", 0);
                    int shares = metrics.getOrDefault("shares", 0);
                    
                    System.out.println("  * Likes: " + likes);
                    System.out.println("  * Comments: " + comments);
                    System.out.println("  * Shares: " + shares);
                    System.out.println("  * Total Engagement: " + (likes + comments + shares));
                }
            }
        }
        
        // Display campaign history
        System.out.println("\nCampaign History:");
        List<Campaign> pastCampaigns = influencer.getPastCampaigns();
        if (pastCampaigns.isEmpty()) {
            System.out.println("No past campaigns");
        } else {
            for (Campaign campaign : pastCampaigns) {
                System.out.println("- " + campaign.getName());
            }
        }
    }
    
    /**
     * Display brand analytics.
     * 
     * @param brand The brand to analyze
     */
    public void displayBrandAnalytics(Brand brand) {
        if (brand == null) {
            System.out.println("Brand not found");
            return;
        }
        
        System.out.println("\n===== Brand Analytics: " + 
                         (brand.getCompanyName() != null ? brand.getCompanyName() : brand.getUsername()) + 
                         " =====");
        
        // Display overall metrics
        System.out.println("Total Budget: $" + String.format("%.2f", brand.getBudget()));
        System.out.println("Remaining Budget: $" + String.format("%.2f", brand.getRemainingBudget()));
        System.out.println("Total Spent: $" + String.format("%.2f", brand.getTotalSpent()));
        System.out.println("Total Campaigns: " + brand.getTotalCampaigns());
        
        // Display active campaigns
        System.out.println("\nActive Campaigns:");
        List<Campaign> activeCampaigns = brand.getActiveCampaigns();
        if (activeCampaigns.isEmpty()) {
            System.out.println("No active campaigns");
        } else {
            for (Campaign campaign : activeCampaigns) {
                System.out.println("- " + campaign.getName() + " (Status: " + campaign.getStatus() + ")");
                System.out.println("  * Budget: $" + String.format("%.2f", campaign.getBudget()));
                System.out.println("  * Influencers: " + campaign.getAcceptedInfluencers().size());
                System.out.println("  * Total Engagement: " + campaign.calculateTotalEngagement());
                
                // Calculate cost per engagement
                double costPerEngagement = campaign.calculateCostPerEngagement();
                System.out.println("  * Cost Per Engagement: $" + 
                                 (costPerEngagement > 0 ? String.format("%.4f", costPerEngagement) : "N/A"));
            }
        }
        
        // Display campaign history
        System.out.println("\nCampaign History:");
        List<Campaign> pastCampaigns = brand.getPastCampaigns();
        if (pastCampaigns.isEmpty()) {
            System.out.println("No past campaigns");
        } else {
            for (Campaign campaign : pastCampaigns) {
                System.out.println("- " + campaign.getName());
            }
        }
        
        // ROI analysis (simplified for demo)
        System.out.println("\nROI Analysis:");
        calculateAndDisplayROI(brand);
    }
    
    /**
     * Display advertiser analytics.
     * 
     * @param advertiser The advertiser to analyze
     */
    public void displayAdvertiserAnalytics(Advertiser advertiser) {
        if (advertiser == null) {
            System.out.println("Advertiser not found");
            return;
        }
        
        System.out.println("\n===== Advertiser Analytics: " + 
                         (advertiser.getAgencyName() != null ? advertiser.getAgencyName() : advertiser.getUsername()) + 
                         " =====");
        
        // Display overall metrics
        System.out.println("Total Clients: " + advertiser.getTotalClients());
        System.out.println("Managed Brands: " + advertiser.getManagedBrands().size());
        System.out.println("Managed Campaigns: " + advertiser.getManagedCampaigns().size());
        System.out.println("Commission Rate: " + advertiser.getCommission() + "%");
        System.out.println("Estimated Revenue: $" + String.format("%.2f", advertiser.calculateRevenue()));
        
        // Display managed brands
        System.out.println("\nManaged Brands:");
        List<Brand> managedBrands = advertiser.getManagedBrands();
        if (managedBrands.isEmpty()) {
            System.out.println("No managed brands");
        } else {
            for (Brand brand : managedBrands) {
                System.out.println("- " + (brand.getCompanyName() != null ? brand.getCompanyName() : brand.getUsername()));
                System.out.println("  * Budget: $" + String.format("%.2f", brand.getBudget()));
                System.out.println("  * Active Campaigns: " + brand.getActiveCampaigns().size());
            }
        }
        
        // Display managed campaigns
        System.out.println("\nManaged Campaigns:");
        List<Campaign> managedCampaigns = advertiser.getManagedCampaigns();
        if (managedCampaigns.isEmpty()) {
            System.out.println("No managed campaigns");
        } else {
            for (Campaign campaign : managedCampaigns) {
                System.out.println("- " + campaign.getName() + " (Status: " + campaign.getStatus() + ")");
                System.out.println("  * Budget: $" + String.format("%.2f", campaign.getBudget()));
                System.out.println("  * Brand: " + (campaign.getBrand() instanceof Brand ? 
                                 ((Brand)campaign.getBrand()).getCompanyName() : 
                                 campaign.getBrand().getUsername()));
                System.out.println("  * Influencers: " + campaign.getAcceptedInfluencers().size());
                System.out.println("  * Revenue (Commission): $" + 
                                 String.format("%.2f", campaign.getBudget() * (advertiser.getCommission() / 100)));
            }
        }
    }
    
    /**
     * Calculate and display ROI for a brand.
     * 
     * @param brand The brand to analyze
     */
    private void calculateAndDisplayROI(Brand brand) {
        if (campaignService == null) {
            System.out.println("Campaign service not available");
            return;
        }
        
        List<Campaign> campaigns = campaignService.getCampaignsForBrand(brand);
        
        if (campaigns.isEmpty()) {
            System.out.println("No campaigns to analyze");
            return;
        }
        
        double totalSpent = 0.0;
        int totalEngagement = 0;
        
        for (Campaign campaign : campaigns) {
            totalSpent += campaign.getBudget();
            totalEngagement += campaign.calculateTotalEngagement();
        }
        
        if (totalEngagement == 0) {
            System.out.println("No engagement data available");
            return;
        }
        
        double costPerEngagement = totalSpent / totalEngagement;
        
        System.out.println("Total Investment: $" + String.format("%.2f", totalSpent));
        System.out.println("Total Engagement: " + totalEngagement);
        System.out.println("Average Cost Per Engagement: $" + String.format("%.4f", costPerEngagement));
        
        // In a real system, would calculate actual ROI based on conversions, sales, etc.
        System.out.println("Note: Complete ROI analysis would require sales conversion data");
    }
    
    /**
     * Generate a user activity report.
     */
    public void generateUserActivityReport() {
        if (userService == null) {
            System.out.println("User service not available");
            return;
        }
        
        System.out.println("\n===== USER ACTIVITY REPORT =====");
        System.out.println("Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        List<User> allUsers = userService.getAllUsers();
        int totalUsers = allUsers.size();
        int activeUsers = (int) allUsers.stream().filter(User::isActive).count();
        
        System.out.println("\nOVERALL STATISTICS:");
        System.out.println("Total Users: " + totalUsers);
        System.out.println("Active Users: " + activeUsers + " (" + 
                         String.format("%.1f", (activeUsers * 100.0 / totalUsers)) + "%)");
        
        List<Influencer> influencers = userService.getAllInfluencers();
        List<Brand> brands = userService.getAllBrands();
        List<Advertiser> advertisers = userService.getAllAdvertisers();
        List<Admin> admins = userService.getAllAdmins();
        
        System.out.println("\nUSER DISTRIBUTION:");
        System.out.println("Influencers: " + influencers.size() + " (" + 
                         String.format("%.1f", (influencers.size() * 100.0 / totalUsers)) + "%)");
        System.out.println("Brands: " + brands.size() + " (" + 
                         String.format("%.1f", (brands.size() * 100.0 / totalUsers)) + "%)");
        System.out.println("Advertisers: " + advertisers.size() + " (" + 
                         String.format("%.1f", (advertisers.size() * 100.0 / totalUsers)) + "%)");
        System.out.println("Admins: " + admins.size() + " (" + 
                         String.format("%.1f", (admins.size() * 100.0 / totalUsers)) + "%)");
        
        // Display top influencers
        System.out.println("\nTOP INFLUENCERS (by follower count):");
        List<Influencer> topInfluencers = userService.getTopInfluencers(5);
        for (Influencer influencer : topInfluencers) {
            System.out.println("- " + influencer.getUsername() + 
                             ": " + influencer.getTotalFollowers() + " followers");
        }
        
        // Display top brands
        System.out.println("\nTOP BRANDS (by budget):");
        List<Brand> topBrands = userService.getTopBrands(5);
        for (Brand brand : topBrands) {
            System.out.println("- " + (brand.getCompanyName() != null ? brand.getCompanyName() : brand.getUsername()) + 
                             ": $" + String.format("%.2f", brand.getBudget()));
        }
    }
    
    /**
     * Generate a campaign performance report.
     */
    public void generateCampaignPerformanceReport() {
        if (campaignService == null) {
            System.out.println("Campaign service not available");
            return;
        }
        
        System.out.println("\n===== CAMPAIGN PERFORMANCE REPORT =====");
        System.out.println("Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        List<Campaign> allCampaigns = campaignService.getAllCampaigns();
        int totalCampaigns = allCampaigns.size();
        
        System.out.println("\nOVERALL STATISTICS:");
        System.out.println("Total Campaigns: " + totalCampaigns);
        
        // Count campaigns by status
        Map<String, Integer> statusCounts = new HashMap<>();
        for (Campaign campaign : allCampaigns) {
            String status = campaign.getStatus();
            statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
        }
        
        System.out.println("\nCAMPAIGN STATUS DISTRIBUTION:");
        for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
            double percentage = entry.getValue() * 100.0 / totalCampaigns;
            System.out.println(entry.getKey() + ": " + entry.getValue() + 
                             " (" + String.format("%.1f", percentage) + "%)");
        }
        
        // Display top performing campaigns
        System.out.println("\nTOP PERFORMING CAMPAIGNS (by engagement):");
        List<Campaign> topCampaigns = campaignService.getTopPerformingCampaigns(5);
        for (Campaign campaign : topCampaigns) {
            System.out.println("- " + campaign.getName() + 
                             ": " + campaign.calculateTotalEngagement() + " total engagement");
            System.out.println("  * Budget: $" + String.format("%.2f", campaign.getBudget()));
            System.out.println("  * Cost Per Engagement: $" + 
                             String.format("%.4f", campaign.calculateCostPerEngagement()));
            System.out.println("  * Influencers: " + campaign.getAcceptedInfluencers().size());
        }
        
        // Calculate overall metrics
        double totalBudget = 0.0;
        int totalEngagement = 0;
        int totalPosts = 0;
        
        for (Campaign campaign : allCampaigns) {
            Map<String, Object> metrics = campaign.getMetrics();
            totalBudget += campaign.getBudget();
            totalEngagement += (int) metrics.get("total_engagement");
            totalPosts += (int) metrics.get("total_posts");
        }
        
        System.out.println("\nCUMULATIVE METRICS:");
        System.out.println("Total Budget Allocated: $" + String.format("%.2f", totalBudget));
        System.out.println("Total Engagement: " + totalEngagement);
        System.out.println("Total Posts: " + totalPosts);
        
        if (totalEngagement > 0) {
            System.out.println("Overall Cost Per Engagement: $" + 
                             String.format("%.4f", totalBudget / totalEngagement));
        }
    }
    
    /**
     * Generate a financial report.
     */
    public void generateFinancialReport() {
        if (userService == null || campaignService == null) {
            System.out.println("Required services not available");
            return;
        }
        
        System.out.println("\n===== FINANCIAL REPORT =====");
        System.out.println("Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        List<Brand> brands = userService.getAllBrands();
        List<Influencer> influencers = userService.getAllInfluencers();
        List<Campaign> campaigns = campaignService.getAllCampaigns();
        
        // Calculate total platform metrics
        double totalBrandBudgets = brands.stream().mapToDouble(Brand::getBudget).sum();
        double totalBrandSpent = brands.stream().mapToDouble(Brand::getTotalSpent).sum();
        double totalInfluencerEarnings = influencers.stream().mapToDouble(Influencer::getTotalEarnings).sum();
        double totalCampaignBudgets = campaigns.stream().mapToDouble(Campaign::getBudget).sum();
        
        System.out.println("\nPLATFORM FINANCIALS:");
        System.out.println("Total Brand Budgets: $" + String.format("%.2f", totalBrandBudgets));
        System.out.println("Total Brand Spent: $" + String.format("%.2f", totalBrandSpent));
        System.out.println("Total Campaign Budgets: $" + String.format("%.2f", totalCampaignBudgets));
        System.out.println("Total Influencer Earnings: $" + String.format("%.2f", totalInfluencerEarnings));
        
        // Platform revenue (assuming platform takes a 10% commission)
        double platformCommissionRate = 0.10; // 10%
        double estimatedPlatformRevenue = totalBrandSpent * platformCommissionRate;
        
        System.out.println("Estimated Platform Revenue (10% commission): $" + 
                         String.format("%.2f", estimatedPlatformRevenue));
        
        // Top spending brands
        System.out.println("\nTOP SPENDING BRANDS:");
        brands.stream()
            .sorted((b1, b2) -> Double.compare(b2.getTotalSpent(), b1.getTotalSpent()))
            .limit(5)
            .forEach(b -> System.out.println("- " + 
                                           (b.getCompanyName() != null ? b.getCompanyName() : b.getUsername()) + 
                                           ": $" + String.format("%.2f", b.getTotalSpent())));
        
        // Top earning influencers
        System.out.println("\nTOP EARNING INFLUENCERS:");
        influencers.stream()
            .sorted((i1, i2) -> Double.compare(i2.getTotalEarnings(), i1.getTotalEarnings()))
            .limit(5)
            .forEach(i -> System.out.println("- " + i.getUsername() + 
                                          ": $" + String.format("%.2f", i.getTotalEarnings())));
        
        // Most expensive campaigns
        System.out.println("\nMOST EXPENSIVE CAMPAIGNS:");
        campaigns.stream()
            .sorted((c1, c2) -> Double.compare(c2.getBudget(), c1.getBudget()))
            .limit(5)
            .forEach(c -> System.out.println("- " + c.getName() + 
                                          ": $" + String.format("%.2f", c.getBudget())));
    }
    
    /**
     * Calculate total platform value.
     * 
     * @return The calculated platform value
     */
    public double calculatePlatformValue() {
        if (userService == null || campaignService == null) {
            return 0.0;
        }
        
        List<Brand> brands = userService.getAllBrands();
        List<Influencer> influencers = userService.getAllInfluencers();
        
        // In a real valuation, would use much more sophisticated metrics
        // This is a simplified calculation for demo purposes
        
        double totalBrandValue = brands.stream().mapToDouble(Brand::getBudget).sum();
        int totalInfluencerFollowers = influencers.stream().mapToInt(Influencer::getTotalFollowers).sum();
        
        // Very simplified calculation
        double platformValue = totalBrandValue * 0.2 + totalInfluencerFollowers * 0.01;
        
        return platformValue;
    }
    
    /**
     * Analyze influencer growth trends.
     * 
     * @param influencer The influencer to analyze
     * @return Map containing growth metrics
     */
    public Map<String, Object> analyzeInfluencerGrowth(Influencer influencer) {
        Map<String, Object> growthMetrics = new HashMap<>();
        
        // In a real app, this would analyze historical data
        // For demo purposes, return mock metrics
        
        growthMetrics.put("follower_growth", "10% per month");
        growthMetrics.put("engagement_rate", "3.2%");
        growthMetrics.put("earnings_trend", "Growing");
        
        return growthMetrics;
    }
    
    /**
     * Generate recommendations for a brand's campaign strategy.
     * 
     * @param brand The brand to analyze
     * @return List of recommendations
     */
    public List<String> generateBrandRecommendations(Brand brand) {
        List<String> recommendations = new ArrayList<>();
        
        // In a real app, this would use AI/ML to generate personalized recommendations
        // For demo purposes, return generic recommendations
        
        recommendations.add("Consider increasing budget for higher engagement");
        recommendations.add("Target influencers in " + (brand.getIndustry() != null ? brand.getIndustry() : "your industry"));
        recommendations.add("Focus on platforms with highest ROI for your industry");
        
        return recommendations;
    }
}
