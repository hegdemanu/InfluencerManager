# UML Diagram for Influencer Manager Platform

## Class Diagram

```mermaid
classDiagram
    %% Interfaces
    class Manageable
    class Analyzable

    %% Abstract
    class User {
        +int id
        +String username
        +String email
        +String password
        +authenticate(): boolean
        +updateProfile(): void
    }

    %% Role entities
    User <|-- Admin
    User <|-- Advertiser
    User <|-- Brand
    User <|-- Influencer {
        +int followers
        +double engagementRate
        +String niche
    }

    %% Core domain
    class Campaign {
        +String name
        +double budget
        +startDate
        +endDate
        +inviteInfluencer(i:Influencer)
        +inviteInfluencers(i:Influencer...)  %% var-arg overload
    }
    class Contract
    class Payment

    Brand      --> "0..*" Campaign      : creates
    Influencer <-- "0..*" Campaign      : participates
    Campaign   --> "1..*" Contract      : governed by
    Contract   --> "1..*" Payment       : triggers

    %% Services
    class UserService
    class CampaignService
    class AnalyticsService
    class RecommendationService
    class NotificationService
    class FileService

    Manageable  <|.. CampaignService
    Analyzable  <|.. AnalyticsService

    %% Utilities
    class AuthenticationManager
    class DatabaseManager

    UserService         --> AuthenticationManager
    CampaignService     --> DatabaseManager
    AnalyticsService    --> DatabaseManager
    RecommendationService --> AnalyticsService
    FileService         --> DatabaseManager

    %% Exceptions
    class AuthenticationException
    class DataProcessingException
    AuthenticationManager --> AuthenticationException
    DatabaseManager       --> DataProcessingException