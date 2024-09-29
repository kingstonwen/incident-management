# Incident Management Frontend (React)

This is the frontend for the Incident Management System, built using React. It allows users to manage incidents via a user-friendly interface, performing operations such as creating, updating, deleting, and retrieving incidents.

## Technologies Used
- **React**: JavaScript library for building the frontend.
- **Axios**: HTTP client for making API requests.
- **Node.js**: Version 20.11.1

## Requirements
- **Node.js v20.11.1** or later
- **Docker** (for containerization)

## Running the Frontend

### 1. Running Locally

Install dependencies and run the development server:

```bash
npm install
npm start
```

This will start the development server, and the app will be available at http://localhost:3000.

### 2. Running the Frontend with Docker
To build and run the Docker container:

```bash
docker build -t react-frontend .
docker run -p 3000:3000 react-frontend

```

#### Environment Variables

The frontend communicates with the backend through an API. The base URL of the backend API can be set using the following environment variable:

REACT_APP_BACKEND_URL: The URL of the backend API (e.g., http://localhost:8080).