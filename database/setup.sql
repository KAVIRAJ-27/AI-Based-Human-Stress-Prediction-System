-- ============================================================
-- StressGuard AI – Cloud MySQL / TiDB Database Setup Script
-- Fully compatible with MySQL 8.0 and TiDB Cloud Serverless
-- Production-safe: Provisions schema only without default backdoor credentials.
-- ============================================================

CREATE DATABASE IF NOT EXISTS stress_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE stress_db;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(150),
  age INT,
  gender VARCHAR(20),
  phone VARCHAR(30),
  role VARCHAR(20) NOT NULL DEFAULT 'USER',
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Prediction Records Table
CREATE TABLE IF NOT EXISTS prediction_records (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  anxiety_level INT,
  self_esteem INT,
  mental_health_history INT,
  depression INT,
  headache INT,
  blood_pressure INT,
  sleep_quality INT,
  breathing_problem INT,
  noise_level INT,
  living_conditions INT,
  safety INT,
  basic_needs INT,
  academic_performance INT,
  study_load INT,
  teacher_student_relationship INT,
  future_career_concerns INT,
  social_support INT,
  peer_pressure INT,
  extracurricular_activities INT,
  bullying INT,
  stress_level INT,
  stress_label VARCHAR(30),
  confidence DOUBLE,
  low_probability DOUBLE,
  medium_probability DOUBLE,
  high_probability DOUBLE,
  recommendations TEXT,
  top_factors TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_prediction_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Note on Production User Accounts:
-- Initial administrator and user accounts should be created via the secure web
-- registration interface after frontend deployment, or by running database/seed-dev.sql
-- strictly in local development environments.

SELECT 'Production schema verified: users and prediction_records tables ready.' AS status;
