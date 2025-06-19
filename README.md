# Blog Platform (Java + Spring Boot)

A secure and structured backend application for a multi-user blog platform. Built using **Java 21** and **Spring Boot**, this project focuses on authentication, clean architecture, and well-documented code.

---

## 🛠 Tech Stack

- Java 21  
- Spring Boot  
- Spring Security + JWT  
- Spring Data JPA  
- PostgreSQL  
- MapStruct  
- Lombok  
- Docker  
- Maven  

---

## 🔐 Features

- **JWT-based authentication** with login and registration endpoints
- **Spring Security filter chain** to secure all protected routes
- Stateless request handling with custom **JWT extraction and validation**
- **Global Exception Handling** for better error messaging and control
- Support for multiple users:
  - Create, edit, and delete blog posts
  - Save drafts or publish blogs
  - Assign a category to each blog
  - Add up to 10 tags per blog
  - Automatic **reading time calculation**
- **DTOs** used for requests, responses, and internal data transfer
- **MapStruct** for clean mapping between DTOs and entities
- Clean modular folder structure following best practices
- Dockerized setup for database and local development

---

## 🐳 Docker Support

Docker is used for a containerized PostgreSQL setup and ease of development.  
Make sure Docker is installed and running.

---

## 🤫 Secrets & Passwords

- Before running, make sure that you have 2 environment variables set up: DB_USER, DB_PASSWORD (I have set them up using a .env file)
- These are needed in docker-compose.yml & src > main > resources > application.properties

---

## 👤 User creation

- To create new users access this file: src > main > java > com...Blog_Platgorm > config > SecurityConfig.java > userDetailsService
- Enter the email or password according to your wish and run the application to save it into your database

## 💻 Frontend

- To run the frontend navigate to frontend directory in your terminal then do these 2 steps:
    1. npm install (ignore the warnings)
    2. npm run dev
- And voila! you will get the link for frontend
