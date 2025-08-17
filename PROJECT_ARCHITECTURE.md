# Quiz Boot - Project Architecture

## 📋 Table of Contents
- [Project Overview](#project-overview)
- [Technology Stack](#technology-stack)
- [System Architecture](#system-architecture)
- [Module Structure](#module-structure)
- [Detailed Package Structure](#detailed-package-structure)
- [Database Design](#database-design)
- [API Design](#api-design)
- [Security Architecture](#security-architecture)
- [Cross-Cutting Concerns](#cross-cutting-concerns)
- [Development Guidelines](#development-guidelines)
- [Deployment Architecture](#deployment-architecture)

## 🚀 Project Overview

**Quiz Boot** is a comprehensive quiz management system built with Spring Boot 3.5.4, designed to provide a scalable, secure, and maintainable platform for creating, managing, and taking quizzes.

### Key Features
- 🎯 Multi-category quiz management
- 👥 User authentication and authorization
- 📊 Real-time scoring and analytics
- ⏱️ Timed quiz sessions
- 📱 Responsive web interface
- 🔒 Role-based access control
- 📈 Progress tracking and reporting

## 🛠️ Technology Stack

### Backend Framework
- **Spring Boot 3.5.4** - Main application framework
- **Spring Security 6.x** - Authentication and authorization
- **Spring Data JPA** - Data persistence layer
- **Spring Web MVC** - REST API development
- **Spring Modulith** - Modular architecture support
- **Spring Boot Actuator** - Application monitoring

### Database
- **MySQL 8.0+** - Primary database
- **Hibernate ORM** - Object-relational mapping
- **Flyway/Liquibase** - Database migration (recommended addition)

### Build & Development
- **Maven 3.8+** - Dependency management and build tool
- **Java 21** - Programming language
- **Lombok** - Boilerplate code reduction
- **Spring Boot DevTools** - Development productivity

### Testing
- **JUnit 5** - Unit testing framework
- **Spring Boot Test** - Integration testing
- **TestContainers** - Database testing (recommended addition)
- **Mockito** - Mocking framework

## 🏗️ System Architecture

### High-Level Architecture
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Presentation  │    │    Business     │    │      Data       │
│     Layer       │◄──►│     Layer       │◄──►│     Layer       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
│                      │                      │
├─ REST Controllers    ├─ Service Layer       ├─ JPA Repositories
├─ Request/Response    ├─ Business Logic      ├─ Entity Models
├─ Exception Handlers ├─ Validation          ├─ Database Schema
├─ Security Config    ├─ Transaction Mgmt    ├─ Query Optimization
└─ Web Configuration  └─ Event Handling      └─ Connection Pool
```

### Modular Architecture (Spring Modulith)
```
quiz-boot/
├─ user-management/     # User operations and authentication
├─ quiz-management/     # Quiz creation and management
├─ session-management/  # Quiz session handling
├─ scoring-analytics/   # Scoring and reporting
├─ notification/        # Notifications and alerts
└─ shared/             # Common utilities and DTOs
```

## 📦 Module Structure

### 1. User Management Module
**Responsibilities:**
- User registration and authentication
- Profile management
- Role and permission management
- Password reset functionality

**Key Components:**
- `UserController` - User-related endpoints
- `UserService` - Business logic for user operations
- `UserRepository` - Data access for users
- `User`, `Role`, `Permission` entities

### 2. Quiz Management Module
**Responsibilities:**
- Quiz creation and editing
- Question management
- Category management
- Quiz publishing and archiving

**Key Components:**
- `QuizController` - Quiz CRUD operations
- `QuestionController` - Question management
- `CategoryController` - Category operations
- `QuizService`, `QuestionService` - Business logic
- `Quiz`, `Question`, `Category`, `Answer` entities

### 3. Session Management Module
**Responsibilities:**
- Quiz session lifecycle
- Timer management
- Answer submission
- Session state management

**Key Components:**
- `SessionController` - Session endpoints
- `SessionService` - Session business logic
- `SessionRepository` - Session data access
- `QuizSession`, `SessionAnswer` entities

### 4. Scoring & Analytics Module
**Responsibilities:**
- Score calculation
- Performance analytics
- Progress tracking
- Report generation

**Key Components:**
- `ScoringController` - Scoring endpoints
- `AnalyticsController` - Analytics endpoints
- `ScoringService` - Score calculation logic
- `Score`, `Analytics`, `Progress` entities

### 5. Notification Module
**Responsibilities:**
- Email notifications
- In-app notifications
- Event-driven messaging

**Key Components:**
- `NotificationService` - Notification handling
- `EmailService` - Email operations
- `NotificationRepository` - Notification storage

## 📁 Detailed Package Structure

```
src/main/java/com/example/quiz_boot/
│
├─ QuizBootApplication.java
│
├─ config/                          # Configuration classes
│  ├─ SecurityConfig.java
│  ├─ DatabaseConfig.java
│  ├─ WebConfig.java
│  ├─ ModulithConfig.java
│  └─ CacheConfig.java
│
├─ modules/                         # Spring Modulith modules
│  │
│  ├─ user/                        # User Management Module
│  │  ├─ api/
│  │  │  ├─ UserController.java
│  │  │  ├─ AuthController.java
│  │  │  └─ ProfileController.java
│  │  ├─ domain/
│  │  │  ├─ User.java
│  │  │  ├─ Role.java
│  │  │  ├─ Permission.java
│  │  │  └─ UserProfile.java
│  │  ├─ service/
│  │  │  ├─ UserService.java
│  │  │  ├─ AuthService.java
│  │  │  └─ ProfileService.java
│  │  ├─ repository/
│  │  │  ├─ UserRepository.java
│  │  │  ├─ RoleRepository.java
│  │  │  └─ PermissionRepository.java
│  │  └─ dto/
│  │     ├─ UserDto.java
│  │     ├─ LoginRequest.java
│  │     ├─ RegisterRequest.java
│  │     └─ UserResponse.java
│  │
│  ├─ quiz/                        # Quiz Management Module
│  │  ├─ api/
│  │  │  ├─ QuizController.java
│  │  │  ├─ QuestionController.java
│  │  │  └─ CategoryController.java
│  │  ├─ domain/
│  │  │  ├─ Quiz.java
│  │  │  ├─ Question.java
│  │  │  ├─ Answer.java
│  │  │  ├─ Category.java
│  │  │  └─ QuestionType.java (enum)
│  │  ├─ service/
│  │  │  ├─ QuizService.java
│  │  │  ├─ QuestionService.java
│  │  │  └─ CategoryService.java
│  │  ├─ repository/
│  │  │  ├─ QuizRepository.java
│  │  │  ├─ QuestionRepository.java
│  │  │  ├─ AnswerRepository.java
│  │  │  └─ CategoryRepository.java
│  │  └─ dto/
│  │     ├─ QuizDto.java
│  │     ├─ QuestionDto.java
│  │     ├─ AnswerDto.java
│  │     └─ QuizSummaryDto.java
│  │
│  ├─ session/                     # Session Management Module
│  │  ├─ api/
│  │  │  ├─ SessionController.java
│  │  │  └─ SubmissionController.java
│  │  ├─ domain/
│  │  │  ├─ QuizSession.java
│  │  │  ├─ SessionAnswer.java
│  │  │  ├─ SessionState.java (enum)
│  │  │  └─ TimerConfig.java
│  │  ├─ service/
│  │  │  ├─ SessionService.java
│  │  │  ├─ TimerService.java
│  │  │  └─ SubmissionService.java
│  │  ├─ repository/
│  │  │  ├─ SessionRepository.java
│  │  │  └─ SessionAnswerRepository.java
│  │  └─ dto/
│  │     ├─ SessionDto.java
│  │     ├─ SubmissionDto.java
│  │     └─ SessionProgressDto.java
│  │
│  ├─ scoring/                     # Scoring & Analytics Module
│  │  ├─ api/
│  │  │  ├─ ScoringController.java
│  │  │  └─ AnalyticsController.java
│  │  ├─ domain/
│  │  │  ├─ Score.java
│  │  │  ├─ Analytics.java
│  │  │  ├─ Progress.java
│  │  │  └─ ScoreCalculationRule.java
│  │  ├─ service/
│  │  │  ├─ ScoringService.java
│  │  │  ├─ AnalyticsService.java
│  │  │  └─ ReportService.java
│  │  ├─ repository/
│  │  │  ├─ ScoreRepository.java
│  │  │  ├─ AnalyticsRepository.java
│  │  │  └─ ProgressRepository.java
│  │  └─ dto/
│  │     ├─ ScoreDto.java
│  │     ├─ AnalyticsDto.java
│  │     └─ ReportDto.java
│  │
│  ├─ notification/                # Notification Module
│  │  ├─ api/
│  │  │  └─ NotificationController.java
│  │  ├─ domain/
│  │  │  ├─ Notification.java
│  │  │  ├─ NotificationType.java (enum)
│  │  │  └─ EmailTemplate.java
│  │  ├─ service/
│  │  │  ├─ NotificationService.java
│  │  │  ├─ EmailService.java
│  │  │  └─ EventListener.java
│  │  ├─ repository/
│  │  │  └─ NotificationRepository.java
│  │  └─ dto/
│  │     ├─ NotificationDto.java
│  │     └─ EmailDto.java
│  │
│  └─ shared/                      # Shared Components
│     ├─ dto/
│     │  ├─ ApiResponse.java
│     │  ├─ PageResponse.java
│     │  └─ ErrorResponse.java
│     ├─ exception/
│     │  ├─ GlobalExceptionHandler.java
│     │  ├─ BusinessException.java
│     │  ├─ ResourceNotFoundException.java
│     │  └─ ValidationException.java
│     ├─ util/
│     │  ├─ DateTimeUtils.java
│     │  ├─ ValidationUtils.java
│     │  └─ SecurityUtils.java
│     ├─ event/
│     │  ├─ QuizCompletedEvent.java
│     │  ├─ UserRegisteredEvent.java
│     │  └─ SessionExpiredEvent.java
│     └─ constants/
│        ├─ AppConstants.java
│        ├─ SecurityConstants.java
│        └─ MessageConstants.java
│
└─ aspect/                         # Cross-cutting concerns
   ├─ LoggingAspect.java
   ├─ SecurityAspect.java
   ├─ PerformanceAspect.java
   └─ AuditAspect.java
```

## 🗄️ Database Design

### Core Tables

#### Users & Authentication
```sql
users
├─ id (PRIMARY KEY)
├─ username (UNIQUE)
├─ email (UNIQUE)
├─ password_hash
├─ first_name
├─ last_name
├─ is_active
├─ created_at
├─ updated_at
└─ last_login

roles
├─ id (PRIMARY KEY)
├─ name (UNIQUE)
├─ description
├─ created_at
└─ updated_at

user_roles (Many-to-Many)
├─ user_id (FK to users.id)
├─ role_id (FK to roles.id)
└─ assigned_at
```

#### Quiz Management
```sql
categories
├─ id (PRIMARY KEY)
├─ name (UNIQUE)
├─ description
├─ is_active
├─ created_at
└─ updated_at

quizzes
├─ id (PRIMARY KEY)
├─ title
├─ description
├─ category_id (FK to categories.id)
├─ created_by (FK to users.id)
├─ duration_minutes
├─ max_attempts
├─ passing_score
├─ is_published
├─ is_randomized
├─ created_at
├─ updated_at
└─ published_at

questions
├─ id (PRIMARY KEY)
├─ quiz_id (FK to quizzes.id)
├─ question_text
├─ question_type (ENUM: SINGLE_CHOICE, MULTIPLE_CHOICE, TRUE_FALSE, ESSAY)
├─ points
├─ order_index
├─ explanation
├─ created_at
└─ updated_at

answers
├─ id (PRIMARY KEY)
├─ question_id (FK to questions.id)
├─ answer_text
├─ is_correct
├─ order_index
├─ created_at
└─ updated_at
```

#### Session Management
```sql
quiz_sessions
├─ id (PRIMARY KEY)
├─ quiz_id (FK to quizzes.id)
├─ user_id (FK to users.id)
├─ session_state (ENUM: NOT_STARTED, IN_PROGRESS, COMPLETED, EXPIRED)
├─ start_time
├─ end_time
├─ duration_seconds
├─ created_at
└─ updated_at

session_answers
├─ id (PRIMARY KEY)
├─ session_id (FK to quiz_sessions.id)
├─ question_id (FK to questions.id)
├─ selected_answer_ids (JSON)
├─ answer_text (For essay questions)
├─ is_correct
├─ points_earned
├─ answered_at
├─ created_at
└─ updated_at
```

#### Scoring & Analytics
```sql
scores
├─ id (PRIMARY KEY)
├─ session_id (FK to quiz_sessions.id)
├─ total_questions
├─ correct_answers
├─ total_points
├─ earned_points
├─ percentage_score
├─ grade
├─ is_passed
├─ calculated_at
├─ created_at
└─ updated_at

user_progress
├─ id (PRIMARY KEY)
├─ user_id (FK to users.id)
├─ category_id (FK to categories.id)
├─ quizzes_attempted
├─ quizzes_passed
├─ average_score
├─ total_time_spent
├─ last_quiz_date
├─ created_at
└─ updated_at
```

### Database Indexes
```sql
-- Performance optimization indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_quizzes_category ON quizzes(category_id);
CREATE INDEX idx_quizzes_created_by ON quizzes(created_by);
CREATE INDEX idx_sessions_user_quiz ON quiz_sessions(user_id, quiz_id);
CREATE INDEX idx_sessions_state ON quiz_sessions(session_state);
CREATE INDEX idx_answers_question ON session_answers(question_id);
CREATE INDEX idx_scores_session ON scores(session_id);
```

## 🔌 API Design

### RESTful Endpoints Structure

#### Authentication Endpoints
```
POST   /api/v1/auth/register         # User registration
POST   /api/v1/auth/login            # User login
POST   /api/v1/auth/logout           # User logout
POST   /api/v1/auth/refresh          # Token refresh
POST   /api/v1/auth/forgot-password  # Password reset request
POST   /api/v1/auth/reset-password   # Password reset confirmation
```

#### User Management Endpoints
```
GET    /api/v1/users                 # Get all users (Admin)
GET    /api/v1/users/{id}            # Get user by ID
PUT    /api/v1/users/{id}            # Update user profile
DELETE /api/v1/users/{id}            # Delete user (Admin)
GET    /api/v1/users/{id}/progress   # Get user progress
```

#### Quiz Management Endpoints
```
GET    /api/v1/quizzes               # Get all quizzes (with pagination)
GET    /api/v1/quizzes/{id}          # Get quiz by ID
POST   /api/v1/quizzes               # Create new quiz
PUT    /api/v1/quizzes/{id}          # Update quiz
DELETE /api/v1/quizzes/{id}          # Delete quiz
POST   /api/v1/quizzes/{id}/publish  # Publish quiz
GET    /api/v1/quizzes/categories    # Get all categories
```

#### Question Management Endpoints
```
GET    /api/v1/quizzes/{quizId}/questions     # Get questions for quiz
POST   /api/v1/quizzes/{quizId}/questions     # Add question to quiz
PUT    /api/v1/questions/{id}                 # Update question
DELETE /api/v1/questions/{id}                 # Delete question
POST   /api/v1/questions/{id}/answers         # Add answer to question
```

#### Session Management Endpoints
```
POST   /api/v1/sessions               # Start new quiz session
GET    /api/v1/sessions/{id}          # Get session details
PUT    /api/v1/sessions/{id}/answers  # Submit answer
POST   /api/v1/sessions/{id}/submit   # Submit entire session
GET    /api/v1/sessions/{id}/progress # Get session progress
```

#### Scoring & Analytics Endpoints
```
GET    /api/v1/scores/{sessionId}     # Get session score
GET    /api/v1/analytics/users/{id}   # Get user analytics
GET    /api/v1/analytics/quizzes/{id} # Get quiz analytics
GET    /api/v1/reports/user-progress  # Get progress reports
GET    /api/v1/reports/quiz-stats     # Get quiz statistics
```

### API Response Format
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {
    // Response data
  },
  "errors": [],
  "timestamp": "2025-08-17T10:30:00Z",
  "path": "/api/v1/quizzes"
}
```

### Error Response Format
```json
{
  "success": false,
  "message": "Validation failed",
  "data": null,
  "errors": [
    {
      "field": "email",
      "code": "INVALID_FORMAT",
      "message": "Email format is invalid"
    }
  ],
  "timestamp": "2025-08-17T10:30:00Z",
  "path": "/api/v1/users"
}
```

## 🔒 Security Architecture

### Authentication Strategy
- **JWT (JSON Web Tokens)** for stateless authentication
- **Refresh Token** mechanism for session management
- **BCrypt** password hashing
- **Multi-factor Authentication** support (future enhancement)

### Authorization Model
```
Roles Hierarchy:
ADMIN → INSTRUCTOR → STUDENT → GUEST

Permissions:
- QUIZ_CREATE, QUIZ_READ, QUIZ_UPDATE, QUIZ_DELETE
- USER_CREATE, USER_READ, USER_UPDATE, USER_DELETE
- SESSION_START, SESSION_SUBMIT, SESSION_VIEW
- ANALYTICS_VIEW, REPORTS_GENERATE
```

### Security Configuration
```java
// Security filter chain priorities
1. JWT Authentication Filter
2. CORS Filter
3. CSRF Protection (for web forms)
4. Rate Limiting Filter
5. Method Security
6. Exception Translation Filter
```

### Data Protection
- **Input Validation** on all endpoints
- **SQL Injection Protection** via JPA
- **XSS Protection** for user inputs
- **HTTPS Enforcement** in production
- **Sensitive Data Encryption** for PII

## ⚙️ Cross-Cutting Concerns

### Logging Strategy
```
Structured Logging with:
├─ Request/Response logging
├─ Business operation logging
├─ Security event logging
├─ Performance monitoring
└─ Error tracking
```

### Caching Strategy
```
Multi-level Caching:
├─ Application Level (Spring Cache)
├─ Database Level (Hibernate 2nd Level Cache)
├─ Web Level (HTTP Caching Headers)
└─ CDN Level (Static Resources)
```

### Transaction Management
```
Transaction Boundaries:
├─ Service Layer (@Transactional)
├─ Read-only transactions for queries
├─ Propagation strategies
└─ Rollback rules
```

### Event-Driven Architecture
```
Domain Events:
├─ UserRegisteredEvent
├─ QuizPublishedEvent
├─ SessionCompletedEvent
├─ ScoreCalculatedEvent
└─ NotificationSentEvent
```

## 📋 Development Guidelines

### Code Quality Standards
- **Java Coding Standards** (Google Java Style Guide)
- **SonarQube** integration for code quality
- **Test Coverage** minimum 80%
- **Code Review** mandatory for all changes
- **Documentation** for public APIs

### Testing Strategy
```
Testing Pyramid:
├─ Unit Tests (70%)
│  ├─ Service layer tests
│  ├─ Repository tests
│  └─ Utility tests
├─ Integration Tests (20%)
│  ├─ API integration tests
│  ├─ Database integration tests
│  └─ Security integration tests
└─ End-to-End Tests (10%)
   ├─ User journey tests
   └─ Performance tests
```

### Git Workflow
```
Branch Strategy:
├─ main (production-ready)
├─ develop (integration branch)
├─ feature/* (feature development)
├─ hotfix/* (production fixes)
└─ release/* (release preparation)
```

### Documentation Requirements
- **API Documentation** (OpenAPI/Swagger)
- **Database Schema** documentation
- **Deployment Guide**
- **Configuration Guide**
- **Troubleshooting Guide**

## 🚀 Deployment Architecture

### Environment Strategy
```
Environments:
├─ Development
│  ├─ Local MySQL
│  ├─ H2 for testing
│  └─ Development profiles
├─ Staging
│  ├─ Production-like setup
│  ├─ Integration testing
│  └─ Performance testing
└─ Production
   ├─ Load balancing
   ├─ Database clustering
   └─ Monitoring & alerting
```

### Container Strategy
```dockerfile
# Multi-stage Docker build
FROM openjdk:21-jdk-slim as builder
FROM openjdk:21-jre-slim as runtime

# Application layers
├─ Base OS layer
├─ JRE layer
├─ Application dependencies
└─ Application JAR
```

### Infrastructure Components
```
Production Setup:
├─ Load Balancer (nginx/HAProxy)
├─ Application Servers (2+ instances)
├─ Database (MySQL Master/Slave)
├─ Cache Layer (Redis)
├─ File Storage (AWS S3/MinIO)
├─ Monitoring (Prometheus/Grafana)
└─ Log Aggregation (ELK Stack)
```

### Configuration Management
```
Configuration Sources:
├─ application.yml (defaults)
├─ application-{profile}.yml (environment-specific)
├─ Environment variables
├─ External config server
└─ Command line arguments
```

## 📊 Monitoring & Observability

### Application Metrics
- **Performance Metrics** (response times, throughput)
- **Business Metrics** (quiz completion rates, user engagement)
- **Technical Metrics** (JVM metrics, database connections)
- **Custom Metrics** (quiz difficulty analysis, user progress)

### Health Checks
```
Health Endpoints:
├─ /actuator/health (overall health)
├─ /actuator/health/db (database connectivity)
├─ /actuator/health/diskSpace (disk usage)
└─ /actuator/health/custom (business logic health)
```

### Alerting Rules
- **High Error Rate** (>5% in 5 minutes)
- **Slow Response Time** (>2 seconds average)
- **Database Connection Issues**
- **High Memory Usage** (>85%)
- **Disk Space Low** (<20% free)

---

## 🎯 Future Enhancements

### Phase 2 Features
- Real-time collaborative quizzes
- Video/audio question support
- Advanced analytics dashboard
- Mobile application
- Integration with LMS systems

### Technical Improvements
- Microservices migration
- Event sourcing implementation
- GraphQL API support
- Machine learning for question difficulty
- Kubernetes deployment

### Scalability Considerations
- Database sharding strategy
- Caching optimization
- CDN integration
- Async processing for heavy operations
- Read replica implementation

---

**Document Version:** 1.0  
**Last Updated:** August 17, 2025  
**Author:** Development Team  
**Review Date:** September 17, 2025
