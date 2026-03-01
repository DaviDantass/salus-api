# Salus API

A robust and scalable healthcare management REST API built with Spring Boot 3, featuring appointment scheduling, doctor and patient management with an integrated authentication system.

## Features

- **User Authentication** - JWT-based authentication with bcrypt password encryption
- **Appointment Scheduling** - Book, reschedule, and cancel medical appointments
- **Doctor Management** - Register and manage doctors by specialty
- **Patient Management** - Register and manage patient information
- **Smart Doctor Assignment** - Automatic doctor selection based on availability and specialty
- **Validation Rules** - Comprehensive business logic validation
  - Clinic operating hours (7 AM - 6 PM, Monday-Friday)
  - Minimum 30 minutes appointment notice
  - Minimum 24 hours cancellation notice
  - One appointment per patient per day
  - Doctor availability check
- **API Documentation** - Swagger UI and OpenAPI 3.0
- **Database Migrations** - Flyway for version control
- **Soft Delete** - Logical deletion for doctors and patients

## 🛠 Tech Stack

- **Java 21** - Latest LTS version
- **Spring Boot 3.3.11** - Modern application framework
- **Spring Data JPA** - Database abstraction
- **Spring Security** - Authentication and authorization
- **JWT (java-jwt)** - Token-based stateless authentication
- **MySQL 8** - Relational database
- **Flyway** - Database migration tool
- **Lombok** - Boilerplate reduction
- **Swagger/SpringDoc OpenAPI** - API documentation
- **Junit 5 & Mockito** - Testing framework
- **Maven 3.9.12** - Dependency management

## Prerequisites

