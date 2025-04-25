package com.influencerManager.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a payment transaction in the Influencer Manager platform.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String paymentId;
    private Contract contract;
    private Campaign campaign;
    private Influencer influencer;
    private User brand;
    private double amount;
    private String currency;
    private String status;
    private String paymentMethod;
    private String transactionId;
    private Date paymentDate;
    private Date createdAt;
    
    /**
     * Default constructor.
     */
    public Payment() {
        this.paymentId = UUID.randomUUID().toString();
        this.currency = "USD";
        this.status = "Pending";
        this.createdAt = new Date();
    }
    
    /**
     * Constructor with basic payment information.
     * 
     * @param contract The associated contract
     * @param amount The payment amount
     */
    public Payment(Contract contract, double amount) {
        this();
        this.contract = contract;
        this.campaign = contract.getCampaign();
        this.influencer = contract.getInfluencer();
        this.brand = contract.getBrand();
        this.amount = amount;
    }
    
    /**
     * Constructor with comprehensive payment information.
     * 
     * @param contract The associated contract
     * @param amount The payment amount
     * @param currency The currency
     * @param paymentMethod The payment method
     */
    public Payment(Contract contract, double amount, String currency, String paymentMethod) {
        this(contract, amount);
        this.currency = currency;
        this.paymentMethod = paymentMethod;
    }
    
    // Getters and setters
    
    public String getPaymentId() {
        return paymentId;
    }
    
    public Contract getContract() {
        return contract;
    }
    
    public void setContract(Contract contract) {
        this.contract = contract;
    }
    
    public Campaign getCampaign() {
        return campaign;
    }
    
    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }
    
    public Influencer getInfluencer() {
        return influencer;
    }
    
    public void setInfluencer(Influencer influencer) {
        this.influencer = influencer;
    }
    
    public User getBrand() {
        return brand;
    }
    
    public void setBrand(User brand) {
        this.brand = brand;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public Date getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Process the payment.
     * 
     * @param paymentMethod The payment method to use
     * @return true if successful, false otherwise
     */
    public boolean processPayment(String paymentMethod) {
        // In a real app, this would integrate with a payment gateway
        this.paymentMethod = paymentMethod;
        this.status = "Processing";
        
        // Simulate payment processing
        boolean success = simulatePaymentProcessing();
        
        if (success) {
            this.status = "Completed";
            this.paymentDate = new Date();
            this.transactionId = "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            // Update influencer's earnings
            influencer.completeCampaign(campaign, amount);
            
            // Update brand's spent amount (if it's a Brand object)
            if (brand instanceof Brand) {
                ((Brand) brand).completeCampaign(campaign, amount);
            }
            
            return true;
        } else {
            this.status = "Failed";
            return false;
        }
    }
    
    /**
     * Simulate payment processing (would be replaced with actual payment gateway in a real app).
     * 
     * @return true for successful payment
     */
    private boolean simulatePaymentProcessing() {
        // For demo purposes, 95% chance of success
        return Math.random() < 0.95;
    }
    
    /**
     * Cancel the payment.
     * 
     * @param reason The reason for cancellation
     * @return true if successful, false if payment already completed
     */
    public boolean cancelPayment(String reason) {
        if (status.equals("Completed")) {
            return false;
        }
        
        status = "Cancelled";
        return true;
    }
    
    /**
     * Generate a receipt for the payment.
     * 
     * @return The receipt as a formatted string
     */
    public String generateReceipt() {
        if (!status.equals("Completed")) {
            return "Payment not completed yet. No receipt available.";
        }
        
        StringBuilder receipt = new StringBuilder();
        
        receipt.append("PAYMENT RECEIPT\n");
        receipt.append("===============\n\n");
        
        receipt.append("Receipt ID: ").append(paymentId).append("\n");
        receipt.append("Transaction ID: ").append(transactionId).append("\n");
        receipt.append("Date: ").append(paymentDate).append("\n\n");
        
        receipt.append("PAYMENT DETAILS\n");
        receipt.append("---------------\n");
        receipt.append("Campaign: ").append(campaign.getName()).append("\n");
        receipt.append("Brand: ").append(brand instanceof Brand ? ((Brand)brand).getCompanyName() : brand.getUsername()).append("\n");
        receipt.append("Influencer: ").append(influencer.getUsername()).append("\n");
        receipt.append("Amount: ").append(currency).append(" ").append(String.format("%.2f", amount)).append("\n");
        receipt.append("Payment Method: ").append(paymentMethod).append("\n");
        receipt.append("Status: ").append(status).append("\n\n");
        
        receipt.append("Thank you for using Influencer Manager Platform!\n");
        
        return receipt.toString();
    }
    
    /**
     * Returns a string representation of the Payment object.
     * 
     * @return String representation of the Payment
     */
    @Override
    public String toString() {
        return "Payment ID: " + paymentId +
               "\nCampaign: " + (campaign != null ? campaign.getName() : "N/A") +
               "\nInfluencer: " + (influencer != null ? influencer.getUsername() : "N/A") +
               "\nBrand: " + (brand != null ? 
                   (brand instanceof Brand ? ((Brand)brand).getCompanyName() : brand.getUsername()) : "N/A") +
               "\nAmount: " + currency + " " + String.format("%.2f", amount) +
               "\nStatus: " + status +
               "\nPayment Method: " + (paymentMethod != null ? paymentMethod : "Not specified") +
               "\nTransaction ID: " + (transactionId != null ? transactionId : "Not yet processed") +
               "\nPayment Date: " + (paymentDate != null ? paymentDate : "Pending") +
               "\nCreated: " + createdAt;
    }
    
    /**
     * Vararg method to process multiple payments at once.
     * This demonstrates vararg overloading requirement.
     * 
     * @param method The payment method to use
     * @param payments The payments to process
     * @return The number of successful payments
     */
    public static int processMultiplePayments(String method, Payment... payments) {
        int successCount = 0;
        
        for (Payment payment : payments) {
            if (payment.processPayment(method)) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    /**
     * Vararg method to cancel multiple payments at once.
     * This demonstrates vararg overloading requirement.
     * 
     * @param reason The reason for cancellation
     * @param payments The payments to cancel
     * @return The number of successfully cancelled payments
     */
    public static int cancelMultiplePayments(String reason, Payment... payments) {
        int cancelCount = 0;
        
        for (Payment payment : payments) {
            if (payment.cancelPayment(reason)) {
                cancelCount++;
            }
        }
        
        return cancelCount;
    }
}
