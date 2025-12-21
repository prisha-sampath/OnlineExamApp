package com.examapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            String url = System.getenv("DATABASE_URL");

            if (url == null || url.isEmpty()) {
                throw new RuntimeException("DATABASE_URL not set");
            }

            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url);

        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to PostgreSQL", e);
        }
    }
}
