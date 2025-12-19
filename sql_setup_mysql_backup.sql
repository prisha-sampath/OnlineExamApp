-- SQL Setup for Online Exam App
-- Run this script in MySQL to create the quiz_results table

-- Create quiz_results table to store quiz attempt data
CREATE TABLE IF NOT EXISTS quiz_results (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    quiz_type VARCHAR(20) NOT NULL,
    score INT NOT NULL,
    total_questions INT NOT NULL,
    percentage INT NOT NULL,
    attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
    INDEX idx_username (username),
    INDEX idx_quiz_type (quiz_type),
    INDEX idx_attempt_date (attempt_date)
);

SHOW TABLES LIKE 'quiz_results';


DESCRIBE quiz_results;