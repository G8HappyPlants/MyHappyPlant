MyHappyPlant 🌱

MyHappyPlant is a full-stack web application for managing plants and plant care.  
The project is built with **Spring Boot** (backend), **MySQL** (database), and **React + Vite** (frontend).

This repository contains both backend and frontend code.

---

## Tech Stack

### Backend
- Java 23
- Spring Boot
- Spring Data JPA
- Spring Security (development configuration)
- MySQL 8
- Maven

### Frontend
- React
- Vite
- JavaScript
- npm

---

## Project Structure
MyHappyPlant/
│
├── backend/ # Spring Boot application
│ ├── app/src/main/java
│ ├── app/src/main/resources
│ └── app/pom.xml
│
├── frontend/ # React + Vite application
│ ├── src
│ ├── index.html
│ ├── vite.config.js
│ └── package.json
│
├── .env # Environment variables (NOT committed)
├── .gitignore
└── README.md

---

## Database Configuration

The application connects to a **MySQL** database.

Database credentials are stored in a local `.env` file (not committed to Git).

### `.env` example
```env
DB_USER=your_db_username
DB_PASSWORD=your_db_password
```

## Running the Project (Development)
**Backend (Spring Boot)**
Requirements:
- Java 23 
- Maven 
- Access to the MySQL database

Steps:
```
cd backend
mvn spring-boot:run
```
Backend runs on:
```
localhost:8080
```
**Frontend (React + Vite)**

Requirements:
- Node.js (LTS recommended)
- npm

Steps:
```
cd frontend
npm install
npm run dev
```
Frontend runs on:
http://localhost:5173

The frontend uses a Vite proxy to communicate with the backend (/api → localhost:8080).

## Git & Environment Variables

- `.env` is ignored by Git
- Each developer must create their own local `.env` file
- No credentials are stored in the repository


## Main contributors
VenomiZeD - Melvin B.
TheBiggerArtist - Amin A.
TF139 - Torun F.
Jawadigital - Mohammad J.
Lexelicious - David L.
SaraMStar - Sara M.
lowisacs - Lowisa S.C.