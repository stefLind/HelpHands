# HelpHands  
### A Platform for Donating Manual Labor, Services, Items and Food

HelpHands is a Java-based web application developed for **educational and academic purposes**. The platform is designed to connect volunteers, individuals, and organizations who want to offer **free manual labor, services, items or food donations** to people and communities in need.

The application enables users to create and manage donation campaigns, apply to participate in them, and track campaign progress in a transparent and structured way.

---

## 📖 About the Project

HelpHands provides a centralized platform where users can:

- Create and manage donation campaigns  
- Apply to participate in campaigns as a volunteer or helper  
- Review and manage participation requests  
- Track campaign progress and participation details  

The project is part of a **Master’s thesis** and focuses on building a fully functional, secure, and well-structured Spring-based web application.

---

## ✨ Features

### 👤 User Management & Security
- Role-based user registration and authentication  
- Secure login and password management  
- Three user roles:
  - **User** – individual helper or campaign creator  
  - **Company** – organization managing multiple employees  
  - **Employee** – user linked to a company account  

---

### 🔍 Campaign Search & Discovery
- Search campaigns by:
  - Campaign creator (user or company name)
  - Location
  - Type of help needed (manual labor, food, services, or items)
  - Campaign status (active, closed, or cancelled)

---

### 🤝 Donations & Participation
- Users can apply to campaigns by submitting a donation or participation request  
- Applications may include message to the owner such as:
  - Questions about campaign details  
  - Description of the offered help (e.g. gardening, moving assistance, tutoring, repairs)

---

### 🗂 Donation Request Management
- Campaign owners can:
  - Review incoming participation requests  
  - Approve or reject applications  
  - Manage campaign participation flow  

---

### 📊 Participation Dashboard
- View detailed campaign and participation information:
  - Donation and participation history  
  - Number of required helpers vs. applied helpers
  - Information about participants  

---

### 🏢 Company Functionality
- Company accounts can:
  - Manage employees registered under the company 

---

## 🛠 Tech Stack

HelpHands is built using a **modern Java and Spring-based technology stack**, focused on clean architecture, modularity, and full-stack web development.

---

## 🔙 Backend

- **Java 21** – Core programming language  
- **Spring Boot 3.5.7** – Framework for rapid application development  

### Spring Boot Modules & Starters
- **Spring Data JPA** – ORM for database access using Java entities  
- **Spring Security** – Authentication and authorization  
- **Spring Web** – RESTful API development and HTTP request handling  
- **Spring Validation** – Input validation for forms and DTOs  
- **Spring Boot Actuator** – Application monitoring and health checks  

### Additional Libraries
- **Lombok** – Reduces boilerplate code (getters, setters, constructors, etc.)

---

## 🗄 Database

- **MySQL** – Relational database used in production  
- **Hibernate** – ORM framework for database persistence  

---

## 🎨 Frontend

- **HTML5** 
- **CSS3** 
- **JavaScript**
- **Thymeleaf** – Server-side Java template engine for dynamic HTML rendering  

---

## 📦 Build & Dependency Management

- **Maven** – Project build and dependency management tool  
- **Spring Boot Maven Plugin** – Packaging and running the application with Spring Boot support  

---

## 🎓 Purpose

This project is developed as part of a **Master’s degree thesis** and demonstrates:
- Full-stack web application development  
- Secure user and role management  
- RESTful design principles  
- Clean, modular Spring architecture  
- Practical application of Java and Spring technologies  

---

## 📌 License

This project is intended for **educational use only**.
