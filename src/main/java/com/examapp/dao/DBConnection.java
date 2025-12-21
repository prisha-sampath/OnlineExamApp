package com.examapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            String url = System.getenv("DATABASE_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (url == null || user == null || password == null) {
                throw new RuntimeException("DATABASE_URL / DB_USER / DB_PASSWORD not set");
            }

            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // ✅ PASS USERNAME + PASSWORD
            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to PostgreSQL", e);
        }
    }
}
