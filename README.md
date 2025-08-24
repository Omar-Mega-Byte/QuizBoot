# Quiz Boot Application

A Spring Boot application with JWT authentication for quiz management.

## Features

- **JWT Authentication**: Secure user registration and login with JWT tokens
- **User Management**: User registration, login, and profile management
- **Security**: Password hashing with BCrypt, input validation, and audit logging
- **Database Integration**: MySQL database with JPA/Hibernate
- **RESTful API**: Clean REST endpoints with proper HTTP status codes
- **Modular Architecture**: Well-organized modular structure following Spring Modulith

## Quick Start

### Prerequisites

- Java 21
- Maven 3.6+
- MySQL 8.0+

### Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE quizdb;
```

2. Update database credentials in `application.yml` or set environment variables:
```bash
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_DB=quizdb
export MYSQL_USER=your_username
export MYSQL_PASSWORD=your_password
```

### Running the Application

1. Clone the repository
2. Install dependencies:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## JWT Authentication

### Configuration

Set JWT configuration in `application.yml`:
```yaml
app:
  jwt:
    secret: ${JWT_SECRET:your-secret-key-here}
    expirationMs: ${JWT_EXPIRATION_MS:86400000} # 24 hours
```

### API Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "SecurePassword123",
    "firstName": "John",
    "lastName": "Doe"
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "john_doe",
    "password": "SecurePassword123"
}
```

Both endpoints return:
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": {
        "id": 1,
        "username": "john_doe",
        "email": "john@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "active": true,
        "createdAt": "2023-08-24T10:30:00Z",
        "updatedAt": "2023-08-24T10:30:00Z"
    }
}
```

### Using JWT Tokens

Include the token in the Authorization header for protected endpoints:
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Project Structure

```
src/
└── main/
    └── java/
        └── com/example/quiz_boot/
            ├── QuizBootApplication.java
            └── modules/
                ├── shared/
                │   ├── Base/
                │   │   └── BaseEntity.java
                │   └── utils/
                │       ├── SecurityConfig.java
                │       ├── JwtUtils.java
                │       └── JwtAuthenticationEntryPoint.java
                └── user/
                    ├── controller/
                    │   └── AuthController.java
                    ├── dto/
                    │   ├── request/
                    │   │   ├── UserCreateDto.java
                    │   │   └── LoginRequestDto.java
                    │   └── response/
                    │       ├── UserResponseDto.java
                    │       └── JwtResponseDto.java
                    ├── model/
                    │   ├── User.java
                    │   ├── Role.java
                    │   └── UserRole.java
                    ├── repository/
                    │   └── UserRepository.java
                    ├── service/
                    │   ├── AuthService.java
                    │   ├── UserDetailsServiceImpl.java
                    │   └── UserPrincipal.java
                    ├── mapper/
                    │   └── UserMapper.java
                    ├── validation/
                    │   └── UserValidation.java
                    └── exception/
                        ├── GlobalExceptionHandler.java
                        ├── UserNotFoundException.java
                        ├── InvalidUserException.java
                        └── DatabaseOperationException.java
```

## Security Features

1. **Password Hashing**: BCrypt with configurable strength
2. **JWT Tokens**: HMAC SHA-256 signed tokens
3. **Input Validation**: Comprehensive validation for all inputs
4. **Audit Logging**: Security events are logged with context
5. **Exception Handling**: Secure error messages without information leakage
6. **CORS Configuration**: Configurable cross-origin resource sharing

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `MYSQL_HOST` | MySQL host | `localhost` |
| `MYSQL_PORT` | MySQL port | `3306` |
| `MYSQL_DB` | Database name | `quizdb` |
| `MYSQL_USER` | Database username | `root` |
| `MYSQL_PASSWORD` | Database password | `password` |
| `JWT_SECRET` | JWT signing secret | Auto-generated |
| `JWT_EXPIRATION_MS` | Token expiration in milliseconds | `86400000` (24h) |
| `SERVER_PORT` | Application port | `8080` |

## Testing

Run tests:
```bash
mvn test
```

Run specific test:
```bash
mvn test -Dtest=JwtUtilsTest
```

## API Documentation

For detailed API documentation, see [JWT_API_DOCUMENTATION.md](JWT_API_DOCUMENTATION.md)

## Development

### Building
```bash
mvn clean compile
```

### Running with different profiles
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Creating executable JAR
```bash
mvn clean package
java -jar target/quiz-boot-0.0.1-SNAPSHOT.jar
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
