-- ============================================================
-- StressGuard AI – Local Development Seed Data ONLY
-- WARNING: DO NOT RUN THIS SCRIPT IN A PRODUCTION DATABASE.
-- ============================================================

USE stress_db;

-- 1. Sample Local Administrator (password: "admin123")
INSERT INTO users (username, email, password, full_name, age, gender, role, active, created_at, updated_at)
VALUES (
  'admin',
  'admin@stressguard.ai',
  '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE2ceOPlXtyI1i1ia',
  'System Administrator (Dev)',
  30,
  'Other',
  'ADMIN',
  1,
  NOW(),
  NOW()
) ON DUPLICATE KEY UPDATE id=id;

-- 2. Sample Local User (password: "user123")
INSERT INTO users (username, email, password, full_name, age, gender, role, active, created_at, updated_at)
VALUES (
  'testuser',
  'test@example.com',
  '$2a$10$Xn3the3xJEnPhMBVVzj7H.eiCqyYFZx9BnKPxpRt1OZ/TFuMiQ1YC',
  'Test User (Dev)',
  22,
  'Male',
  'USER',
  1,
  NOW(),
  NOW()
) ON DUPLICATE KEY UPDATE id=id;

SELECT 'Local development seed accounts provisioned.' AS status;
