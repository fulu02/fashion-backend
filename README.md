README.md

# Fashion Backend (Spring Boot)

An e-commerce backend API built with **Java Spring Boot**.  
This project is a **Work in Progress (WIP)** and is being developed as a production-style portfolio project.

## Tech Stack
- Java
- Spring Boot
- Spring Data JPA
- Maven
- (DB) PostgreSQL / H2 (local dev)

## Architecture
- Layered structure: **Controller → Service → Repository**
- DTO-based request/response models
- Mapper pattern for clean conversions (Entity ↔ DTO)

## Current Features
- Product domain base structure
- Product CRUD service layer (in progress)
- REST API endpoints (initial)

## Planned Features
- Category management
- Authentication & Authorization
- Cart & Order management
- Payment integration (Iyzico)
- Dockerization & CI/CD pipeline

## Getting Started

### 1) Requirements
- Java 17+ (or your configured version)
- Maven

### 2) Run
```bash
./mvnw spring-boot:run


