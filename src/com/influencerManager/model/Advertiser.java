package com.influencerManager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Advertiser user in the Influencer Manager platform.
 * Advertisers can manage multiple brands and their campaigns.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class Advertiser extends User {
    private static final long serialVersionUID = 1L;
    
    private String agencyName;
    private String contactPerson;
    private String phone;
    private List<Brand> managedBrands;
    private List<Campaign> managedCampaigns;
    private int totalClients;
    private double commission;
    
    /**
     * Default constructor.
     */
    public Advertiser() {
        super();
        this.role = "Advertiser";
        this.managedBrands = new ArrayList<>();
        this.managedCampaigns = new ArrayList<>();
        this.totalClients = 0;
        this.commission = 10.0; // Default commission percentage
    }
    
    /**
     * Constructor with basic user information.
     * 
     * @param username The username
     * @param email The email address
     * @param password The password
     */
    public Advertiser(String username, String email, String password) {
        super(username, email, password);
        this.role = "Advertiser";
        this.managedBrands = new ArrayList<>();
        this.managedCampaigns = new ArrayList<>();
        this.totalClients = 0;
        this.commission = 10.0; // Default commission percentage
    }
    
    /**
     * Constructor with all advertiser attributes.
     * 
     * @param username The username
     * @param email The email address
     * @param password The password
     * @param agencyName The agency name
     * @param contactPerson The contact person
     * @param phone The phone number
     * @param commission The commission percentage
     */
    public Advertiser(String username, String email, String password, 
                     String agencyName, String contactPerson, String phone, 
                     double commission) {
        super(username, email, password);
        this.role = "Advertiser";
        this.agencyName = agencyName;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.managedBrands = new ArrayList<>();
        this.managedCampaigns = new ArrayList<>();
        this.totalClients = 0;
        this.commission = commission;
    }
    
    // Getters and setters
    
    public String getAgencyName() {
        return agencyName;
    }
    
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }
    
    public String getContactPerson() {
        return contactPerson;
    }
    
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public List<Brand> getManagedBrands() {
        return managedBrands;
    }
    
    public List<Campaign> getManagedCampaigns() {
        return managedCampaigns;
    }
    
    public int getTotalClients() {
        return totalClients;
    }
    
    public double getCommission() {
        return commission;
    }
    
    public void setCommission(double commission) {
        this.commission = commission;
    }
    
    /**
     * Add a brand to be managed by this advertiser.
     * 
     * @param brand The brand to manage
     */
    public void addBrand(Brand brand) {
        if (!managedBrands.contains(brand)) {
            managedBrands.add(brand);
            totalClients++;
        }
    }
    
    /**
     * Remove a brand from this advertiser's management.
     * 
     * @param brand The brand to remove
     * @return true if successful, false if the brand wasn't managed
     */
    public boolean removeBrand(Brand brand) {
        boolean removed = managedBrands.remove(brand);
        if (removed) {
            totalClients--;
        }
        return removed;
    }
    
    /**
     * Add a campaign to be managed by this advertiser.
     * 
     * @param campaign The campaign to manage
     */
    public void addCampaign(Campaign campaign) {
        if (!managedCampaigns.contains(campaign)) {
            managedCampaigns.add(campaign);
        }
    }
    
    /**
     * Remove a campaign from this advertiser's management.
     * 
     * @param campaign The campaign to remove
     * @return true if successful, false if the campaign wasn't managed
     */
    public boolean removeCampaign(Campaign campaign) {
        return managedCampaigns.remove(campaign);
    }
    
    /**
     * Calculate total revenue from commissions.
     * 
     * @return The total revenue
     */
    public double calculateRevenue() {
        double total = 0.0;
        for (Campaign campaign : managedCampaigns) {
            total += campaign.getBudget() * (commission / 100);
        }
        return total;
    }
    
    /**
     * Returns a string representation of the Advertiser's profile.
     * 
     * @return String containing the Advertiser's profile information
     */
    @Override
    public String getProfileInfo() {
        return toString();
    }
    
    /**
     * Returns a string representation of the Advertiser object.
     * 
     * @return String representation of the Advertiser
     */
    @Override
    public String toString() {
        return super.toString() +
               "\nAgency Name: " + (agencyName != null ? agencyName : "Not specified") +
               "\nContact Person: " + (contactPerson != null ? contactPerson : "Not specified") +
               "\nPhone: " + (phone != null ? phone : "Not specified") +
               "\nManaged Brands: " + managedBrands.size() +
               "\nTotal Clients: " + totalClients +
               "\nCommission Rate: " + commission + "%" +
               "\nEstimated Revenue: $" + String.format("%.2f", calculateRevenue());
    }
    
    /**
     * Create a campaign for a managed brand.
     * 
     * @param brand The brand to create the campaign for
     * @param name The campaign name
     * @param description The campaign description
     * @param budget The campaign budget
     * @param startDate The start date
     * @param endDate The end date
     * @return The created Campaign or null if the brand is not managed
     */
    public Campaign createCampaignForBrand(Brand brand, String name, String description, 
                                         double budget, String startDate, String endDate) {
        if (managedBrands.contains(brand)) {
            Campaign campaign = brand.createCampaign(name, description, budget, startDate, endDate);
            addCampaign(campaign);
            return campaign;
        }
        return null;
    }
    
    /**
     * Vararg method to add multiple brands at once.
     * This demonstrates vararg overloading requirement.
     * 
     * @param brands The brands to add
     * @return The number of brands added
     */
    public int addBrands(Brand... brands) {
        int count = 0;
        for (Brand brand : brands) {
            if (!managedBrands.contains(brand)) {
                managedBrands.add(brand);
                totalClients++;
                count++;
            }
        }
        return count;
    }
    
    /**
     * Vararg method to add multiple campaigns at once.
     * This demonstrates vararg overloading requirement.
     * 
     * @param campaigns The campaigns to add
     * @return The number of campaigns added
     */
    public int addCampaigns(Campaign... campaigns) {
        int count = 0;
        for (Campaign campaign : campaigns) {
            if (!managedCampaigns.contains(campaign)) {
                managedCampaigns.add(campaign);
                count++;
            }
        }
        return count;
    }
}