- **Java 21 JDK** - [Download](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- **Maven 3.9+** - [Download](https://maven.apache.org/download.cgi) or use mvnw
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/mysql/)
- **Git** - Version control

## 📁 Project Structure

```
salus-api/
├── src/
│   ├── main/
│   │   ├── java/com/davidantassdev/salus/
│   │   │   ├── Application.java                 # Main entry point
│   │   │   ├── controller/                      # REST Controllers
│   │   │   │   ├── AutenticacaoController
│   │   │   │   ├── MedicoController
│   │   │   │   ├── PacienteController
│   │   │   │   ├── ConsultaController
│   │   │   │   ├── UsuarioController
│   │   │   │   └── HealthController
│   │   │   ├── domain/                          # Business logic layer
│   │   │   │   ├── usuario/                     # User/Auth domain
│   │   │   │   ├── medico/                      # Doctor domain
│   │   │   │   ├── paciente/                    # Patient domain
│   │   │   │   ├── consultas/                   # Appointment domain
│   │   │   │   ├── endereco/                    # Address embedded
│   │   │   │   └── ValidacaoException.java
│   │   │   └── infra/                           # Infrastructure
│   │   │       ├── security/                    # JWT & Auth config
│   │   │       ├── exception/                   # Global error handling
│   │   │       └── springdoc/                   # Swagger config
│   │   └── resources/
│   │       ├── application.properties           # Main config
│   │       ├── application-prod.properties      # Production config
│   │       ├── application-test.properties      # Test config
│   │       └── db/migration/                    # Flyway migrations (V1-V7)
│   └── test/
│       └── java/com/davidantassdev/salus/
│           ├── controller/                      # Controller tests
│           └── domain/                          # Domain tests
├── pom.xml                                      # Maven configuration
├── Dockerfile                                   # Container image
├── .env.example                                 # Environment variables template
├── .gitignore                                   # Git ignore rules
└── README.md                                    # This file
```

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/salus-api.git
cd salus-api
```

### 2. Set Up Environment Variables

Copy the example environment file and update with your values:

```bash
cp .env.example .env
```

Edit `.env` with your database credentials and JWT secret.

### 3. Create MySQL Database

```sql
CREATE DATABASE salus_api;
CREATE DATABASE salus_api_test;
```

Create a user with privileges:

```sql
CREATE USER 'salus_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON salus_api.* TO 'salus_user'@'localhost';
GRANT ALL PRIVILEGES ON salus_api_test.* TO 'salus_user'@'localhost';
FLUSH PRIVILEGES;
```

### 4. Build the Project

```bash
./mvnw clean install
```

Or on Windows:

```cmd
mvnw.cmd clean install
```

## ⚙️ Configuration

### Environment Variables

Set the following environment variables (or update `application.properties`):

```properties
DATASOURCE_URL=jdbc:mysql://localhost:3306/salus_api?useSSL=false&serverTimezone=UTC
DATASOURCE_USERNAME=salus_user
DATASOURCE_PASSWORD=your_secure_password
JWT_SECRET=your_super_secret_jwt_key_min_32_chars
```

### Application Profiles

The project supports multiple profiles:

- **development** (default) - Uses `application.properties`
- **production** - Uses `application-prod.properties`
- **test** - Uses `application-test.properties`

Activate a profile:

```bash
java -Dspring.profiles.active=production -jar salus-api-0.0.1-SNAPSHOT.jar
```

## Running the Application

### Using Maven

```bash
./mvnw spring-boot:run
```

### Using JAR

```bash
./mvnw package
java -jar target/salus-api-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### Authentication

**Login**
```
POST /login
Content-Type: application/json

{
  "login": "user123",
  "senha": "password"
}

Response: 200 OK
{
  "tokenJWT": "eyJhbGc..."
}
```

**Register User**
```
POST /usuarios
Authorization: Bearer {token}
Content-Type: application/json

{
  "login": "newuser",
  "senha": "password"
}
```

### Doctors

```
POST /medicos                              # Create doctor
GET /medicos                               # List active doctors (paginated)
GET /medicos/{id}                          # Get doctor details
PUT /medicos                               # Update doctor
DELETE /medicos/{id}                       # Soft delete doctor
```

### Patients

```
POST /pacientes                            # Register patient
GET /pacientes                             # List active patients (paginated)
PUT /pacientes                             # Update patient
DELETE /pacientes/{id}                     # Soft delete patient
```

### Appointments

```
POST /consultas                            # Schedule appointment
DELETE /consultas                          # Cancel appointment
```

### Health

```
GET /api/health                            # Health check endpoint
```

### Documentation

```
GET /swagger-ui.html                       # Swagger UI
GET /v3/api-docs                           # OpenAPI JSON
```

## 🗄️ Database Migrations

Migrations are automatically applied on startup using Flyway.

**Migration Files:**
- V1 - Create medicos table
- V2 - Add telefone column to medicos
- V3 - Add ativo column to medicos
- V4 - Create pacientes table
- V5 - Create usuarios table
- V6 - Create consultas table
- V7 - Add motivo_cancelamento to consultas

To create a new migration, add a file to `src/main/resources/db/migration/` following the naming convention: `V{number}__{description}.sql`

## 🧪 Testing

Run all tests:

```bash
./mvnw test
```

Run specific test class:

```bash
./mvnw test -Dtest=MedicoRepositoryTest
```

Run with coverage:

```bash
./mvnw test jacoco:report
```

**Test Coverage:**
- Controller layer integration tests
- Repository layer tests with real database
- Business logic validation tests
- Mock-based unit tests


Run with: `docker-compose up -d`

## 🔐 Security

### Features

- **JWT Authentication** - Stateless token-based auth
- **Password Encryption** - BCrypt with strength 10
- **CORS Protection** - Configurable cross-origin requests
- **Role-Based Access** - All endpoints require authentication except login
- **HTTPS Ready** - Configure SSL certificates for production

### Best Practices

1. **Never commit secrets** - Use environment variables
2. **Rotate JWT secrets** - Regularly update JWT_SECRET
3. **Use HTTPS** - Always use HTTPS in production
4. **Rate Limiting** - Implement rate limiting for login endpoint
5. **Input Validation** - All inputs are validated with @Valid annotations

## 📋 Appointment Booking Rules

- **Hours**: 7 AM - 6 PM
- **Days**: Monday - Friday only
- **Notice**: Minimum 30 minutes in advance
- **Cancellation**: Minimum 24 hours notice
- **Frequency**: One appointment per patient per day
- **Doctor**: Auto-selected by specialty or manually chosen


## API Authentication Flow

1. **Register a user** via `/usuarios` endpoint (requires admin token)
2. **Login** via `/login` to get JWT token
3. **Use token** in Authorization header: `Authorization: Bearer {token}`
4. **Token expires** after 2 hours

Example request with token:

```bash
curl -H "Authorization: Bearer eyJhbGc..." http://localhost:8080/medicos
```

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

