# Influencer Manager Platform

## Overview
The Influencer Manager Platform is a comprehensive Java application that connects brands with influencers for marketing campaigns. The platform facilitates campaign management, influencer discovery, contract management, and performance tracking across major social media platforms like Instagram, YouTube, TikTok, and Twitter.

> **TL;DR**  
> A pure-Java, OOP-first command-line application that lets brands, advertisers, and influencers run an entire sponsorship cycle—from discovery to payout—without touching a spreadsheet.

---
## Features

### Role-Based Access
- **Influencers**: Create profiles, showcase statistics, receive and manage campaign offers
- **Brands**: Discover influencers, create and manage campaigns, track performance
- **Advertisers**: Manage multiple brands and their campaigns
- **Admins**: System management and oversight

### Core Functionality
- User profile management
- Campaign creation and management
- Influencer discovery and filtering
- Contract management
- Payment processing
- Real-time analytics and reporting
- AI-driven influencer recommendations

## Technical Details

### Architecture
The application follows object-oriented programming principles and is built using core Java. It uses a layered architecture:

- **Model Layer**: Domain objects representing system entities
- **Service Layer**: Business logic and operations
- **Utility Layer**: Helper classes and utilities
- **Interface Layer**: Defines contracts for implementation
- **Exception Layer**: Custom exceptions for error handling

### OOP Concepts Implemented
- **Inheritance**: Hierarchical class structure (User as base class)
- **Encapsulation**: Private fields with getters/setters
- **Polymorphism**: Method overriding and overloading
- **Abstraction**: Abstract classes and interfaces

### Java Features Utilized
- Abstract classes
- Interfaces
- Nested classes
- Method overloading
- Constructor overloading
- Varargs methods
- Exception handling
- File I/O operations
- Multithreading
- Collections Framework

## Project Structure
---------------------
```
InfluencerManager/
├─ src/
│  └─ com/
│     └─ influencerManager/
│        ├─ main/
│        │  └─ InfluencerManagerApp.java
│        ├─ model/
│        │  ├─ User.java           ← superclass
│        │  ├─ Admin.java          │        \
│        │  ├─ Advertiser.java     │         > role entities
│        │  ├─ Brand.java          │        /
│        │  ├─ Influencer.java     ├─ Campaign.java
│        │  ├─ Contract.java       └─ Payment.java
│        ├─ service/
│        │  ├─ UserService.java
│        │  ├─ CampaignService.java
│        │  ├─ AnalyticsService.java
│        │  ├─ RecommendationService.java
│        │  ├─ NotificationService.java
│        │  └─ FileService.java
│        ├─ interfaces/
│        │  ├─ Analyzable.java
│        │  └─ Manageable.java
│        ├─ util/
│        │  ├─ AuthenticationManager.java
│        │  └─ DatabaseManager.java
│        └─ exception/
│           ├─ AuthenticationException.java
│           └─ DataProcessingException.java
├─ users.dat                ← demo data dump (serialized)
├─ system.log               ← simple activity log
├─ InfluencerPlatform_UML.md
├─ RubricsTable.md
├─ .classpath  •  .project  ← Eclipse artefacts
└─ .gitattributes / .DS_Store
```

## ✨ Key Features
| Category | Highlights |
| -------- | ---------- |
| **Role-based Access** | `Admin`, `Advertiser`, `Brand`, `Influencer` classes inherit from `User` for clean polymorphism. |
| **Campaign Lifecycle** | Create → approve contracts → release payments → live analytics. |
| **AI-ready Hooks** | `RecommendationService` stub designed for later ML model plug-in. |
| **Realtime-ish Metrics** | `AnalyticsService` crunches engagement stats; swap in a live API later. |
| **Secure Auth** | `AuthenticationManager` + custom `AuthenticationException` guard every entry point. |
| **File-Safe** | `FileService` persists objects to `users.dat`, so your demo data survives a reboot. |

---

## 🏗️ Architecture

* **Layered** (`model → service → util/interface → main`)  
* 100 % vanilla **Java 17** (no external deps)  
* Thread-safe **synchronized** sections where IO might collide  
* Eclipse `.classpath/.project` checked in for one-click import

The class diagram lives in **`InfluencerPlatform_UML.md`**.

---
## 🚀 Getting Started

### Prerequisites
* JDK 17 + (tested on 17.0.11)  
* Any IDE **or** plain old `javac`

### Compile & Run
```bash
# step into repo root
javac -d out $(find src -name '*.java')
java  -cp out  com.influencerManager.main.InfluencerManagerApp
```
> On Windows, replace `$(find …)` with a `dir /b /s` equivalent.

### Sample Login Flow
1. Start the app → choose **Brand** login.  
2. Create a campaign, filter influencers (`RecommendationService` gives a ranked list).  
3. Auto-generate a contract; approve; payment recorded in `system.log`.  

---

## 🔧 Extending the Platform
| Hook point | How to wire your own logic |
| ---------- | ------------------------- |
| **Recommendation engine** | Implement `Manageable` + `Analyzable`, inject into `RecommendationService`. |
| **Database** | Swap `DatabaseManager`’s file IO with JDBC; the rest of the code stays untouched (encapsulation FTW). |
| **REST API** | Wrap `service` layer with Spring/Quarkus controllers—model classes already serialise cleanly. |


## 📜 Acknowledgements
This repo has began as a university OOP design assignment, this will grow legs, arms, based on caffeine dependency. Thanks to the Java community for best-practice inspo.
```

---

**Brutally honest footnote:** The code compiles, but there’s zero persistence beyond flat files and the “AI-driven recommendations” are currently a glorified TODO. Ship it anyway, then iterate. Perfection is for poetry, not software.
---
```

