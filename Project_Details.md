# AI-Based Human Stress Prediction System

## Project Title

**AI-Based Human Stress Prediction System**

---

## Problem Statement

Stress has become a major issue among students and working professionals due to increasing academic pressure, work demands, social expectations, and technological dependence. Future generations are expected to face even higher levels of stress because of growing competition and fast-paced lifestyles.

Traditional methods of stress assessment are time-consuming and often require expert evaluation. Therefore, there is a need for an intelligent system that can automatically predict stress levels and provide early insights for better mental health management.

---

## Project Objectives

* Predict human stress levels using Machine Learning techniques.
* Detect stress at an early stage.
* Provide accurate and real-time stress analysis.
* Maintain user records securely.
* Visualize stress data using dashboards and charts.
* Improve awareness about mental health and stress management.
* Support future integration with wearable health devices.

---

## Module List

### 1. User Management Module

* User Registration
* User Login
* Profile Management
* Password Management

### 2. Stress Assessment Module

* Collect user stress-related information
* Validate user inputs
* Store assessment data

### 3. Stress Prediction Module

* Data Preprocessing
* Random Forest Prediction
* Stress Level Classification

### 4. Dashboard & Analytics Module

* Display Stress Reports
* Show Stress Trends Using Charts
* View Prediction History

### 5. Admin Module

* Manage Users
* Monitor Prediction Records
* View System Reports

### 6. Security Module

* JWT Authentication
* User Authorization
* Secure Data Access

---

## Database Tables

### User Table

| Field Name | Type     |
| ---------- | -------- |
| user_id    | INT (PK) |
| name       | VARCHAR  |
| email      | VARCHAR  |
| password   | VARCHAR  |
| age        | INT      |
| gender     | VARCHAR  |

### Stress_Assessment Table

| Field Name        | Type     |
| ----------------- | -------- |
| assessment_id     | INT (PK) |
| user_id           | INT (FK) |
| sleep_hours       | FLOAT    |
| work_pressure     | INT      |
| academic_pressure | INT      |
| physical_activity | INT      |
| assessment_date   | DATE     |

### Stress_Prediction Table

| Field Name       | Type     |
| ---------------- | -------- |
| prediction_id    | INT (PK) |
| assessment_id    | INT (FK) |
| stress_level     | VARCHAR  |
| prediction_score | FLOAT    |
| prediction_date  | DATE     |

### Admin Table

| Field Name | Type     |
| ---------- | -------- |
| admin_id   | INT (PK) |
| admin_name | VARCHAR  |
| email      | VARCHAR  |
| password   | VARCHAR  |

### Analytics_Report Table

| Field Name   | Type     |
| ------------ | -------- |
| report_id    | INT (PK) |
| user_id      | INT (FK) |
| stress_trend | VARCHAR  |
| report_date  | DATE     |

---

## Expected Outcome

* Accurate prediction of user stress levels.
* Early identification of stress-related issues.
* Secure storage and management of user data.
* Interactive dashboard with charts and reports.
* Improved awareness of mental health conditions.
* Better decision-making through data-driven insights.
* Scalable system for future enhancements such as mobile applications and wearable device integration.

---

## Technologies Used

### Frontend

* HTML
* CSS
* Bootstrap
* React.js

### Backend

* Java
* Spring Boot

### Database

* MySQL

### Machine Learning

* Python
* Scikit-learn
* Random Forest Algorithm

### Security

* JWT Authentication

### Visualization

* Chart.js
