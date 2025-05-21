# Internship Management System (Backend)

A comprehensive backend system for managing student internships, developed as part of the internship program at **Esprit School of Engineering**.

## Overview

This backend project provides API services for managing internships, meetings, tasks, complaints, documents, and users. It ensures secure data handling and communication between the system components.

## Features

- User Authentication with **JWT**
- Role-Based Authorization
- CRUD Operations for Internships, Meetings, Documents, and Tasks
- Complaint Management System
- Notification System
- Company and Offer Management
- Quiz-Based Internship Selection
- Kanban Task Board System
- Defense Scheduling

## Tech Stack

### Backend

- Spring Boot
- Spring Security
- JWT Authentication
- MySQL
- Hibernate
- Postman (API Testing)

## Directory Structure

```
backend/
│   ├─ src/
│   │   ├─ main/
│   │   │   ├─ java/
│   │   │   ├─ tn.esprit.innoxpert/
│   ├─ pom.xml
```
## Module Assignments

| **Module**             | **Developer**         |
|------------------------|-----------------------|
| User Management        | All                   |
| Internship Management  | Ghassen Ben Aissa     |
| Event Management       | Skander Mercini       |
| Complaint Management   | Skander Mercini       |
| Company Management     | Youssef Sayari        |
| Publication Management | Youssef Sayari        |
| Document Management    | Amine Kaboubi         |
| Quiz Management        | Ala Gafsi             |
| Meeting Management     | Amanellah Kthiri      |
| Convention Management  | Ghassen Ben Aissa     |
| Defense Management     | Amine Kaboubi         |
| Notification System    | Ala Gafsi             |
| Task Management        | Amanellah Kthiri      |


## Security

The backend uses **Spring Security** and **JWT Authentication** to protect endpoints and manage user access.

## API Documentation

You can test the APIs using **Postman** or Swagger (if integrated).

## Getting Started

1. Clone the repository:

```bash
git clone https://github.com/youssefsayari/internshipEspritplatformBack.git
```

2. Configure the **application.properties** file with your MySQL database credentials.
3. Install dependencies:

```bash
mvn install
```

4. Run the backend:

```bash
mvn spring-boot:run
```

## UML Diagrams

![Class Diagram Overview.png](..%2FSprint%200%2FClass%20Diagram%20Overview.png)

## Acknowledgments

This backend system was developed as part of the internship management project at **Esprit School of Engineering**.

---

# Topics

- spring-boot
- jwt-authentication
- esprit-school-of-engineering
- internship-management
- backend

