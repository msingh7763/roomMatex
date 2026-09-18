# RoomMateX

RoomMateX is a Spring Boot application for a roommate matching and rental platform. It allows users to register, browse rooms, request bookings, connect with potential roommates, and manage admin tasks.

## Overview

This project is a backend service for a housing marketplace where:

- users can sign up and log in securely
- room owners can list available accommodations
- users can search and view room listings
- booking requests can be created and managed
- roommate matching suggestions can be generated
- admin users can manage roles and content
- payment, email, and notification features are integrated

## Tech Stack

- Java 17
- Spring Boot 3.2.2
- Spring Web
- Spring Data JPA
- Spring Security
- PostgreSQL
- JWT Authentication
- Redis support (available for caching)
- WebSocket support
- Spring Mail
- Razorpay payment integration
- Swagger / OpenAPI
- Maven

## Project Structure

```text
src/
  main/
    java/
      com/
        RoomMateX/
          auth/
          config/
          controller/
          dto/
          entity/
          enums/
          exception/
          repository/
          scheduler/
          security/
          service/
          websocket/
    resources/
      application.properties
  test/
    java/
      com/
        RoomMateX/
```

## Main Features

### Authentication

- registration
- login
- JWT token generation and refresh flow
- password reset and email verification
- logout support

### Room Management

- add rooms
- list paginated rooms
- search rooms by keyword
- view room details
- delete rooms (admin-only)

### Booking System

- submit room booking requests
- check personal bookings
- check owner bookings
- approve or reject bookings
- cancel bookings with payment logic

### Matching System

- view compatible roommate matches
- fetch top recommended matches

### Admin Features

- list users
- search users
- update user roles
- delete users
- dashboard-related admin endpoints

### Additional Features

- Razorpay integration
- notification support
- email sending
- webhook endpoint support
- Swagger API documentation

## Prerequisites

Before running the project, make sure you have:

- Java 17 or higher
- Maven
- PostgreSQL database
- Optional Redis for cache support
- SMTP email credentials
- Razorpay API keys

## Configuration

The project configuration is in:

```properties
src/main/resources/application.properties
```

You should update the following values for your environment:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/roommatex
spring.datasource.username=postgres
spring.datasource.password=your_password
jwt.secret=your_jwt_secret
razorpay.key=your_razorpay_key
razorpay.secret=your_razorpay_secret
spring.mail.username=your_email
spring.mail.password=your_email_password
```

## Running the Application

From the project root, run:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8081
```

## API Documentation

Swagger UI is available when the app is running:

```text
http://localhost:8081/swagger-ui/index.html
```

## Security

The backend uses Spring Security with JWT authentication. Most endpoints require a valid token, and admin endpoints are restricted by role.

## Notes

- The project is backend-focused and can be integrated with a frontend application.
- Some secrets and email credentials may be hardcoded in local development config files.
- For production use, move secrets to environment variables or a secure configuration system.

## License

No explicit license has been added to the project yet.
