# Rubrics Table: Java Feature Implementation in Influencer Manager Platform

This document provides a comprehensive overview of how the Influencer Manager Platform implements the required Java features specified in the assignment requirements.

## Core Java Features Implementation Table

| Feature | Count | Implementation Locations |
|---------|-------|--------------------------|
| **Overloaded Methods** | 4+ | `Brand.formatBudget()` with and without decimal places parameter<br>`Campaign.formatBudget()` with and without decimal places parameter<br>`User.updateInfo()` with different parameter combinations |
| **Overloaded Constructors** | 10+ | All model classes (`User`, `Influencer`, `Brand`, `Advertiser`, `Admin`, etc.) have multiple constructors<br>Example: `Influencer` has 3 constructors with different parameter sets |
| **Vararg Overloading** | 3 | `Advertiser.addBrands(Brand... brands)`<br>`Advertiser.addCampaigns(Campaign... campaigns)`<br>`NotificationService.sendBulkNotification(String message, String... usernames)` |
| **Nested Classes** | 2 | `Influencer.SocialMediaProfile` - non-static nested class<br>`NotificationService.Notification` - static nested class<br>`InfluencerManagerApp.DashboardWidget` - static nested class |
| **Abstract Class** | 1 | `User` abstract class - base class for all user types |
| **Interface** | 2 | `Manageable` interface - implemented by `Campaign`<br>`Analyzable` interface - implemented by `Campaign` |
| **Hierarchical Inheritance** | 1 | User hierarchy: `User` -> `Influencer`, `Brand`, `Advertiser`, `Admin` |
| **Multiple Inheritance** | 1 | `Campaign` implements both `Manageable` and `Analyzable` interfaces |
| **Wrappers** | Multiple | Used throughout the code:<br>- Primitive wrapper classes (Integer, Double, Boolean)<br>- Collection wrappers (List, Map, etc.)<br>Example: `Integer.parseInt()`, `Double.parseDouble()` in user input handling |
| **Package** | 6 | `com.influencerManager.model`<br>`com.influencerManager.service`<br>`com.influencerManager.util`<br>`com.influencerManager.exception`<br>`com.influencerManager.interface`<br>`com.influencerManager.main` |
| **Exception Handling** | 5+ | Custom exceptions: `AuthenticationException`, `DataProcessingException`<br>Try-catch blocks in file operations, user input processing<br>Example: Exception handling in `FileService.loadUserData()` |
| **I/O: File Handling** | Multiple | `FileService` class handles all file operations<br>- Reading/writing user data and campaign data to files<br>- Log file management<br>- CSV import/export functionality |
| **Scanner Class** | Multiple | Used in `InfluencerManagerApp` for user input<br>Example: Reading user selections from console menus |
| **Multithreading** | 1 | `NotificationService` implements `Runnable` interface<br>Thread started in `InfluencerManagerApp.initializeSystem()` to process notifications asynchronously |

## Additional Feature Implementations

| Feature | Implementation Details |
|---------|------------------------|
| **Collections Framework** | Extensive use of Collections throughout the application:<br>- Lists for storing users, campaigns, etc.<br>- Maps for lookups and caching<br>- Queues for notification processing |
| **Serialization** | All model classes implement `Serializable` for persistence:<br>- User data serialized to files<br>- Campaign data serialized to files |
| **Functional Programming** | Java Streams API used in multiple services:<br>- Filtering users and campaigns<br>- Transforming collections<br>Example: `UserService.getUsersByType()` |
| **Encapsulation** | Private fields with getters/setters throughout all classes<br>Example: All model class attributes are private with controlled access |
| **Polymorphism** | Method overriding in user subclasses<br>Example: `getProfileInfo()` implemented differently in each user type |

## Demonstration of Requirements

### Overloaded Methods
The project demonstrates method overloading in multiple locations, providing different parameter options for the same functionality. For example, `Brand.formatBudget()` can be called with or without specifying decimal places.

### Overloaded Constructors
All model classes provide multiple constructors for flexible object creation. Users can be created with minimal information or with comprehensive attribute sets.

### Vararg Overloading
The project uses variable arguments in methods like `Advertiser.addBrands()` to allow adding multiple brands in a single method call.

### Nested Classes
Both static and non-static nested classes are used to encapsulate related functionality. `Influencer.SocialMediaProfile` demonstrates how a nested class can represent a component of the outer class.

### Abstract Class and Interface
The project uses abstraction through the `User` abstract class and interfaces like `Manageable` and `Analyzable` to define contracts that implementing classes must fulfill.

### Inheritance and Polymorphism
The user hierarchy demonstrates both inheritance (extending the `User` class) and polymorphism (overriding methods like `getProfileInfo()`).

### Exception Handling
Custom exceptions and robust try-catch blocks ensure that errors are handled gracefully throughout the application.

### File I/O and Threading
The project demonstrates file operations for data persistence and multithreading for background notification processing.

## Conclusion

The Influencer Manager Platform successfully implements all required Java features, often exceeding the minimum requirements. The application demonstrates a thorough understanding of object-oriented programming principles and advanced Java concepts.
