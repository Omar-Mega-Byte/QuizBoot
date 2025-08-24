# 🧠 QuizBoot

<div align="center">

![QuizBoot Logo](https://img.shields.io/badge/🧠-QuizBoot-blue?style=for-the-badge&color=4285f4)

**A Modern, Scalable Quiz Management System Built with Spring Boot**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

[🚀 Quick Start](#-quick-start) • [📖 Documentation](#-documentation) • [🛠️ API Reference](#️-api-reference) • [🤝 Contributing](#-contributing)

</div>

---

## ✨ Features

<table>
<tr>
<td width="50%">

### 🎯 **Quiz Management**
- **Multi-category quiz creation** with flexible question types
- **Real-time quiz sessions** with timer functionality
- **Advanced scoring algorithms** with instant feedback
- **Quiz publishing workflow** with approval system

</td>
<td width="50%">

### 👥 **User Experience**
- **Role-based access control** (Admin, Instructor, Student)
- **JWT-based authentication** with refresh tokens
- **Progress tracking** and analytics dashboard
- **Responsive web interface** for all devices

</td>
</tr>
<tr>
<td width="50%">

### 🔒 **Security & Performance**
- **Spring Security 6.x** integration
- **BCrypt password hashing**
- **Rate limiting** and CORS protection
- **Database optimization** with proper indexing

</td>
<td width="50%">

### 🏗️ **Modern Architecture**
- **Spring Modulith** for modular design
- **Clean Architecture** principles
- **RESTful API** with OpenAPI documentation
- **Production-ready** with monitoring support

</td>
</tr>
</table>

---

## 🚀 Quick Start

### Prerequisites

Before you begin, ensure you have the following installed:

- ☕ **Java 21** or higher ([Download](https://adoptium.net/))
- 🐬 **MySQL 8.0+** ([Download](https://dev.mysql.com/downloads/mysql/))
- 📦 **Maven 3.8+** (included via Maven Wrapper)
- 🔧 **Git** ([Download](https://git-scm.com/))

### 🏃‍♂️ Running Locally

1. **Clone the repository**
   ```bash
   git clone https://github.com/Omar-Mega-Byte/QuizBoot.git
   cd QuizBoot
   ```

2. **Set up the database**
   ```sql
   CREATE DATABASE quizdb;
   CREATE USER 'quizuser'@'localhost' IDENTIFIED BY 'quizpassword';
   GRANT ALL PRIVILEGES ON quizdb.* TO 'quizuser'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Configure environment variables** (optional)
   ```bash
   export MYSQL_HOST=localhost
   export MYSQL_PORT=3306
   export MYSQL_DB=quizdb
   export MYSQL_USER=quizuser
   export MYSQL_PASSWORD=quizpassword
   export SERVER_PORT=8080
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Access the application**
   - 🌐 **Application**: http://localhost:8080
   - 📚 **API Documentation**: http://localhost:8080/swagger-ui.html
   - ❤️ **Health Check**: http://localhost:8080/actuator/health

---

## 🐳 Docker Setup

```bash
# Build and run with Docker Compose (coming soon)
docker-compose up -d

# Or build manually
docker build -t quizboot .
docker run -p 8080:8080 --env-file .env quizboot
```

---

## 🛠️ API Reference

QuizBoot provides a comprehensive RESTful API. Here are some key endpoints:

### 🔐 Authentication
```http
POST /api/v1/auth/register     # User registration
POST /api/v1/auth/login        # User login
POST /api/v1/auth/refresh      # Token refresh
```

### 📝 Quiz Management
```http
GET    /api/v1/quizzes         # List all quizzes
POST   /api/v1/quizzes         # Create new quiz
GET    /api/v1/quizzes/{id}    # Get quiz details
PUT    /api/v1/quizzes/{id}    # Update quiz
DELETE /api/v1/quizzes/{id}    # Delete quiz
```

### 🏃‍♂️ Quiz Sessions
```http
POST /api/v1/sessions              # Start new quiz session
PUT  /api/v1/sessions/{id}/answers # Submit answer
POST /api/v1/sessions/{id}/submit  # Submit entire session
```

### 📊 Analytics
```http
GET /api/v1/analytics/users/{id}   # User analytics
GET /api/v1/analytics/quizzes/{id} # Quiz analytics
GET /api/v1/reports/user-progress  # Progress reports
```

> 📚 **Full API Documentation**: Available at `/swagger-ui.html` when running the application

---

## 🏗️ Architecture

QuizBoot follows a modern modular architecture using Spring Modulith:

```
📦 com.example.quiz_boot
├── 👤 user/              # User Management Module
├── 📝 quiz/              # Quiz Management Module  
├── 🏃 session/           # Session Management Module
├── 📊 analytics/         # Analytics & Reporting Module
└── 🔧 shared/            # Shared Components & Utilities
```

### Key Technologies

| Category | Technology | Purpose |
|----------|------------|---------|
| **Framework** | Spring Boot 3.5.4 | Main application framework |
| **Security** | Spring Security 6.x | Authentication & authorization |
| **Database** | MySQL 8.0+ & Hibernate | Data persistence |
| **Architecture** | Spring Modulith | Modular application design |
| **Documentation** | OpenAPI/Swagger | API documentation |
| **Monitoring** | Spring Actuator | Application monitoring |

> 🔍 **Detailed Architecture**: See [PROJECT_ARCHITECTURE.md](PROJECT_ARCHITECTURE.md) for comprehensive technical documentation

---

## 🔧 Development

### Building the Project

```bash
# Compile
./mvnw compile

# Run tests
./mvnw test

# Package
./mvnw package

# Skip tests (for faster builds)
./mvnw package -DskipTests
```

### Code Quality

We maintain high code quality standards:

- ✅ **80%+ test coverage** requirement
- 🔍 **SonarQube** integration for code analysis
- 📝 **Google Java Style Guide** compliance
- 🔒 **Mandatory code reviews** for all changes

### Development Profiles

| Profile | Purpose | Database |
|---------|---------|----------|
| `default` | Local development | MySQL |
| `test` | Testing | H2 in-memory |
| `staging` | Pre-production | MySQL cluster |
| `production` | Production | MySQL with replication |

---

## 🌟 Contributing

We welcome contributions! Here's how you can help:

### 🐛 Reporting Issues
- Use our [issue template](.github/ISSUE_TEMPLATE.md)
- Include detailed steps to reproduce
- Add relevant logs and screenshots

### 🚀 Submitting Changes
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes following our coding standards
4. Add tests for new functionality
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

### 📋 Development Setup

```bash
# Clone your fork
git clone https://github.com/YOUR_USERNAME/QuizBoot.git

# Add upstream remote
git remote add upstream https://github.com/Omar-Mega-Byte/QuizBoot.git

# Create development branch
git checkout -b feature/your-feature-name
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **Spring Team** for the amazing Spring Boot framework
- **Contributors** who help make this project better
- **Open Source Community** for inspiration and support

---

## 📞 Support

- 📧 **Email**: [your.email@example.com](mailto:your.email@example.com)
- 🐛 **Issues**: [GitHub Issues](https://github.com/Omar-Mega-Byte/QuizBoot/issues)
- 💬 **Discussions**: [GitHub Discussions](https://github.com/Omar-Mega-Byte/QuizBoot/discussions)

---

<div align="center">

**Made with ❤️ by the QuizBoot Team**

⭐ **Star this repo if you find it useful!** ⭐

[🔝 Back to Top](#-quizboot)

</div>