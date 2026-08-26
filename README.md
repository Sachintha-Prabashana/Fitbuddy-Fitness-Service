# ECA Gym Fitness Service

## 👤 Student Information
- **Name**: R.K. Sachintha Prabashana
- **Student ID**: 241722032
- **GCP Project ID**: `fitbuddy-505618`

## ## Project Description
The Fitness Service manages fitness evaluations, compiles weekly stats, generates PDF fitness reports, and uploads them to Google Cloud Storage.

## ## Technology Stack
- **Language**: Java 25
- **Framework**: Spring Boot 4.0.1 / 4.1.0
- **Database**: PostgreSQL (Production), MySQL (Dev)
- **Cloud Storage**: Google Cloud Storage (GCS) SDK
- **PDF Generation**: OpenPDF (LibrePDF)
- **Build Tool**: Maven

## ## Project Structure
```
fitness-service/
├── src/
│   ├── main/
│   │   ├── java/lk/ijse/eca/fitnessservice/
│   │   │   ├── FitnessServiceApplication.java # Entry point
│   │   │   ├── config/      # GCS client & Security configurations
│   │   │   ├── controller/  # REST Endpoints for PDF reports
│   │   │   ├── dto/         # Request & Response model contracts
│   │   │   ├── entity/      # JPA entities (FitnessReport)
│   │   │   ├── exception/   # Custom domain exceptions
│   │   │   ├── handler/     # Exception advice interceptor
│   │   │   ├── repository/  # Spring Data JPA Repositories
│   │   │   ├── security/    # JWT Validation & Security filters
│   │   │   └── service/     # Core Business logic, PDF generator & GCS client
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── application-dev.yaml
│   └── test/
├── pom.xml
└── README.md
```

## ## Setup / Getting Started Instructions
1. Navigate to the `fitness-service` directory.
2. Build the Maven package:
   ```bash
   ./mvnw clean install
   ```
3. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
4. By default, the service starts on a dynamic port and registers itself automatically with Eureka Service Discovery.
