-- PostgreSQL SQL Setup for Online Exam App

CREATE TABLE IF NOT EXISTS quiz_results (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    quiz_type VARCHAR(20) NOT NULL,
    score INTEGER NOT NULL,
    total_questions INTEGER NOT NULL,
    percentage INTEGER NOT NULL,
    attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_username
        FOREIGN KEY (username)
        REFERENCES users(username)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_quiz_results_username
    ON quiz_results(username);

CREATE INDEX IF NOT EXISTS idx_quiz_results_quiz_type
    ON quiz_results(quiz_type);

CREATE INDEX IF NOT EXISTS idx_quiz_results_attempt_date
    ON quiz_results(attempt_date);
