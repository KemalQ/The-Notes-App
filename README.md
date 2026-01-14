# Notes App

A simple Notes application built with Java Spring Boot and MongoDB, fully containerized with Docker.

---

## 🔹 Contents

- [Description](#description)
- [Technologies](#technologies)
- [Requirements](#requirements)
- [Running with Docker Compose](#running-with-docker-compose)
- [API](#api)


---

## 📌 Description

Notes App is a RESTful application for creating, reading, updating, and deleting notes.  
It uses MongoDB as the database and Spring Boot for the backend.  
The whole stack is containerized using Docker and Docker Compose.

---

## 🛠 Technologies

- Java 21 + Spring Boot 3
- MongoDB 8
- Docker & Docker Compose
- Maven (or `mvnd`) for building

---

## ⚙ Requirements

- Docker >= 24
- Docker Compose >= 2
- (Optional) Maven or `mvnd` for local build

---

## 🚀 Running with Docker Compose

Clone the repository:

```bash
git clone <https://github.com/KemalQ/The-Notes-App.git>
cd notesApp
```

### Build Docker images:
```bash
docker-compose build
```
### Start the services:
```bash
docker-compose up
```
### Check if everything is running:
- Application: http://localhost:8080
- MongoDB: via CLI or mongo-express, port 27017

### Stop the services:
```bash
docker-compose down
```

## 📡 API

Basic CRUD and additional endpoints for notes:

| Method | URL                   | Description                                                |
| ------ |-----------------------| ---------------------------------------------------------- |
| POST   | /api/notes            | Create a new note                                          |
| GET    | /api/notes            | Get all notes (title + created date only)                 |
| GET    | /api/notes/{id}       | Get note details by id (text + tags)                      |
| GET    | /api/notes/{id}/stats | Get statistics for a note by id (word count, etc.)        |
| PUT    | /api/notes/{id}       | Update a note by id                                        |
| DELETE | /api/notes/{id}       | Delete a note by id                                        |
