CREATE DATABASE IF NOT EXISTS internalhub;

USE internalhub;

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    role_type ENUM('EMPLOYEE', 'MANAGER', 'HR', 'ADMIN') NOT NULL,
    seniority_level ENUM('INTERN', 'JUNIOR', 'MID', 'SENIOR', 'EXECUTIVE') NOT NULL,
    min_salary DECIMAL(10,2) NOT NULL,
    max_salary DECIMAL(10,2) NOT NULL,
    description TEXT NOT NULL
    );

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    corporate_email VARCHAR(250) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    personal_email VARCHAR(250) NOT NULL UNIQUE,
    salary DECIMAL(10,2),
    department ENUM('ENGINEERING', 'IT', 'PRODUCT', 'SUPPORT', 'HR', 'FINANCE') NOT NULL,
    work_model ENUM('REMOTE', 'HYBRID', 'ONSITE') DEFAULT 'ONSITE',
    hire_date DATE,
    is_active boolean DEFAULT TRUE,

    role_id BIGINT,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
    );

--  Initial data insertion
INSERT IGNORE INTO roles (id, role_name, role_type, seniority_level, min_salary, max_salary, description) VALUES
                                                                                      (1, 'System Administrator', 'ADMIN', 'SENIOR', 13000.00, 20000.00, 'Full system administrator with unrestricted access'),
                                                                                      (2, 'HR Analyst', 'HR','SENIOR', 8900.00, 11900.00, 'Senior Human Resources Analyst responsible for strategic talent management and personnel administration');

INSERT IGNORE INTO users (employee_id, first_name, last_name, corporate_email, password_hash, personal_email, salary, department, work_model, hire_date, is_active, role_id) VALUES
                                                                                                                                                                          ('IH-ADMIN01', 'Sys', 'Admin', 'sysadmin@internalhub.com', '$argon2id$v=19$m=19456,t=2,p=1$QuyQwk3ixBncjqKj5RwhHw$LbOtRFyzuTaPVyCS5t+JWxA3+XJ7l4MB8Oq05WU4Smg', 'personal@email.com', 18000.00, 'IT', 'REMOTE', '2024-01-15', TRUE, 1),
                                                                                                                                                                          ('IH-Y6FX9G', 'Carol', 'Bergamaschi', 'cbergamaschi@internalhub.com', '$argon2id$v=19$m=19456,t=2,p=1$VZGyV8EVcGTf9pZTG7czNQ$RTomxyG3nuiJ5ViINMFJE6v9h1BfCXZDq4qp3O2luMc', 'carolbergamaschi@email.com', 10500.00, 'HR', 'HYBRID', '2025-06-10', TRUE, 2);