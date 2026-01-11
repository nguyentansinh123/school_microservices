# 🎓 School Microservices Management System

A comprehensive, distributed educational management platform built with **Java 21** and **Spring Boot 3.5**. This project showcases a modern microservices architecture designed for scalability, high-performance, and maintainability in educational institutions.

[![Java Version](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Table of Contents

- [System Architecture](#-system-architecture)
- [Microservices Overview](#-microservices-overview)
- [Tech Stack](#-tech-stack)
- [Key Features](#-key-features)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Configuration](#%EF%B8%8F-configuration)
- [API Documentation](#-api-documentation)
- [Communication Patterns](#-communication-patterns)
- [Security](#-security)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Contributing](#-contributing)

## 🏗 System Architecture

The system is composed of **five specialized microservices** and a **centralized API Gateway**:

```
┌─────────────────────────────────────────────────────────────────┐
│                         API Gateway (Port 4004)                 │
│         Spring Cloud Gateway + JWT Validation Filters            │
└─────────────────────────────────────────────────────────────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                       │
┌───────▼──────┐      ┌────────▼────────┐    ┌────────▼─────────┐
│ Auth Service │      │ Student Service │    │  Course Service  │
│  (Port 4000) │      │   (Port 4001)   │    │   (Port 4003)    │
│  PostgreSQL  │◄─────┤   PostgreSQL    │◄───┤   PostgreSQL     │
└──────┬───────┘      └────────┬────────┘    └────────┬─────────┘
       │ Kafka                 │ Kafka                 │ Kafka
       │                       │ gRPC                  │ gRPC
       └───────────────────────┼───────────────────────┘
                               │
                       ┌───────▼────────┐
                       │Analytics Service│
                       │  (Port 4010)    │
                       │   PostgreSQL    │
                       └─────────────────┘
```

### Architecture Highlights

- **Centralized Entry Point**: API Gateway provides unified access to all services
- **Event-Driven Communication**: Apache Kafka for asynchronous messaging
- **High-Performance RPC**: gRPC for synchronous service-to-service communication
- **Database Per Service**: Each microservice manages its own PostgreSQL database
- **JWT-Based Security**: RSA-signed tokens for authentication and authorization

## 🎯 Microservices Overview

### 1. **API Gateway** (Port 4004)
- **Framework**: Spring Cloud Gateway (WebFlux)
- **Responsibilities**:
  - Single entry point for all client requests
  - Custom JWT validation filter
  - Dynamic routing and load balancing
  - Request/response transformation
  - Security routing (public vs protected endpoints)

**Key Routes**:
```yaml
/auth/**        → Auth Service (Public)
/students/**    → Student Service (Protected)
/courses/**     → Course Service (Protected)
/analytics/**   → Analytics Service (Protected)
```

### 2. **Auth Service** (Port 4000)
- **Framework**: Spring Boot + Spring Security
- **Database**: PostgreSQL
- **Responsibilities**:
  - User registration and authentication
  - Role-Based Access Control (RBAC)
  - Permission management
  - RSA-signed JWT token generation
  - User profile management
  - Password reset and email verification

**Key Entities**:
- `User`: Core user entity with email, phone, and profile details
- `Role`: User roles (ADMIN, TEACHER, STUDENT, PARENT)
- `Permission`: Granular permissions (admin: read, admin:write, etc.)

**Technologies**:
- Spring Security + JWT
- BCrypt password encoding
- RSA public/private key pair for token signing
- Apache Kafka producer (publishes user creation events)

### 3. **Student Service** (Port 4001)
- **Framework**: Spring Boot
- **Database**: PostgreSQL
- **Responsibilities**:
  - Student profile management
  - Guardian/parent information
  - Attendance tracking
  - Enrollment management
  - Email notifications to guardians

**Key Entities**:
- `Student`: Student profiles with personal information
- `Guardian`: Parent/guardian contact details
- `Attendance`: Daily attendance records
- `Enrollment`: Course enrollment records

**Technologies**:
- Spring Data JPA
- Spring Mail + Thymeleaf (email templates)
- Apache Kafka consumer (receives user creation events)
- gRPC client (calls Course Service)
- Swagger/OpenAPI documentation

### 4. **School Course Service** (Port 4003)
- **Framework**: Spring Boot
- **Database**: PostgreSQL
- **Responsibilities**:
  - Course/subject management
  - Teacher assignments
  - Class schedules
  - Academic calendar
  - Grading and assessments

**Key Entities**: 
- `Course`: Academic courses/subjects
- `Teacher`: Teacher information and assignments
- `Schedule`: Class timetables
- `Grade`: Student grades and assessments

**Technologies**:
- Spring Data JPA
- gRPC server (Port 4312) - exposes course data to other services
- Apache Kafka consumer (receives enrollment events)
- Swagger/OpenAPI documentation

### 5. **Analytics Service** (Port 4010)
- **Framework**: Spring Boot
- **Database**: PostgreSQL
- **Responsibilities**:
  - Real-time dashboard data aggregation
  - Attendance statistics and reporting
  - Enrollment metrics
  - Performance analytics
  - Automated report generation

**Technologies**:
- Spring Data JPA
- Apache Kafka consumer (receives events from all services)
- Event-driven data processing
- Swagger/OpenAPI documentation

## 🛠 Tech Stack

### Core Technologies

| Category | Technology | Version |
|----------|-----------|---------|
| **Language** | Java | 21 |
| **Framework** | Spring Boot | 3.5.6 - 3.5.7 |
| **Build Tool** | Maven | 3.9.9 |
| **Database** | PostgreSQL | Latest |
| **In-Memory DB** | H2 Database | (for testing) |
| **API Gateway** | Spring Cloud Gateway | 2025.0.0 |
| **Security** | Spring Security | 3.5.x |
| **JWT Library** | jjwt | 0.12.6 |
| **Message Broker** | Apache Kafka | Latest |
| **RPC Framework** | gRPC | 1.76.0 |
| **Serialization** | Protocol Buffers | 4.32.1 |
| **Email** | Spring Mail | 3.5.x |
| **Template Engine** | Thymeleaf | 3.5.x |
| **API Documentation** | SpringDoc OpenAPI | 2.7.0 |
| **Logging** | Logback | 1.5.19 |
| **Utilities** | Apache Commons Lang3 | 3.18.0 |
| **DevOps** | Docker + Docker Compose | Latest |

### Spring Ecosystem

- **Spring Data JPA**: Database access and ORM
- **Spring Validation**: Request validation
- **Spring Web**: RESTful APIs
- **Spring Cloud Gateway**: API Gateway
- **Spring Security**: Authentication & Authorization
- **Spring Kafka**: Event streaming
- **Spring gRPC**: High-performance RPC

## ✨ Key Features

### 🔐 Advanced Security

1. **RSA-Signed JWT Tokens**
   - Public/private key pair encryption
   - Secure token generation and validation
   - Configurable token expiration
   - Refresh token support

2. **Role-Based Access Control (RBAC)**
   ```java
   @PreAuthorize("hasAuthority('admin: write')")
   public ResponseEntity<Void> registerTeacher(@Valid @RequestBody RegistrationRequest request)
   ```
   - Granular permission system
   - Role hierarchy (ADMIN > TEACHER > STUDENT)
   - Method-level security annotations

3. **API Gateway Security**
   - Custom JWT validation filter
   - Public endpoint bypass
   - Automatic token extraction and validation

### 📡 Communication Patterns

1. **Synchronous Communication (gRPC)**
   ```
   Student Service ──gRPC──> Course Service
   (Get course details, teacher info, schedules)
   ```
   - Type-safe contracts via Protocol Buffers
   - High-performance binary serialization
   - Bidirectional streaming support

2. **Asynchronous Communication (Kafka)**
   ```
   Auth Service ──Kafka──> Student Service
   (New student/teacher registration events)
   
   Student Service ──Kafka──> Analytics Service
   (Attendance & enrollment events)
   
   Course Service ──Kafka──> Analytics Service
   (Grading & course events)
   ```
   - Event-driven architecture
   - Decoupled services
   - Real-time data processing

### 📧 Automated Notifications

- Email notifications for: 
  - Student absence warnings to guardians
  - New enrollment confirmations
  - Grade updates
  - Account verification

- Thymeleaf-based HTML email templates
- Configurable SMTP settings

### 📊 Analytics & Reporting

- Real-time dashboard metrics
- Attendance statistics
- Enrollment trends
- Performance reports
- Event-sourced data aggregation

## 📁 Project Structure

```
school_microservices/
├── api-gateway/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/caffein/apigateway/
│   │   │   │   ├── filter/              # JWT validation filter
│   │   │   │   └── config/              # Gateway configuration
│   │   │   └── resources/
│   │   │       └── application.yml      # Gateway routes
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
│
├── auth-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/caffein/authservice/
│   │   │   │   ├── config/
│   │   │   │   │   ├── jwtConfig/       # JWT service, filters
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   └── BeanConfig.java
│   │   │   │   ├── controller/          # REST controllers
│   │   │   │   ├── service/             # Business logic
│   │   │   │   ├── repository/          # JPA repositories
│   │   │   │   ├── model/               # Entities (User, Role, Permission)
│   │   │   │   ├── request/             # DTOs
│   │   │   │   └── kafka/producer/      # Kafka producers
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── private_key.pem      # RSA private key
│   │   │       └── public_key.pem       # RSA public key
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
│
├── student-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/caffein/studentservice/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── model/               # Student, Guardian, Attendance
│   │   │   │   ├── dto/
│   │   │   │   ├── kafka/consumer/      # Kafka consumers
│   │   │   │   ├── grpc/client/         # gRPC client for Course Service
│   │   │   │   └── email/               # Email service + templates
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── templates/           # Thymeleaf email templates
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
│
├── school-course-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/caffein/schoolcourseservice/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── model/               # Course, Teacher, Schedule
│   │   │   │   ├── dto/
│   │   │   │   ├── kafka/consumer/
│   │   │   │   └── grpc/server/         # gRPC server implementation
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── proto/               # Protocol Buffer definitions
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
│
├── analytic-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/caffein/analyticservice/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── model/               # Analytics entities
│   │   │   │   └── kafka/consumer/      # Kafka event consumers
���   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
│
├── api-request/                         # Postman/HTTP request collections
├── docker-compose.yml                   # (if exists)
├── pom.xml                              # Parent POM (if multi-module)
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- **JDK 21** ([Download](https://adoptium.net/))
- **Maven 3.9+** ([Download](https://maven.apache.org/download.cgi))
- **Docker & Docker Compose** ([Download](https://www.docker.com/))
- **PostgreSQL** (for local development without Docker)
- **Apache Kafka** (for local development without Docker)

### Option 1: Running with Docker (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/nguyentansinh123/school_microservices.git
cd school_microservices

# 2. Build all services
mvn clean install -DskipTests

# 3. Start infrastructure and services
docker-compose up -d

# 4. Check service health
docker-compose ps

# 5. View logs
docker-compose logs -f [service-name]
```

**Services will be available at**:
- API Gateway: `http://localhost:4004`
- Auth Service: `http://localhost:4000`
- Student Service: `http://localhost:4001`
- Course Service: `http://localhost:4003`
- Analytics Service: `http://localhost:4010`

### Option 2: Running Locally

#### 1. Start Infrastructure

```bash
# Start PostgreSQL
docker run -d --name postgres \
  -e POSTGRES_USER=admin_user \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  postgres:latest

# Start Kafka (with Zookeeper)
docker-compose up -d zookeeper kafka
```

#### 2. Configure Each Service

Create `application-dev.properties` for each service (see [Configuration](#%EF%B8%8F-configuration) section).

#### 3. Start Services

```bash
# Terminal 1 - Auth Service
cd auth-service
mvn spring-boot:run

# Terminal 2 - Student Service
cd student-service
mvn spring-boot:run

# Terminal 3 - Course Service
cd school-course-service
mvn spring-boot:run

# Terminal 4 - Analytics Service
cd analytic-service
mvn spring-boot:run

# Terminal 5 - API Gateway
cd api-gateway
mvn spring-boot:run
```

## ⚙️ Configuration

### Environment Variables

Each service requires specific environment variables.  Here's a comprehensive guide:

#### **Auth Service**

```properties
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/auth_db
SPRING_DATASOURCE_USERNAME=admin_user
SPRING_DATASOURCE_PASSWORD=password
SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Kafka
SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=localhost:9092

# JWT
APP_SECURITY_JWT_ACCESS_TOKEN_EXPIRATION=86400000  # 1 day
APP_SECURITY_JWT_REFRESH_TOKEN_EXPIRATION=604800000  # 7 days

# Server
SERVER_PORT=4000
```

#### **Student Service**

```properties
# Database
SPRING_DATASOURCE_URL=jdbc: postgresql://localhost:5432/student_db
SPRING_DATASOURCE_USERNAME=admin_user
SPRING_DATASOURCE_PASSWORD=password
SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Kafka
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
SPRING_KAFKA_CONSUMER_GROUP_ID=mysuperGroup

# Email (for guardian notifications)
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your_email@gmail.com
SPRING_MAIL_PASSWORD=your_app_password

# Server
SERVER_PORT=4001
```

#### **Course Service**

```properties
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/course_db
SPRING_DATASOURCE_USERNAME=admin_user
SPRING_DATASOURCE_PASSWORD=password
SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Kafka
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
SPRING_KAFKA_CONSUMER_GROUP_ID=mysuperGroupnumbertwo

# gRPC
SPRING_GRPC_SERVER_PORT=4312
SPRING_GRPC_SERVER_ENABLE_REFLECTION=true

# Server
SERVER_PORT=4003
```

#### **Analytics Service**

```properties
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/analytics_db
SPRING_DATASOURCE_USERNAME=admin_user
SPRING_DATASOURCE_PASSWORD=password
SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Kafka
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
SPRING_KAFKA_CONSUMER_GROUP_ID=analytics-group

# Server
SERVER_PORT=4010
```

#### **API Gateway**

```yaml
server:
  port: 4004

# Service URLs
AUTH_SERVICE_URL: http://localhost:4000
STUDENT_SERVICE_URL:  http://localhost:4001
SCHOOL_COURSE_SERVICE_URL:  http://localhost:4003
ANALYTICS_SERVICE_URL: http://localhost:4010
```

### Docker Compose Configuration

If you have a `docker-compose.yml`, ensure it includes:
- PostgreSQL instances for each service
- Apache Kafka + Zookeeper
- All 5 microservices
- Network configuration

## 📚 API Documentation

### Swagger/OpenAPI

Each service exposes Swagger UI for API documentation:

- **Student Service**: `http://localhost:4001/swagger-ui.html`
- **Course Service**: `http://localhost:4003/swagger-ui.html`
- **Analytics Service**: `http://localhost:4010/swagger-ui.html`

### Sample API Endpoints

#### **Auth Service** (via Gateway)

```bash
# Register new user
POST http://localhost:4004/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "password": "SecurePass123!"
}

# Login
POST http://localhost:4004/auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "SecurePass123!"
}

# Register student (admin only)
POST http://localhost:4004/student/admin/register-student
Authorization: Bearer {admin_token}
Content-Type:  application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@school.edu",
  "phoneNumber": "+0987654321",
  "password": "StudentPass123!"
}
```

#### **Student Service** (via Gateway)

```bash
# Get all students
GET http://localhost:4004/students
Authorization: Bearer {token}

# Add student attendance
POST http://localhost:4004/students/{id}/attendance
Authorization: Bearer {token}
Content-Type: application/json

{
  "date": "2025-01-11",
  "status": "PRESENT"
}

# Get student by ID
GET http://localhost:4004/students/{id}
Authorization: Bearer {token}
```

#### **Course Service** (via Gateway)

```bash
# Get all courses
GET http://localhost:4004/courses
Authorization: Bearer {token}

# Create new course
POST http://localhost:4004/courses
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Mathematics 101",
  "code": "MATH101",
  "description": "Introduction to Calculus",
  "credits": 3
}

# Assign teacher to course
PUT http://localhost:4004/courses/{courseId}/teacher/{teacherId}
Authorization: Bearer {token}
```

#### **Analytics Service** (via Gateway)

```bash
# Get dashboard statistics
GET http://localhost:4004/analytics/dashboard
Authorization: Bearer {token}

# Get attendance report
GET http://localhost:4004/analytics/attendance? startDate=2025-01-01&endDate=2025-01-31
Authorization: Bearer {token}
```

## 🔄 Communication Patterns

### 1. gRPC Communication

**Course Service gRPC Server** (Port 4312):

```protobuf
// course.proto
service CourseService {
  rpc GetCourse(CourseRequest) returns (CourseResponse);
  rpc GetTeacher(TeacherRequest) returns (TeacherResponse);
  rpc GetSchedule(ScheduleRequest) returns (ScheduleResponse);
}

message CourseRequest {
  string course_id = 1;
}

message CourseResponse {
  string id = 1;
  string name = 2;
  string code = 3;
  int32 credits = 4;
}
```

**Student Service gRPC Client**:

```java
@Service
@RequiredArgsConstructor
public class CourseGrpcClient {
    
    private final CourseServiceGrpc.CourseServiceBlockingStub courseServiceStub;
    
    public CourseDTO getCourseDetails(String courseId) {
        CourseRequest request = CourseRequest. newBuilder()
            .setCourseId(courseId)
            .build();
            
        CourseResponse response = courseServiceStub.getCourse(request);
        return mapToDTO(response);
    }
}
```

### 2. Kafka Event Streaming

**Auth Service Producer**:

```java
@Service
@RequiredArgsConstructor
public class UserProducer {
    
    private final KafkaTemplate<String, UserKafkaDTO> kafkaTemplate;
    
    public void publishUserCreated(User user) {
        UserKafkaDTO dto = userMapper.toKafkaDTO(user);
        kafkaTemplate.send("pushUserFromAuthServiceToStudentServiceTopic", dto);
    }
}
```

**Student Service Consumer**:

```java
@Service
public class UserConsumer {
    
    @KafkaListener(topics = "pushUserFromAuthServiceToStudentServiceTopic", 
                   groupId = "mysuperGroup")
    public void consumeUserEvent(UserDTO userDTO) {
        // Create student profile from user data
        createStudentProfile(userDTO);
    }
}
```

## 🔒 Security

### JWT Token Flow

1. **User Login** → Auth Service generates RSA-signed JWT
2. **Client Request** → Includes JWT in `Authorization:  Bearer {token}` header
3. **API Gateway** → Validates JWT signature using public key
4. **Microservice** → Receives validated request with user context

### RSA Key Generation

```bash
# Generate private key
openssl genrsa -out private_key.pem 2048

# Extract public key
openssl rsa -in private_key.pem -pubout -out public_key.pem
```

Place keys in `auth-service/src/main/resources/`:
- `private_key.pem` (for token signing)
- `public_key.pem` (for token validation - copy to gateway)

### Permission Examples

```java
// Admin can create teachers
@PreAuthorize("hasAuthority('admin:write')")
public ResponseEntity<Void> registerTeacher(...)

// Teachers can view all students
@PreAuthorize("hasAuthority('teacher:read')")
public ResponseEntity<List<Student>> getAllStudents(...)

// Students can only view their own data
@PreAuthorize("hasAuthority('student: read') and #id == principal.id")
public ResponseEntity<Student> getStudent(@PathVariable String id)
```

## 🧪 Testing

### Running Tests

```bash
# Run all tests
mvn clean test

# Run tests for specific service
cd auth-service
mvn test

# Run with coverage
mvn clean test jacoco:report
```

### Test Categories

1. **Unit Tests**: Service layer, mappers, utilities
2. **Integration Tests**: Repository tests with H2 database
3. **API Tests**: Controller integration tests
4. **Kafka Tests**: Consumer/producer integration tests (using embedded Kafka)
5. **gRPC Tests**: RPC communication tests

### Example Test

```java
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockUser(authorities = "teacher:read")
    void shouldReturnAllStudents() throws Exception {
        mockMvc.perform(get("/api/v1/students"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}
```

## 🚢 Deployment

### Docker Build

Each service has a multi-stage Dockerfile:

```dockerfile
# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre AS runner
WORKDIR /app
COPY --from=builder /app/target/*. jar app.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Kubernetes Deployment (Optional)

```yaml
apiVersion: apps/v1
kind:  Deployment
metadata:
  name:  auth-service
spec:
  replicas: 3
  selector:
    matchLabels: 
      app: auth-service
  template:
    metadata:
      labels:
        app: auth-service
    spec:
      containers:
      - name: auth-service
        image: school/auth-service:latest
        ports:
        - containerPort: 4000
        env:
        - name: SPRING_DATASOURCE_URL
          value: jdbc:postgresql://postgres:5432/auth_db
        - name: SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS
          value: kafka:9092
```

### Production Checklist

- [ ] Configure production database credentials
- [ ] Set up monitoring (Prometheus + Grafana)
- [ ] Enable distributed tracing (Sleuth + Zipkin)
- [ ] Configure centralized logging (ELK stack)
- [ ] Set up service discovery (Eureka/Consul)
- [ ] Implement circuit breakers (Resilience4j)
- [ ] Configure rate limiting
- [ ] Enable HTTPS/TLS
- [ ] Set up backup and disaster recovery
- [ ] Configure auto-scaling

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style

- Follow Java naming conventions
- Use Lombok to reduce boilerplate
- Write comprehensive Javadoc for public APIs
- Maintain test coverage above 80%
- Use SonarLint for code quality

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Nguyen Tan Sinh**
- GitHub: [@nguyentansinh123](https://github.com/nguyentansinh123)

## 🙏 Acknowledgments

- Spring Boot team for excellent framework
- Apache Kafka for event streaming
- gRPC team for high-performance RPC
- PostgreSQL community
- OpenJDK contributors

---

**⭐ Star this repository if you find it helpful!**

For questions or support, please open an issue on GitHub. 
