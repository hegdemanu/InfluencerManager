```mermaid
classDiagram
    %% ========= Interfaces / Tags (implicit) =========
    class Manageable
    class Analyzable

    %% ========= Abstract / Super-class =========
    class User {
        +int id
        +String name
        +String email
        +String password
        +authenticate(): boolean
        +updateProfile(): void
    }

    %% ========= Role Entities =========
    class Admin
    class Advertiser
    class Brand
    class Influencer {
        +int followers
        +double engagementRate
        +String niche
    }

    User <|-- Admin
    User <|-- Advertiser
    User <|-- Brand
    User <|-- Influencer

    %% ========= Core Domain =========
    class Campaign {
        +int id
        +String title
        +double budget
        +LocalDate startDate
        +LocalDate endDate
        +CampaignStatus status
        +addInfluencer(i: Influencer)
        +launch(): void
    }

    class Contract {
        +int id
        +String terms
        +double amount
        +LocalDate signedDate
        +ContractStatus status
        +sign(): void
    }

    class Payment {
        +int id
        +double amount
        +LocalDate date
        +PaymentStatus status
        +process(): void
    }

    Brand      --> "0..*" Campaign      : creates
    Influencer <-- "0..*" Campaign      : participates
    Campaign   --> "1..*" Contract      : governed by
    Contract   --> "1..*" Payment       : triggers

    %% ========= Service Layer =========
    class UserService
    class CampaignService
    class AnalyticsService
    class RecommendationService
    class NotificationService
    class FileService

    Manageable  <|.. CampaignService
    Analyzable  <|.. AnalyticsService

    %% ========= Utilities =========
    class AuthenticationManager {
        +login(email,pw): boolean
        +logout(user): void
    }

    class DatabaseManager {
        +save(obj): void
        +find(id): Object
        +delete(id): void
    }

    UserService          --> AuthenticationManager
    CampaignService      --> DatabaseManager
    AnalyticsService     --> DatabaseManager
    RecommendationService--> AnalyticsService
    NotificationService  --> UserService
    FileService          --> DatabaseManager

    %% ========= Exceptions =========
    class AuthenticationException
    class DataProcessingException

    AuthenticationManager --> AuthenticationException
    DatabaseManager       --> DataProcessingException
