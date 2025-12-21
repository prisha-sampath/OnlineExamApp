package com.examapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            // Get Neon PostgreSQL connection string
            String url = System.getenv("DATABASE_URL");

            if (url == null || url.isEmpty()) {
                throw new RuntimeException("DATABASE_URL not set");
            }

            // Force PostgreSQL JDBC driver loading
            Class.forName("org.postgresql.Driver");

            // Connect using ONLY the URL
            return DriverManager.getConnection(url);

        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to PostgreSQL", e);
        }
    }
}
