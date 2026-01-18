# Task Manager API

A RESTful API for managing tasks, built using a feature-based **controller–service–repository** architecture.

**Tech stack:** Spring Boot, Spring Data JPA, MySQL

---

## Features

### User
1. Register an account
2. Log in
3. Create, update, delete, and retrieve tasks
4.  Search tasks by prefix
5.  Retrieve tasks with filtering
6. Retrieve tasks with multiple sorting options

### Admin
1. View all users in the system
2. Access all standard user features

---

## Project Highlights
1. Secure password storage using the **Argon2** slow hashing algorithm
2. Authentication and authorization via **JWT access tokens**
3. **Revocable refresh tokens** for improved security
4. Centralized exception handling for cleaner and more maintainable code
5. Comprehensive validation for request bodies, query parameters, and path variables
6. Application logging with sensitive credentials redacted to support debugging while preserving confidentiality
7. Standardized API responses with built-in support for **infinite scrolling** in list-retrieval endpoints