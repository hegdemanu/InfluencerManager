package com.influencerManager.main;

import com.influencerManager.model.*;
import com.influencerManager.service.*;
import com.influencerManager.util.*;
import com.influencerManager.exception.*;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Main application class for the Influencer Manager platform.
 * This class serves as the entry point of the application and demonstrates
 * the functionality of the influencer collaboration platform.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class InfluencerManagerApp {
    private static UserService userService;
    private static CampaignService campaignService;
    private static AnalyticsService analyticsService;
    private static RecommendationService recommendationService;
    private static NotificationService notificationService;
    private static FileService fileService;
    private static AuthenticationManager authManager;
    private static User currentUser;
    private static Scanner scanner;
    
    /**
     * Main method to start the application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        initializeSystem();
        welcomeMenu();
        
        // Clean up resources
        scanner.close();
        System.out.println("Thank you for using Influencer Manager Platform. Goodbye!");
    }
    
    /**
     * Initialize all services and managers required for the system.
     */
    private static void initializeSystem() {
        try {
            scanner = new Scanner(System.in);
            userService = new UserService();
            campaignService = new CampaignService();
            analyticsService = new AnalyticsService();
            recommendationService = new RecommendationService();
            notificationService = new NotificationService();
            fileService = new FileService();
            authManager = new AuthenticationManager();
            
            // Load sample data (in a real app, this would be from a database)
            loadSampleData();
            
            // Start notification service in a separate thread
            Thread notificationThread = new Thread(notificationService);
            notificationThread.start();
            
            System.out.println("System initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error initializing system: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Load sample data for demonstration purposes.
     */
    private static void loadSampleData() {
        try {
            // Load users from file
            fileService.loadUserData(userService);
            
            // Load campaigns from file
            fileService.loadCampaignData(campaignService);
            
            System.out.println("Sample data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Could not load sample data from files. Creating default data.");
            
            // Create default admin
            Admin admin = new Admin("admin", "admin@platform.com", "password123");
            userService.addUser(admin);
            
            // Create some influencers
            Influencer influencer1 = new Influencer("janesmith", "jane@influencer.com", "pass123");
            influencer1.addSocialMedia("Instagram", "jane_style", 50000);
            influencer1.addSocialMedia("TikTok", "janesmithofficial", 75000);
            influencer1.setNiche("Fashion");
            influencer1.setRate(500.0);
            userService.addUser(influencer1);
            
            Influencer influencer2 = new Influencer("techguru", "tech@influencer.com", "guru123");
            influencer2.addSocialMedia("YouTube", "TechWithGuru", 200000);
            influencer2.addSocialMedia("Twitter", "TechGuru", 35000);
            influencer2.setNiche("Technology");
            influencer2.setRate(700.0);
            userService.addUser(influencer2);
            
            // Create some brands
            Brand brand1 = new Brand("fashionco", "contact@fashionco.com", "brand123");
            brand1.setCompanyName("Fashion Co");
            brand1.setIndustry("Clothing");
            brand1.setBudget(10000.0);
            userService.addUser(brand1);
            
            Brand brand2 = new Brand("techcorp", "marketing@techcorp.com", "tech456");
            brand2.setCompanyName("Tech Corp");
            brand2.setIndustry("Technology");
            brand2.setBudget(25000.0);
            userService.addUser(brand2);
            
            // Create some campaigns
            Campaign campaign1 = new Campaign("Summer Collection", brand1, "Fashion product promotion");
            campaign1.addInfluencer(influencer1);
            campaign1.setBudget(5000.0);
            campaign1.setStartDate("2023-06-01");
            campaign1.setEndDate("2023-07-30");
            campaignService.addCampaign(campaign1);
            
            Campaign campaign2 = new Campaign("Gadget Launch", brand2, "New smartphone promotion");
            campaign2.addInfluencer(influencer2);
            campaign2.setBudget(8000.0);
            campaign2.setStartDate("2023-07-15");
            campaign2.setEndDate("2023-08-15");
            campaignService.addCampaign(campaign2);
        }
    }
    
    /**
     * Display the welcome menu and handle user login or registration.
     */
    private static void welcomeMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n===== INFLUENCER MANAGER PLATFORM =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 3:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (AuthenticationException e) {
                System.out.println("Authentication error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Handle user login process.
     * 
     * @throws AuthenticationException If login fails
     */
    private static void login() throws AuthenticationException {
        System.out.println("\n----- Login -----");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        currentUser = authManager.authenticate(username, password, userService);
        
        if (currentUser != null) {
            System.out.println("Login successful! Welcome, " + currentUser.getUsername());
            mainMenu();
        } else {
            throw new AuthenticationException("Invalid username or password");
        }
    }
    
    /**
     * Handle user registration process.
     */
    private static void register() {
        System.out.println("\n----- Registration -----");
        System.out.println("Select user type:");
        System.out.println("1. Influencer");
        System.out.println("2. Brand");
        System.out.println("3. Advertiser");
        System.out.print("Enter choice: ");
        
        try {
            int userType = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            switch (userType) {
                case 1:
                    Influencer influencer = new Influencer(username, email, password);
                    System.out.print("Niche (e.g., Fashion, Technology): ");
                    String niche = scanner.nextLine();
                    influencer.setNiche(niche);
                    
                    System.out.print("Rate per post ($): ");
                    double rate = Double.parseDouble(scanner.nextLine());
                    influencer.setRate(rate);
                    
                    // Add social media platforms
                    boolean addingPlatforms = true;
                    while (addingPlatforms) {
                        System.out.print("Add social media platform (y/n)? ");
                        String choice = scanner.nextLine();
                        if (choice.equalsIgnoreCase("y")) {
                            System.out.print("Platform name (e.g., Instagram, YouTube): ");
                            String platform = scanner.nextLine();
                            System.out.print("Handle/username: ");
                            String handle = scanner.nextLine();
                            System.out.print("Follower count: ");
                            int followers = Integer.parseInt(scanner.nextLine());
                            
                            influencer.addSocialMedia(platform, handle, followers);
                        } else {
                            addingPlatforms = false;
                        }
                    }
                    
                    userService.addUser(influencer);
                    break;
                    
                case 2:
                    Brand brand = new Brand(username, email, password);
                    System.out.print("Company Name: ");
                    String companyName = scanner.nextLine();
                    brand.setCompanyName(companyName);
                    
                    System.out.print("Industry: ");
                    String industry = scanner.nextLine();
                    brand.setIndustry(industry);
                    
                    System.out.print("Budget ($): ");
                    double budget = Double.parseDouble(scanner.nextLine());
                    brand.setBudget(budget);
                    
                    userService.addUser(brand);
                    break;
                    
                case 3:
                    Advertiser advertiser = new Advertiser(username, email, password);
                    System.out.print("Agency Name: ");
                    String agencyName = scanner.nextLine();
                    advertiser.setAgencyName(agencyName);
                    
                    userService.addUser(advertiser);
                    break;
                    
                default:
                    System.out.println("Invalid user type.");
                    return;
            }
            
            System.out.println("Registration successful! You can now login.");
            fileService.saveUserData(userService);
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers where required.");
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }
    
    /**
     * Display the main menu based on user role.
     */
    private static void mainMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n===== MAIN MENU =====");
            
            // Common options for all users
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            
            // Role-specific options
            if (currentUser instanceof Admin) {
                System.out.println("3. View All Users");
                System.out.println("4. View All Campaigns");
                System.out.println("5. Generate System Reports");
            } else if (currentUser instanceof Influencer) {
                System.out.println("3. View Campaign Offers");
                System.out.println("4. View My Campaigns");
                System.out.println("5. View Analytics");
                System.out.println("6. Update Social Media Profiles");
            } else if (currentUser instanceof Brand) {
                System.out.println("3. Search Influencers");
                System.out.println("4. Create Campaign");
                System.out.println("5. View My Campaigns");
                System.out.println("6. View Campaign Analytics");
            } else if (currentUser instanceof Advertiser) {
                System.out.println("3. View Brand Clients");
                System.out.println("4. Manage Brand Campaigns");
                System.out.println("5. View Analytics");
            }
            
            System.out.println("0. Logout");
            System.out.print("Select an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                if (choice == 0) {
                    currentUser = null;
                    System.out.println("Logged out successfully.");
                    running = false;
                } else {
                    handleMainMenuChoice(choice);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Handle user selection from the main menu.
     * 
     * @param choice User's menu selection
     */
    private static void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                viewProfile();
                break;
            case 2:
                updateProfile();
                break;
            case 3:
                if (currentUser instanceof Admin) {
                    viewAllUsers();
                } else if (currentUser instanceof Influencer) {
                    viewCampaignOffers();
                } else if (currentUser instanceof Brand) {
                    searchInfluencers();
                } else if (currentUser instanceof Advertiser) {
                    viewBrandClients();
                }
                break;
            case 4:
                if (currentUser instanceof Admin) {
                    viewAllCampaigns();
                } else if (currentUser instanceof Influencer) {
                    viewMyCampaigns();
                } else if (currentUser instanceof Brand) {
                    createCampaign();
                } else if (currentUser instanceof Advertiser) {
                    manageBrandCampaigns();
                }
                break;
            case 5:
                if (currentUser instanceof Admin) {
                    generateSystemReports();
                } else if (currentUser instanceof Influencer || currentUser instanceof Advertiser) {
                    viewAnalytics();
                } else if (currentUser instanceof Brand) {
                    viewMyCampaigns();
                }
                break;
            case 6:
                if (currentUser instanceof Influencer) {
                    updateSocialMediaProfiles();
                } else if (currentUser instanceof Brand) {
                    viewCampaignAnalytics();
                }
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    // Various functionality methods
    
    private static void viewProfile() {
        System.out.println("\n----- Your Profile -----");
        System.out.println(currentUser.toString());
    }
    
    private static void updateProfile() {
        System.out.println("\n----- Update Profile -----");
        System.out.print("New email (leave blank to keep current): ");
        String email = scanner.nextLine();
        
        if (!email.isEmpty()) {
            currentUser.setEmail(email);
        }
        
        System.out.print("New password (leave blank to keep current): ");
        String password = scanner.nextLine();
        
        if (!password.isEmpty()) {
            currentUser.setPassword(password);
        }
        
        // Role-specific updates
        if (currentUser instanceof Influencer) {
            Influencer influencer = (Influencer) currentUser;
            
            System.out.print("New niche (leave blank to keep current): ");
            String niche = scanner.nextLine();
            if (!niche.isEmpty()) {
                influencer.setNiche(niche);
            }
            
            System.out.print("New rate per post (leave blank to keep current): ");
            String rateStr = scanner.nextLine();
            if (!rateStr.isEmpty()) {
                try {
                    double rate = Double.parseDouble(rateStr);
                    influencer.setRate(rate);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid rate format. Rate not updated.");
                }
            }
        } else if (currentUser instanceof Brand) {
            Brand brand = (Brand) currentUser;
            
            System.out.print("New company name (leave blank to keep current): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                brand.setCompanyName(name);
            }
            
            System.out.print("New industry (leave blank to keep current): ");
            String industry = scanner.nextLine();
            if (!industry.isEmpty()) {
                brand.setIndustry(industry);
            }
            
            System.out.print("New budget (leave blank to keep current): ");
            String budgetStr = scanner.nextLine();
            if (!budgetStr.isEmpty()) {
                try {
                    double budget = Double.parseDouble(budgetStr);
                    brand.setBudget(budget);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid budget format. Budget not updated.");
                }
            }
        }
        
        try {
            fileService.saveUserData(userService);
            System.out.println("Profile updated successfully!");
        } catch (IOException e) {
            System.out.println("Error saving profile updates: " + e.getMessage());
        }
    }
    
    private static void viewAllUsers() {
        System.out.println("\n----- All Users -----");
        List<User> users = userService.getAllUsers();
        
        System.out.println("Total users: " + users.size());
        System.out.println("\nInfluencers:");
        users.stream()
            .filter(u -> u instanceof Influencer)
            .forEach(System.out::println);
        
        System.out.println("\nBrands:");
        users.stream()
            .filter(u -> u instanceof Brand)
            .forEach(System.out::println);
        
        System.out.println("\nAdvertisers:");
        users.stream()
            .filter(u -> u instanceof Advertiser)
            .forEach(System.out::println);
        
        System.out.println("\nAdmins:");
        users.stream()
            .filter(u -> u instanceof Admin)
            .forEach(System.out::println);
    }
    
    private static void viewAllCampaigns() {
        System.out.println("\n----- All Campaigns -----");
        campaignService.getAllCampaigns().forEach(System.out::println);
    }
    
    private static void generateSystemReports() {
        System.out.println("\n----- System Reports -----");
        System.out.println("1. User Activity Report");
        System.out.println("2. Campaign Performance Report");
        System.out.println("3. Financial Report");
        System.out.print("Select report type: ");
        
        try {
            int reportType = Integer.parseInt(scanner.nextLine());
            
            switch (reportType) {
                case 1:
                    analyticsService.generateUserActivityReport();
                    break;
                case 2:
                    analyticsService.generateCampaignPerformanceReport();
                    break;
                case 3:
                    analyticsService.generateFinancialReport();
                    break;
                default:
                    System.out.println("Invalid report type.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
    
    private static void viewCampaignOffers() {
        System.out.println("\n----- Campaign Offers -----");
        Influencer influencer = (Influencer) currentUser;
        List<Campaign> offers = campaignService.getCampaignOffersForInfluencer(influencer);
        
        if (offers.isEmpty()) {
            System.out.println("You have no campaign offers at this time.");
            return;
        }
        
        for (int i = 0; i < offers.size(); i++) {
            System.out.println((i + 1) + ". " + offers.get(i).getName() + " by " + 
                              ((Brand)offers.get(i).getBrand()).getCompanyName());
        }
        
        System.out.print("Select a campaign to view details (0 to cancel): ");
        try {
            int selection = Integer.parseInt(scanner.nextLine());
            
            if (selection > 0 && selection <= offers.size()) {
                Campaign selected = offers.get(selection - 1);
                System.out.println("\nCampaign Details:");
                System.out.println(selected);
                
                System.out.print("Accept this campaign? (y/n): ");
                String choice = scanner.nextLine();
                
                if (choice.equalsIgnoreCase("y")) {
                    selected.acceptInfluencer(influencer);
                    System.out.println("Campaign accepted!");
                    // Create a contract
                    Contract contract = new Contract(selected, influencer, selected.getBrand());
                    // In a real application, this would be saved to a database
                    System.out.println("Contract created successfully.");
                    
                    try {
                        fileService.saveCampaignData(campaignService);
                    } catch (IOException e) {
                        System.out.println("Error saving campaign data: " + e.getMessage());
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewMyCampaigns() {
        System.out.println("\n----- My Campaigns -----");
        List<Campaign> campaigns;
        
        if (currentUser instanceof Influencer) {
            campaigns = campaignService.getCampaignsForInfluencer((Influencer) currentUser);
        } else if (currentUser instanceof Brand) {
            campaigns = campaignService.getCampaignsForBrand((Brand) currentUser);
        } else {
            System.out.println("This feature is not available for your user type.");
            return;
        }
        
        if (campaigns.isEmpty()) {
            System.out.println("You have no campaigns at this time.");
            return;
        }
        
        for (int i = 0; i < campaigns.size(); i++) {
            System.out.println((i + 1) + ". " + campaigns.get(i).getName());
        }
        
        System.out.print("Select a campaign to view details (0 to cancel): ");
        try {
            int selection = Integer.parseInt(scanner.nextLine());
            
            if (selection > 0 && selection <= campaigns.size()) {
                Campaign selected = campaigns.get(selection - 1);
                System.out.println("\nCampaign Details:");
                System.out.println(selected);
                
                if (currentUser instanceof Brand) {
                    System.out.println("\nOptions:");
                    System.out.println("1. Edit Campaign");
                    System.out.println("2. View Performance Metrics");
                    System.out.println("3. Add Influencer");
                    System.out.print("Select an option (0 to cancel): ");
                    
                    int option = Integer.parseInt(scanner.nextLine());
                    
                    switch (option) {
                        case 1:
                            editCampaign(selected);
                            break;
                        case 2:
                            viewCampaignPerformance(selected);
                            break;
                        case 3:
                            addInfluencerToCampaign(selected);
                            break;
                    }
                } else if (currentUser instanceof Influencer) {
                    System.out.println("\nOptions:");
                    System.out.println("1. View Performance Metrics");
                    System.out.println("2. Update Content Status");
                    System.out.print("Select an option (0 to cancel): ");
                    
                    int option = Integer.parseInt(scanner.nextLine());
                    
                    switch (option) {
                        case 1:
                            viewCampaignPerformance(selected);
                            break;
                        case 2:
                            updateContentStatus(selected);
                            break;
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void updateSocialMediaProfiles() {
        Influencer influencer = (Influencer) currentUser;
        System.out.println("\n----- Update Social Media Profiles -----");
        
        // Display current profiles
        influencer.displaySocialMediaProfiles();
        
        System.out.println("\nOptions:");
        System.out.println("1. Add new social media profile");
        System.out.println("2. Update existing profile");
        System.out.println("3. Remove profile");
        System.out.print("Select an option (0 to cancel): ");
        
        try {
            int option = Integer.parseInt(scanner.nextLine());
            
            switch (option) {
                case 1:
                    System.out.print("Platform name: ");
                    String platform = scanner.nextLine();
                    System.out.print("Handle/username: ");
                    String handle = scanner.nextLine();
                    System.out.print("Follower count: ");
                    int followers = Integer.parseInt(scanner.nextLine());
                    
                    influencer.addSocialMedia(platform, handle, followers);
                    System.out.println("Social media profile added!");
                    break;
                    
                case 2:
                    System.out.print("Platform to update: ");
                    String platformToUpdate = scanner.nextLine();
                    System.out.print("New handle/username (leave blank to keep current): ");
                    String newHandle = scanner.nextLine();
                    System.out.print("New follower count (leave blank to keep current): ");
                    String followerStr = scanner.nextLine();
                    
                    if (!newHandle.isEmpty() || !followerStr.isEmpty()) {
                        int newFollowers = followerStr.isEmpty() ? 
                                          influencer.getFollowerCount(platformToUpdate) : 
                                          Integer.parseInt(followerStr);
                        
                        influencer.updateSocialMedia(platformToUpdate, 
                                                   newHandle.isEmpty() ? influencer.getHandle(platformToUpdate) : newHandle, 
                                                   newFollowers);
                        System.out.println("Social media profile updated!");
                    }
                    break;
                    
                case 3:
                    System.out.print("Platform to remove: ");
                    String platformToRemove = scanner.nextLine();
                    
                    influencer.removeSocialMedia(platformToRemove);
                    System.out.println("Social media profile removed!");
                    break;
            }
            
            fileService.saveUserData(userService);
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void searchInfluencers() {
        System.out.println("\n----- Search Influencers -----");
        System.out.println("Search filters:");
        System.out.print("Niche (leave blank for all): ");
        String niche = scanner.nextLine();
        
        System.out.print("Minimum followers (leave blank for any): ");
        String minFollowersStr = scanner.nextLine();
        int minFollowers = 0;
        if (!minFollowersStr.isEmpty()) {
            try {
                minFollowers = Integer.parseInt(minFollowersStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Using default value 0.");
            }
        }
        
        System.out.print("Maximum rate per post (leave blank for any): ");
        String maxRateStr = scanner.nextLine();
        double maxRate = Double.MAX_VALUE;
        if (!maxRateStr.isEmpty()) {
            try {
                maxRate = Double.parseDouble(maxRateStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Using no maximum rate limit.");
            }
        }
        
        List<Influencer> results = userService.searchInfluencers(niche, minFollowers, maxRate);
        
        System.out.println("\nSearch Results (" + results.size() + " influencers found):");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i).getUsername() + 
                              " - Niche: " + results.get(i).getNiche() + 
                              ", Rate: $" + results.get(i).getRate() + "/post");
        }
        
        if (!results.isEmpty()) {
            System.out.print("\nSelect an influencer to view full profile (0 to cancel): ");
            
            try {
                int selection = Integer.parseInt(scanner.nextLine());
                
                if (selection > 0 && selection <= results.size()) {
                    Influencer selected = results.get(selection - 1);
                    System.out.println("\nInfluencer Profile:");
                    System.out.println(selected);
                    selected.displaySocialMediaProfiles();
                    
                    System.out.print("\nInvite this influencer to a campaign? (y/n): ");
                    String choice = scanner.nextLine();
                    
                    if (choice.equalsIgnoreCase("y")) {
                        inviteInfluencerToCampaign(selected);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static void inviteInfluencerToCampaign(Influencer influencer) {
        Brand brand = (Brand) currentUser;
        List<Campaign> brandCampaigns = campaignService.getCampaignsForBrand(brand);
        
        if (brandCampaigns.isEmpty()) {
            System.out.println("You don't have any active campaigns. Create a campaign first.");
            return;
        }
        
        System.out.println("\nSelect a campaign to invite the influencer to:");
        for (int i = 0; i < brandCampaigns.size(); i++) {
            System.out.println((i + 1) + ". " + brandCampaigns.get(i).getName());
        }
        
        System.out.print("Select a campaign (0 to cancel): ");
        try {
            int selection = Integer.parseInt(scanner.nextLine());
            
            if (selection > 0 && selection <= brandCampaigns.size()) {
                Campaign selected = brandCampaigns.get(selection - 1);
                
                // Create an offer
                selected.inviteInfluencer(influencer);
                System.out.println("Invitation sent to " + influencer.getUsername() + "!");
                
                // Send notification (in a real system, this would send an email or in-app notification)
                notificationService.addNotification(influencer.getUsername(), 
                                                 "You've been invited to join the campaign: " + selected.getName());
                
                try {
                    fileService.saveCampaignData(campaignService);
                } catch (IOException e) {
                    System.out.println("Error saving campaign data: " + e.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void createCampaign() {
        System.out.println("\n----- Create New Campaign -----");
        System.out.print("Campaign Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Campaign Description: ");
        String description = scanner.nextLine();
        
        System.out.print("Budget: $");
        double budget = 0;
        try {
            budget = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid budget format. Using $0.");
        }
        
        System.out.print("Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        
        System.out.print("End Date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();
        
        // Create campaign
        Campaign campaign = new Campaign(name, (Brand) currentUser, description);
        campaign.setBudget(budget);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        
        // Add campaign to service
        campaignService.addCampaign(campaign);
        
        System.out.println("Campaign created successfully!");
        
        // Save to file
        try {
            fileService.saveCampaignData(campaignService);
        } catch (IOException e) {
            System.out.println("Error saving campaign data: " + e.getMessage());
        }
        
        // Ask if they want to invite influencers right away
        System.out.print("Would you like to invite influencers to this campaign now? (y/n): ");
        String choice = scanner.nextLine();
        
        if (choice.equalsIgnoreCase("y")) {
            addInfluencerToCampaign(campaign);
        }
    }
    
    private static void editCampaign(Campaign campaign) {
        System.out.println("\n----- Edit Campaign -----");
        System.out.print("New name (leave blank to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            campaign.setName(name);
        }
        
        System.out.print("New description (leave blank to keep current): ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            campaign.setDescription(description);
        }
        
        System.out.print("New budget (leave blank to keep current): ");
        String budgetStr = scanner.nextLine();
        if (!budgetStr.isEmpty()) {
            try {
                double budget = Double.parseDouble(budgetStr);
                campaign.setBudget(budget);
            } catch (NumberFormatException e) {
                System.out.println("Invalid budget format. Budget not updated.");
            }
        }
        
        System.out.print("New start date (YYYY-MM-DD) (leave blank to keep current): ");
        String startDate = scanner.nextLine();
        if (!startDate.isEmpty()) {
            campaign.setStartDate(startDate);
        }
        
        System.out.print("New end date (YYYY-MM-DD) (leave blank to keep current): ");
        String endDate = scanner.nextLine();
        if (!endDate.isEmpty()) {
            campaign.setEndDate(endDate);
        }
        
        // Save to file
        try {
            fileService.saveCampaignData(campaignService);
            System.out.println("Campaign updated successfully!");
        } catch (IOException e) {
            System.out.println("Error saving campaign data: " + e.getMessage());
        }
    }
    
    private static void addInfluencerToCampaign(Campaign campaign) {
        System.out.println("\n----- Add Influencer to Campaign -----");
        
        // Get recommended influencers
        List<Influencer> recommendations = recommendationService.getRecommendedInfluencers(campaign);
        
        System.out.println("Recommended Influencers:");
        for (int i = 0; i < recommendations.size(); i++) {
            System.out.println((i + 1) + ". " + recommendations.get(i).getUsername() + 
                              " - Niche: " + recommendations.get(i).getNiche() + 
                              ", Rate: $" + recommendations.get(i).getRate() + "/post");
        }
        
        System.out.println("\n" + (recommendations.size() + 1) + ". Search all influencers");
        System.out.print("Select an option (0 to cancel): ");
        
        try {
            int selection = Integer.parseInt(scanner.nextLine());
            
            if (selection > 0 && selection <= recommendations.size()) {
                Influencer selected = recommendations.get(selection - 1);
                campaign.inviteInfluencer(selected);
                System.out.println("Invitation sent to " + selected.getUsername() + "!");
                
                // Send notification
                notificationService.addNotification(selected.getUsername(), 
                                                 "You've been invited to join the campaign: " + campaign.getName());
                
                try {
                    fileService.saveCampaignData(campaignService);
                } catch (IOException e) {
                    System.out.println("Error saving campaign data: " + e.getMessage());
                }
            } else if (selection == recommendations.size() + 1) {
                searchInfluencersForCampaign(campaign);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void searchInfluencersForCampaign(Campaign campaign) {
        System.out.println("\n----- Search Influencers for Campaign -----");
        System.out.println("Search filters:");
        System.out.print("Niche (leave blank for all): ");
        String niche = scanner.nextLine();
        
        System.out.print("Minimum followers (leave blank for any): ");
        String minFollowersStr = scanner.nextLine();
        int minFollowers = 0;
        if (!minFollowersStr.isEmpty()) {
            try {
                minFollowers = Integer.parseInt(minFollowersStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Using default value 0.");
            }
        }
        
        System.out.print("Maximum rate per post (leave blank for any): ");
        String maxRateStr = scanner.nextLine();
        double maxRate = Double.MAX_VALUE;
        if (!maxRateStr.isEmpty()) {
            try {
                maxRate = Double.parseDouble(maxRateStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Using no maximum rate limit.");
            }
        }
        
        List<Influencer> results = userService.searchInfluencers(niche, minFollowers, maxRate);
        
        System.out.println("\nSearch Results (" + results.size() + " influencers found):");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i).getUsername() + 
                              " - Niche: " + results.get(i).getNiche() + 
                              ", Rate: $" + results.get(i).getRate() + "/post");
        }
        
        if (!results.isEmpty()) {
            System.out.print("\nSelect influencers to invite (comma-separated numbers, 0 to cancel): ");
            String selections = scanner.nextLine();
            
            if (!selections.equals("0")) {
                String[] selectionArray = selections.split(",");
                for (String sel : selectionArray) {
                    try {
                        int idx = Integer.parseInt(sel.trim()) - 1;
                        if (idx >= 0 && idx < results.size()) {
                            Influencer selected = results.get(idx);
                            campaign.inviteInfluencer(selected);
                            System.out.println("Invitation sent to " + selected.getUsername() + "!");
                            
                            // Send notification
                            notificationService.addNotification(selected.getUsername(), 
                                                           "You've been invited to join the campaign: " + campaign.getName());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid selection: " + sel);
                    }
                }
                
                try {
                    fileService.saveCampaignData(campaignService);
                } catch (IOException e) {
                    System.out.println("Error saving campaign data: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * View campaign analytics.
     */
    private static void viewCampaignAnalytics() {
        System.out.println("\n----- Campaign Analytics -----");
        
        if (!(currentUser instanceof Brand)) {
            System.out.println("This feature is only available for brands.");
            return;
        }
        
        Brand brand = (Brand) currentUser;
        List<Campaign> campaigns = brand.getActiveCampaigns();
        
        if (campaigns.isEmpty()) {
            System.out.println("No active campaigns to analyze.");
            return;
        }
        
        System.out.println("Select a campaign to view analytics:");
        for (int i = 0; i < campaigns.size(); i++) {
            System.out.println((i + 1) + ". " + campaigns.get(i).getName());
        }
        
        System.out.print("Enter choice: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice > 0 && choice <= campaigns.size()) {
                Campaign selectedCampaign = campaigns.get(choice - 1);
                analyticsService.displayCampaignPerformance(selectedCampaign);
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
    
    /**
     * View user analytics.
     */
    private static void viewAnalytics() {
        System.out.println("\n----- Analytics Dashboard -----");
        
        if (currentUser instanceof Influencer) {
            Influencer influencer = (Influencer) currentUser;
            analyticsService.displayInfluencerAnalytics(influencer);
        } else if (currentUser instanceof Brand) {
            Brand brand = (Brand) currentUser;
            analyticsService.displayBrandAnalytics(brand);
        } else if (currentUser instanceof Advertiser) {
            Advertiser advertiser = (Advertiser) currentUser;
            analyticsService.displayAdvertiserAnalytics(advertiser);
        }
    }
    
    private static void viewCampaignPerformance(Campaign campaign) {
        System.out.println("\n----- Campaign Performance -----");
        analyticsService.displayCampaignPerformance(campaign);
    }
    
    private static void updateContentStatus(Campaign campaign) {
        Influencer influencer = (Influencer) currentUser;
        
        System.out.println("\n----- Update Content Status -----");
        System.out.println("Current status: " + campaign.getInfluencerStatus(influencer));
        
        System.out.println("\nUpdate status to:");
        System.out.println("1. Content Created");
        System.out.println("2. Content Published");
        System.out.println("3. Report Engagement Metrics");
        System.out.print("Select an option (0 to cancel): ");
        
        try {
            int option = Integer.parseInt(scanner.nextLine());
            
            switch (option) {
                case 1:
                    campaign.updateInfluencerStatus(influencer, "Content Created");
                    System.out.println("Status updated to: Content Created");
                    break;
                case 2:
                    campaign.updateInfluencerStatus(influencer, "Content Published");
                    
                    // Ask for content links
                    System.out.print("Enter content URL: ");
                    String contentUrl = scanner.nextLine();
                    campaign.addContentUrl(influencer, contentUrl);
                    
                    System.out.println("Status updated to: Content Published");
                    break;
                case 3:
                    // Ask for engagement metrics
                    System.out.print("Enter number of likes: ");
                    int likes = Integer.parseInt(scanner.nextLine());
                    
                    System.out.print("Enter number of comments: ");
                    int comments = Integer.parseInt(scanner.nextLine());
                    
                    System.out.print("Enter number of shares: ");
                    int shares = Integer.parseInt(scanner.nextLine());
                    
                    campaign.updateEngagementMetrics(influencer, likes, comments, shares);
                    System.out.println("Engagement metrics updated successfully!");
                    break;
            }
            
            try {
                fileService.saveCampaignData(campaignService);
            } catch (IOException e) {
                System.out.println("Error saving campaign data: " + e.getMessage());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewBrandClients() {
        // For an advertiser managing multiple brands
        System.out.println("\n----- Brand Clients -----");
        Advertiser advertiser = (Advertiser) currentUser;
        
        // In a real app, this would load from a database
        System.out.println("This feature would display brand clients for an advertiser.");
        System.out.println("Currently not implemented in this demo version.");
    }
    
    private static void manageBrandCampaigns() {
        // For an advertiser managing campaigns on behalf of brands
        System.out.println("\n----- Manage Brand Campaigns -----");
        
        // In a real app, this would load from a database
        System.out.println("This feature would allow advertisers to manage campaigns on behalf of brands.");
        System.out.println("Currently not implemented in this demo version.");
    }
    
    /**
     * Inner class demonstrating nested class requirement.
     * This class represents an analytics dashboard widget.
     */
    public static class DashboardWidget {
        private String widgetName;
        private String dataSource;
        
        public DashboardWidget(String widgetName, String dataSource) {
            this.widgetName = widgetName;
            this.dataSource = dataSource;
        }
        
        public void display() {
            System.out.println("Widget: " + widgetName);
            System.out.println("Data Source: " + dataSource);
            System.out.println("Displaying mock visualization...");
        }
    }
}
