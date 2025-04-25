package com.influencerManager.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a contract between a brand and an influencer for a specific campaign.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class Contract implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String contractId;
    private Campaign campaign;
    private Influencer influencer;
    private User brand;
    private double paymentAmount;
    private String paymentTerms;
    private String deliverables;
    private String startDate;
    private String endDate;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private boolean isSigned;
    
    /**
     * Default constructor.
     */
    public Contract() {
        this.contractId = UUID.randomUUID().toString();
        this.status = "Draft";
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.isSigned = false;
    }
    
    /**
     * Constructor with campaign and parties.
     * 
     * @param campaign The associated campaign
     * @param influencer The influencer
     * @param brand The brand
     */
    public Contract(Campaign campaign, Influencer influencer, User brand) {
        this();
        this.campaign = campaign;
        this.influencer = influencer;
        this.brand = brand;
        this.startDate = campaign.getStartDate();
        this.endDate = campaign.getEndDate();
        
        // Calculate default payment
        this.paymentAmount = calculateDefaultPayment();
        this.paymentTerms = "Payment will be processed within 30 days of campaign completion";
        this.deliverables = "Content creation and posting as per campaign requirements";
    }
    
    /**
     * Constructor with all contract details.
     * 
     * @param campaign The associated campaign
     * @param influencer The influencer
     * @param brand The brand
     * @param paymentAmount The payment amount
     * @param paymentTerms The payment terms
     * @param deliverables The deliverables
     * @param startDate The start date
     * @param endDate The end date
     */
    public Contract(Campaign campaign, Influencer influencer, User brand,
                   double paymentAmount, String paymentTerms, String deliverables,
                   String startDate, String endDate) {
        this(campaign, influencer, brand);
        this.paymentAmount = paymentAmount;
        this.paymentTerms = paymentTerms;
        this.deliverables = deliverables;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    // Getters and setters
    
    public String getContractId() {
        return contractId;
    }
    
    public Campaign getCampaign() {
        return campaign;
    }
    
    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
        this.updatedAt = new Date();
    }
    
    public Influencer getInfluencer() {
        return influencer;
    }
    
    public void setInfluencer(Influencer influencer) {
        this.influencer = influencer;
        this.updatedAt = new Date();
    }
    
    public User getBrand() {
        return brand;
    }
    
    public void setBrand(User brand) {
        this.brand = brand;
        this.updatedAt = new Date();
    }
    
    public double getPaymentAmount() {
        return paymentAmount;
    }
    
    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
        this.updatedAt = new Date();
    }
    
    public String getPaymentTerms() {
        return paymentTerms;
    }
    
    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
        this.updatedAt = new Date();
    }
    
    public String getDeliverables() {
        return deliverables;
    }
    
    public void setDeliverables(String deliverables) {
        this.deliverables = deliverables;
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
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public boolean isSigned() {
        return isSigned;
    }
    
    /**
     * Calculate the default payment amount based on influencer rate.
     * 
     * @return The calculated payment amount
     */
    private double calculateDefaultPayment() {
        if (influencer != null) {
            // In a real app, this might be more complex based on campaign requirements
            return influencer.getRate();
        }
        return 0.0;
    }
    
    /**
     * Sign the contract by the influencer.
     * 
     * @return true if successful, false if already signed
     */
    public boolean signContract() {
        if (!isSigned) {
            isSigned = true;
            status = "Active";
            updatedAt = new Date();
            return true;
        }
        return false;
    }
    
    /**
     * Terminate the contract.
     * 
     * @param reason The reason for termination
     * @return true if successful, false if not active
     */
    public boolean terminateContract(String reason) {
        if (status.equals("Active")) {
            status = "Terminated";
            updatedAt = new Date();
            return true;
        }
        return false;
    }
    
    /**
     * Complete the contract.
     * 
     * @return true if successful, false if not active
     */
    public boolean completeContract() {
        if (status.equals("Active")) {
            status = "Completed";
            updatedAt = new Date();
            
            // In a real app, this would trigger payment processing
            
            return true;
        }
        return false;
    }
    
    /**
     * Generate a formatted contract document.
     * 
     * @return The contract as a formatted string
     */
    public String generateContractDocument() {
        StringBuilder document = new StringBuilder();
        
        document.append("CONTRACT AGREEMENT\n");
        document.append("=================\n\n");
        
        document.append("Contract ID: ").append(contractId).append("\n");
        document.append("Date Created: ").append(createdAt).append("\n\n");
        
        document.append("PARTIES\n");
        document.append("------\n");
        document.append("Brand: ").append(brand instanceof Brand ? ((Brand)brand).getCompanyName() : brand.getUsername()).append("\n");
        document.append("Influencer: ").append(influencer.getUsername()).append("\n\n");
        
        document.append("CAMPAIGN DETAILS\n");
        document.append("----------------\n");
        document.append("Campaign Name: ").append(campaign.getName()).append("\n");
        document.append("Description: ").append(campaign.getDescription()).append("\n");
        document.append("Duration: ").append(startDate).append(" to ").append(endDate).append("\n\n");
        
        document.append("TERMS\n");
        document.append("-----\n");
        document.append("Deliverables: ").append(deliverables).append("\n\n");
        document.append("Payment Amount: $").append(String.format("%.2f", paymentAmount)).append("\n");
        document.append("Payment Terms: ").append(paymentTerms).append("\n\n");
        
        document.append("SIGNATURES\n");
        document.append("----------\n");
        document.append("Brand Representative: ____________________\n\n");
        document.append("Influencer: ____________________\n\n");
        
        return document.toString();
    }
    
    /**
     * Returns a string representation of the Contract object.
     * 
     * @return String representation of the Contract
     */
    @Override
    public String toString() {
        return "Contract ID: " + contractId +
               "\nCampaign: " + campaign.getName() +
               "\nBrand: " + (brand instanceof Brand ? ((Brand)brand).getCompanyName() : brand.getUsername()) +
               "\nInfluencer: " + influencer.getUsername() +
               "\nPayment: $" + String.format("%.2f", paymentAmount) +
               "\nDuration: " + startDate + " to " + endDate +
               "\nStatus: " + status +
               "\nSigned: " + (isSigned ? "Yes" : "No") +
               "\nCreated: " + createdAt +
               "\nLast Updated: " + updatedAt;
    }
}
