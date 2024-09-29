# Incident Management Backend (Spring Boot)

This is the backend API for the Incident Management System, built with Spring Boot. 
It provides REST APIs for managing incidents, including creating, updating, retrieving, and deleting incidents.

## Technologies Used
- **Spring Boot**: Java-based framework for building the backend.
- **H2 Database**: In-memory database for storing incident data.
- **Java**: Version 17

## Requirements
- **Java 17**
- **Maven** (for building the project)
- **Docker** (for containerization)

## Running the Backend

### 1. Build the Backend

```bash
mvn clean package
```

This will generate a JAR file in the target directory.

### 2. Running the Backend Locally

You can run the Spring Boot application locally using Maven:

```bash
mvn spring-boot:run
```

### 3. Running the Backend with Docker

Build and run the Docker container:

```bash
docker build -t spring-boot-backend .
docker run -p 8080:8080 spring-boot-backend
```

## Backend API Endpoints

- GET /incidents: Retrieve all incidents
- GET /incidents?page={page}&size={size}: Retrieve all incidents with pagination
    - Query Parameters:
        - page: The page number (0-based index).
        - size: The number of incidents per page.
- POST /incidents: Create a new incident
- PUT /incidents/{id}: Update an existing incident
- DELETE /incidents/{id}: Delete an incident by ID
- GET /incidents/{id}: Retrieve a specific incident by ID

## Environment Variables

The following environment variables can be configured:

```bash
SPRING_DATASOURCE_URL: Set the Spring Boot datasource URL (H2 database).
```

## Testing

### Run Unit Tests

To execute the tests, use the following command:

```bash
mvn test
```