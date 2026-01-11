Here is a professional and comprehensive README.md for your project, designed to highlight your technical expertise to recruiters and engineers.

School Microservices Management System
A robust, distributed educational management platform built with Java 21 and Spring Boot 3.5. This project demonstrates a modern microservices architecture designed for scalability, high-performance inter-service communication, and event-driven data processing.

🏗 System Architecture
The system is composed of five specialized microservices and a centralized entry point:

API Gateway: Built with Spring Cloud Gateway to provide a unified entry point, implementing custom JWT validation filters and security routing.

Auth Service: Manages user identity, roles, and permissions using RSA-signed JWT tokens and Spring Security.

Student Service: Core service for managing student profiles, guardian information, and attendance tracking.

School Course Service: Manages academic subjects, teacher assignments, and schedules.

Analytic Service: An event-driven engine that consumes data from Kafka to generate real-time dashboard summaries and attendance reports.

🛠 Tech Stack & Key Features
Modern Java Stack: Developed using Java 21 and Spring Boot 3.5.x.

High-Performance Communication: Implements gRPC and Protocol Buffers (protobuf) for efficient, typed synchronous communication between the Student and Course services.

Event-Driven Architecture: Utilizes Apache Kafka for asynchronous processing, specifically for updating analytics when enrollments or attendance records are created.

Advanced Security:

JWT with RSA: Secure token signing using private/public key pairs.

Role-Based Access Control (RBAC): Fine-grained permission management across all endpoints.

Data Management:

PostgreSQL: Reliable persistence for production environments.

Spring Data JPA: Efficient database interactions with automated auditing.

Automated Notifications: Integrated Spring Mail with Thymeleaf templates to send automated absence warnings and notifications to guardians.

🚀 Getting Started
Prerequisites
JDK 21

Maven 3.x

Docker & Docker Compose

Running with Docker
# 1. Clone the repository
git clone https://github.com/nguyentansinh123/school_microservices.git

# 2. Build the project
mvn clean install

# 3. Start Infrastructure and Services
docker-compose up -d

🧪 Testing
# Run tests for all services
mvn test
The project maintains high code quality with comprehensive unit and integration tests using JUnit 5, Mockito, and AssertJ
