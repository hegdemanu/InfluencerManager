package com.influencerManager.service;

import com.influencerManager.model.*;
import com.influencerManager.exception.DataProcessingException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for file I/O operations.
 * This class handles saving and loading data to/from files.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class FileService {
    private static final String USER_DATA_FILE = "users.dat";
    private static final String CAMPAIGN_DATA_FILE = "campaigns.dat";
    private static final String LOG_FILE = "system.log";
    
    /**
     * Default constructor.
     */
    public FileService() {
        // Create log file if it doesn't exist
        try {
            File logFile = new File(LOG_FILE);
            if (!logFile.exists()) {
                logFile.createNewFile();
                logMessage("Log file created");
            }
        } catch (IOException e) {
            System.err.println("Could not create log file: " + e.getMessage());
        }
    }
    
    /**
     * Save user data to file.
     * 
     * @param userService The user service containing user data
     * @throws IOException If an I/O error occurs
     */
    public void saveUserData(UserService userService) throws IOException {
        if (userService == null) {
            throw new IOException("UserService is null");
        }
        
        List<User> users = userService.getAllUsers();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
            logMessage("User data saved: " + users.size() + " users");
        } catch (IOException e) {
            logError("Error saving user data: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Load user data from file.
     * 
     * @param userService The user service to load data into
     * @throws IOException If an I/O error occurs
     */
    @SuppressWarnings("unchecked")
    public void loadUserData(UserService userService) throws IOException {
        if (userService == null) {
            throw new IOException("UserService is null");
        }
        
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            logMessage("User data file not found. Creating new file on next save.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            List<User> users = (List<User>) ois.readObject();
            
            try {
                userService.setAllUsers(users);
                logMessage("User data loaded: " + users.size() + " users");
            } catch (DataProcessingException e) {
                logError("Error processing user data: " + e.getMessage());
                throw new IOException("Error processing user data: " + e.getMessage(), e);
            }
        } catch (ClassNotFoundException e) {
            logError("Error loading user data: Class not found - " + e.getMessage());
            throw new IOException("Error loading user data: Class not found", e);
        } catch (IOException e) {
            logError("Error loading user data: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Save campaign data to file.
     * 
     * @param campaignService The campaign service containing campaign data
     * @throws IOException If an I/O error occurs
     */
    public void saveCampaignData(CampaignService campaignService) throws IOException {
        if (campaignService == null) {
            throw new IOException("CampaignService is null");
        }
        
        List<Campaign> campaigns = campaignService.getAllCampaigns();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CAMPAIGN_DATA_FILE))) {
            oos.writeObject(campaigns);
            logMessage("Campaign data saved: " + campaigns.size() + " campaigns");
        } catch (IOException e) {
            logError("Error saving campaign data: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Load campaign data from file.
     * 
     * @param campaignService The campaign service to load data into
     * @throws IOException If an I/O error occurs
     */
    @SuppressWarnings("unchecked")
    public void loadCampaignData(CampaignService campaignService) throws IOException {
        if (campaignService == null) {
            throw new IOException("CampaignService is null");
        }
        
        File file = new File(CAMPAIGN_DATA_FILE);
        if (!file.exists()) {
            logMessage("Campaign data file not found. Creating new file on next save.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CAMPAIGN_DATA_FILE))) {
            List<Campaign> campaigns = (List<Campaign>) ois.readObject();
            
            try {
                campaignService.setAllCampaigns(campaigns);
                logMessage("Campaign data loaded: " + campaigns.size() + " campaigns");
            } catch (DataProcessingException e) {
                logError("Error processing campaign data: " + e.getMessage());
                throw new IOException("Error processing campaign data: " + e.getMessage(), e);
            }
        } catch (ClassNotFoundException e) {
            logError("Error loading campaign data: Class not found - " + e.getMessage());
            throw new IOException("Error loading campaign data: Class not found", e);
        } catch (IOException e) {
            logError("Error loading campaign data: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Export user data to a CSV file.
     * 
     * @param userService The user service
     * @param filePath The output file path
     * @throws IOException If an I/O error occurs
     */
    public void exportUsersToCsv(UserService userService, String filePath) throws IOException {
        if (userService == null) {
            throw new IOException("UserService is null");
        }
        
        List<User> users = userService.getAllUsers();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write header
            writer.println("Username,Email,Role,CreationDate,IsActive");
            
            // Write data
            for (User user : users) {
                writer.println(
                    user.getUsername() + "," +
                    user.getEmail() + "," +
                    user.getRole() + "," +
                    user.getCreationDate() + "," +
                    user.isActive()
                );
            }
            
            logMessage("User data exported to CSV: " + filePath);
        } catch (IOException e) {
            logError("Error exporting users to CSV: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Export campaign data to a CSV file.
     * 
     * @param campaignService The campaign service
     * @param filePath The output file path
     * @throws IOException If an I/O error occurs
     */
    public void exportCampaignsToCsv(CampaignService campaignService, String filePath) throws IOException {
        if (campaignService == null) {
            throw new IOException("CampaignService is null");
        }
        
        List<Campaign> campaigns = campaignService.getAllCampaigns();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write header
            writer.println("ID,Name,Brand,Budget,StartDate,EndDate,Status,InfluencerCount,Engagement");
            
            // Write data
            for (Campaign campaign : campaigns) {
                writer.println(
                    campaign.getId() + "," +
                    campaign.getName() + "," +
                    (campaign.getBrand() instanceof Brand ? 
                        ((Brand)campaign.getBrand()).getCompanyName() : 
                        campaign.getBrand().getUsername()) + "," +
                    campaign.getBudget() + "," +
                    campaign.getStartDate() + "," +
                    campaign.getEndDate() + "," +
                    campaign.getStatus() + "," +
                    campaign.getAcceptedInfluencers().size() + "," +
                    campaign.calculateTotalEngagement()
                );
            }
            
            logMessage("Campaign data exported to CSV: " + filePath);
        } catch (IOException e) {
            logError("Error exporting campaigns to CSV: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Import user data from a CSV file.
     * 
     * @param filePath The input file path
     * @return List of imported users
     * @throws IOException If an I/O error occurs
     */
    public List<User> importUsersFromCsv(String filePath) throws IOException {
        List<User> users = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            String line = reader.readLine();
            
            // Read data
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    continue; // Skip malformed lines
                }
                
                String username = parts[0];
                String email = parts[1];
                String role = parts[2];
                boolean isActive = Boolean.parseBoolean(parts[4]);
                
                // Create appropriate user type based on role
                User user;
                switch (role) {
                    case "Influencer":
                        user = new Influencer(username, email, "password123"); // Default password
                        break;
                    case "Brand":
                        user = new Brand(username, email, "password123");
                        break;
                    case "Advertiser":
                        user = new Advertiser(username, email, "password123");
                        break;
                    case "Admin":
                        user = new Admin(username, email, "password123");
                        break;
                    default:
                        continue; // Skip unknown roles
                }
                
                user.setActive(isActive);
                users.add(user);
            }
            
            logMessage("User data imported from CSV: " + users.size() + " users");
        } catch (IOException e) {
            logError("Error importing users from CSV: " + e.getMessage());
            throw e;
        }
        
        return users;
    }
    
    /**
     * Generate a system report file.
     * 
     * @param report The report content
     * @param reportName The report name
     * @throws IOException If an I/O error occurs
     */
    public void generateReportFile(String report, String reportName) throws IOException {
        String fileName = reportName.replaceAll("\\s+", "_").toLowerCase() + "_" + 
                        System.currentTimeMillis() + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.print(report);
            logMessage("Report generated: " + fileName);
        } catch (IOException e) {
            logError("Error generating report file: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Log a message to the log file.
     * 
     * @param message The message to log
     */
    public void logMessage(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            writer.println(timestamp + " [INFO] " + message);
        } catch (IOException e) {
            System.err.println("Could not write to log file: " + e.getMessage());
        }
    }
    
    /**
     * Log an error message to the log file.
     * 
     * @param message The error message to log
     */
    public void logError(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            writer.println(timestamp + " [ERROR] " + message);
        } catch (IOException e) {
            System.err.println("Could not write to log file: " + e.getMessage());
        }
    }
    
    /**
     * Backup all data files.
     * 
     * @throws IOException If an I/O error occurs
     */
    public void backupData() throws IOException {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        
        // Backup user data
        File userFile = new File(USER_DATA_FILE);
        if (userFile.exists()) {
            copyFile(userFile, new File(USER_DATA_FILE + "." + timestamp + ".bak"));
        }
        
        // Backup campaign data
        File campaignFile = new File(CAMPAIGN_DATA_FILE);
        if (campaignFile.exists()) {
            copyFile(campaignFile, new File(CAMPAIGN_DATA_FILE + "." + timestamp + ".bak"));
        }
        
        logMessage("Data backup created with timestamp: " + timestamp);
    }
    
    /**
     * Copy a file to another location.
     * 
     * @param source The source file
     * @param dest The destination file
     * @throws IOException If an I/O error occurs
     */
    private void copyFile(File source, File dest) throws IOException {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }
    
    /**
     * Check if data files exist.
     * 
     * @return true if both data files exist, false otherwise
     */
    public boolean dataFilesExist() {
        File userFile = new File(USER_DATA_FILE);
        File campaignFile = new File(CAMPAIGN_DATA_FILE);
        
        return userFile.exists() && campaignFile.exists();
    }
    
    /**
     * Delete all data files (for testing or reset).
     * 
     * @return true if successful, false otherwise
     */
    public boolean deleteAllData() {
        boolean success = true;
        
        File userFile = new File(USER_DATA_FILE);
        if (userFile.exists()) {
            success &= userFile.delete();
        }
        
        File campaignFile = new File(CAMPAIGN_DATA_FILE);
        if (campaignFile.exists()) {
            success &= campaignFile.delete();
        }
        
        if (success) {
            logMessage("All data files deleted");
        } else {
            logError("Failed to delete some data files");
        }
        
        return success;
    }
}
