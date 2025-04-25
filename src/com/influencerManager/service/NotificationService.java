package com.influencerManager.service;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Service class to handle notifications.
 * This class manages sending notifications to users.
 * It implements Runnable to support running as a thread.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public class NotificationService implements Runnable {
    private Queue<Notification> notificationQueue;
    private Map<String, List<String>> userNotifications;
    private boolean running;
    private FileService fileService;
    
    /**
     * Default constructor.
     */
    public NotificationService() {
        this.notificationQueue = new ConcurrentLinkedQueue<>();
        this.userNotifications = new HashMap<>();
        this.running = true;
    }
    
    /**
     * Constructor with file service.
     * 
     * @param fileService The file service for logging
     */
    public NotificationService(FileService fileService) {
        this();
        this.fileService = fileService;
    }
    
    /**
     * Set the file service.
     * 
     * @param fileService The file service
     */
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
    
    /**
     * Add a notification to the queue.
     * 
     * @param username The recipient's username
     * @param message The notification message
     */
    public void addNotification(String username, String message) {
        Notification notification = new Notification(username, message);
        notificationQueue.add(notification);
        
        // Store in user's notification list for later retrieval
        userNotifications.putIfAbsent(username, new ArrayList<>());
        userNotifications.get(username).add(message);
        
        // Log the notification
        if (fileService != null) {
            fileService.logMessage("Notification added for " + username + ": " + message);
        }
    }
    
    /**
     * Add a notification to the queue.
     * 
     * @param notification The notification to add
     */
    public void addNotification(Notification notification) {
        if (notification != null) {
            notificationQueue.add(notification);
            
            // Store in user's notification list
            String username = notification.getUsername();
            userNotifications.putIfAbsent(username, new ArrayList<>());
            userNotifications.get(username).add(notification.getMessage());
            
            // Log the notification
            if (fileService != null) {
                fileService.logMessage("Notification added for " + username + 
                                     ": " + notification.getMessage());
            }
        }
    }
    
    /**
     * Get notifications for a specific user.
     * 
     * @param username The username
     * @return List of notification messages
     */
    public List<String> getNotificationsForUser(String username) {
        return userNotifications.getOrDefault(username, new ArrayList<>());
    }
    
    /**
     * Clear notifications for a specific user.
     * 
     * @param username The username
     */
    public void clearNotificationsForUser(String username) {
        userNotifications.remove(username);
        
        // Log the action
        if (fileService != null) {
            fileService.logMessage("Notifications cleared for " + username);
        }
    }
    
    /**
     * Mark notifications as read for a specific user.
     * 
     * @param username The username
     */
    public void markNotificationsAsRead(String username) {
        // In a real app, would update a "read" status rather than removing
        // For this demo, just log the action
        if (fileService != null) {
            fileService.logMessage("Notifications marked as read for " + username);
        }
    }
    
    /**
     * Send a notification to multiple users.
     * 
     * @param usernames List of usernames
     * @param message The notification message
     */
    public void sendBulkNotification(List<String> usernames, String message) {
        for (String username : usernames) {
            addNotification(username, message);
        }
    }
    
    /**
     * Overloaded version with varargs.
     * This demonstrates vararg overloading requirement.
     * 
     * @param message The notification message
     * @param usernames Variable number of usernames
     */
    public void sendBulkNotification(String message, String... usernames) {
        for (String username : usernames) {
            addNotification(username, message);
        }
    }
    
    /**
     * Stop the notification service thread.
     */
    public void stop() {
        running = false;
        
        // Log the action
        if (fileService != null) {
            fileService.logMessage("Notification service stopped");
        }
    }
    
    /**
     * Get the number of pending notifications.
     * 
     * @return The queue size
     */
    public int getPendingNotificationCount() {
        return notificationQueue.size();
    }
    
    /**
     * Get the total number of notifications (sent and pending).
     * 
     * @return The total count
     */
    public int getTotalNotificationCount() {
        int total = notificationQueue.size();
        
        for (List<String> userNotifs : userNotifications.values()) {
            total += userNotifs.size();
        }
        
        return total;
    }
    
    /**
     * The run method for the thread.
     * Processes notifications from the queue.
     */
    @Override
    public void run() {
        if (fileService != null) {
            fileService.logMessage("Notification service started");
        }
        
        while (running) {
            try {
                // Process notifications from the queue
                Notification notification = notificationQueue.poll();
                
                if (notification != null) {
                    // In a real app, would send email, push notification, etc.
                    // For demo, just log the notification
                    System.out.println("[Notification for " + notification.getUsername() + 
                                      "]: " + notification.getMessage());
                    
                    if (fileService != null) {
                        fileService.logMessage("Notification processed for " + notification.getUsername() + 
                                            ": " + notification.getMessage());
                    }
                }
                
                // Sleep to avoid CPU hogging
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                if (fileService != null) {
                    fileService.logError("Notification service interrupted: " + e.getMessage());
                }
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                if (fileService != null) {
                    fileService.logError("Error in notification service: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Nested class representing a notification.
     * This demonstrates the nested class requirement.
     */
    public static class Notification {
        private String username;
        private String message;
        private Date timestamp;
        
        public Notification(String username, String message) {
            this.username = username;
            this.message = message;
            this.timestamp = new Date();
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getMessage() {
            return message;
        }
        
        public Date getTimestamp() {
            return timestamp;
        }
        
        @Override
        public String toString() {
            return "[" + timestamp + "] " + username + ": " + message;
        }
    }
}
