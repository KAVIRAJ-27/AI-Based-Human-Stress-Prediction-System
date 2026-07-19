-- ============================================================
-- StressGuard AI – MySQL Database Setup Script
-- Run this before starting the application
-- ============================================================

CREATE DATABASE IF NOT EXISTS stress_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE stress_db;

-- Create admin user (Spring Boot JPA will create tables automatically)
-- This script just seeds the initial admin account

-- After running the app once (JPA creates tables), run this to seed admin:
-- Password is BCrypt of "admin123"
INSERT INTO users (username, email, password, full_name, age, gender, role, active, created_at, updated_at)
VALUES (
  'admin',
  'admin@stressguard.ai',
  '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE2ceOPlXtyI1i1ia',
  'System Administrator',
  30,
  'Other',
  'ADMIN',
  1,
  NOW(),
  NOW()
) ON DUPLICATE KEY UPDATE id=id;

-- Sample regular user (password: "user123")
INSERT INTO users (username, email, password, full_name, age, gender, role, active, created_at, updated_at)
VALUES (
  'testuser',
  'test@example.com',
  '$2a$10$Xn3the3xJEnPhMBVVzj7H.eiCqyYFZx9BnKPxpRt1OZ/TFuMiQ1YC',
  'Test User',
  22,
  'Male',
  'USER',
  1,
  NOW(),
  NOW()
) ON DUPLICATE KEY UPDATE id=id;

SELECT 'Database setup complete!' AS status;
SELECT 'Admin login: admin / admin123' AS info;
SELECT 'Test user login: testuser / user123' AS info2;
