package com.examapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectionPostgres {
    // Default Neon DB connection (will be used if environment variables not set)
    private static final String DEFAULT_URL =
            "jdbc:postgresql://ep-cool-unit-ahhc47lv-pooler.c-3.us-east-1.aws.neon.tech/neondb?sslmode=require&channelBinding=require";

    private static final String DEFAULT_USER = "neondb_owner";
    private static final String DEFAULT_PASSWORD = "npg_TBAuN1K8pQXR";

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            // Allow overriding via environment variables for security and flexibility
            String url = System.getenv("NEON_DB_URL");
            String user = System.getenv("NEON_DB_USER");
            String password = System.getenv("NEON_DB_PASSWORD");

            if (url == null || url.isEmpty()) {
                url = DEFAULT_URL;
            }
            if (user == null || user.isEmpty()) {
                user = DEFAULT_USER;
            }
            if (password == null || password.isEmpty()) {
                password = DEFAULT_PASSWORD;
            }

            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
