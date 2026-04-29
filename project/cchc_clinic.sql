DROP DATABASE IF EXISTS cchc_clinic;
CREATE DATABASE cchc_clinic CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cchc_clinic;


CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL, -- PATIENT, STAFF, ADMIN
    full_name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clinics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100)
);

CREATE TABLE services (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);


CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    service_id INT NOT NULL,
    clinic_id INT,
    slot_time DATETIME NOT NULL,
    status VARCHAR(50) DEFAULT 'BOOKED',
    cancellation_reason TEXT, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES users(id)
);


CREATE TABLE queue_entries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    clinic_id INT NOT NULL,
    service_id INT NOT NULL,
    queue_date DATE NOT NULL,
    queue_number INT NOT NULL,
    status VARCHAR(20) DEFAULT 'WAITING',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES users(id)
);


CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50), -- SYSTEM, URGENT
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE operational_issues (
    id INT AUTO_INCREMENT PRIMARY KEY,
    clinic_id INT NOT NULL,
    issue_type VARCHAR(100) NOT NULL,
    description TEXT,
    reported_by INT NOT NULL,
    reported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO clinics (name, location) VALUES ('Chai Wan Clinic', 'Chai Wan'), ('Sha Tin Clinic', 'Sha Tin');
INSERT INTO services (name, description) VALUES ('General Consultation', 'Routine checkup'), ('Vaccination', 'Flu shot');


INSERT INTO users (username, password, role, full_name, phone) VALUES 
('staff', 'staff123', 'STAFF', 'Gordon Staff', '23456789'),
('patient', 'patient123', 'PATIENT', 'John Doe', '98765432');

INSERT INTO appointments (patient_id, service_id, clinic_id, slot_time, status) VALUES 
(2, 1, 1, DATE_ADD(NOW(), INTERVAL 1 HOUR), 'BOOKED');

INSERT INTO queue_entries (patient_id, clinic_id, service_id, queue_date, queue_number, status) VALUES 
(2, 1, 1, CURDATE(), 1001, 'WAITING');