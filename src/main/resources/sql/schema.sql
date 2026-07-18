CREATE DATABASE IF NOT EXISTS internalhub;

USE internalhub;

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    role_type ENUM('EMPLOYEE', 'MANAGER', 'HR', 'ADMIN') NOT NULL,
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
    salary DECIMAL(10,2) NOT NULL,
    department ENUM('ENGINEERING', 'IT', 'PRODUCT', 'SUPPORT', 'HR', 'FINANCE'),
    work_model ENUM('REMOTE', 'HYBRID', 'ONSITE') DEFAULT 'ONSITE',
    hire_date DATE NOT NULL,
    is_active boolean DEFAULT TRUE,

    role_id BIGINT,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
    );
