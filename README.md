# Incident Management System

This is a full-stack Incident Management System with a **React frontend** and a **Spring Boot backend**. The system allows users to manage incidents by creating, updating, deleting, and listing all incidents.

## Technologies Used
- **Frontend**: React, Axios
- **Backend**: Spring Boot, Java 17, H2 Database (in-memory)
- **Docker**: Docker and Docker Compose
- **Node.js**: v20.11.1

## Running the Application with Docker Compose

The application can be run using Docker Compose, which will spin up both the frontend and backend services.

### 1. Clone the Repository

```bash
git clone <repository-url>
cd <repository-directory>
```

### 2. Build and Run the Containers

```bash
docker-compose up --build
```

## Access the Application

- Frontend (React): http://localhost:3000
- Backend (Spring Boot API): http://localhost:8080

## Stopping the Application

```bash
docker-compose down
```
