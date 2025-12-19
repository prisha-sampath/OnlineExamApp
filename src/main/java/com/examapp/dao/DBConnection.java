package com.examapp.dao;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Prefer explicit Neon env vars; fall back to DATABASE_URL (platform convention).
    private static final String NEON_URL = System.getenv("NEON_DB_URL");
    private static final String NEON_USER = System.getenv("NEON_DB_USER");
    private static final String NEON_PASSWORD = System.getenv("NEON_DB_PASSWORD");
    private static final String DATABASE_URL = System.getenv("DATABASE_URL");

    public static Connection getConnection() {
        String url = null;
        String user = null;
        String password = null;

        if (NEON_URL != null && !NEON_URL.isEmpty()) {
            url = NEON_URL;
            user = NEON_USER;
            password = NEON_PASSWORD;
        } else if (DATABASE_URL != null && !DATABASE_URL.isEmpty()) {
            // DATABASE_URL often has the form: postgres://user:pass@host:port/dbname
            // Convert it to JDBC URL and extract user/password if present.
            try {
                URI dbUri = new URI(DATABASE_URL);
                String scheme = dbUri.getScheme();
                if (scheme != null && scheme.startsWith("postgres")) {
                    String host = dbUri.getHost();
                    int port = dbUri.getPort() == -1 ? 5432 : dbUri.getPort();
                    String path = dbUri.getPath(); // /dbname
                    url = "jdbc:postgresql://" + host + ":" + port + path + "?sslmode=require";
                    if (dbUri.getUserInfo() != null) {
                        String[] parts = dbUri.getUserInfo().split(":", 2);
                        user = parts[0];
                        if (parts.length > 1) password = parts[1];
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException("Invalid DATABASE_URL format", e);
            }
        }

        if (url == null) {
            throw new IllegalStateException("No database configuration found. Set NEON_DB_URL/NEON_DB_USER/NEON_DB_PASSWORD or DATABASE_URL.");
        }

        try {
            Class.forName("org.postgresql.Driver");
            if (user != null && password != null) {
                return DriverManager.getConnection(url, user, password);
            } else {
                // If user/password not present (uncommon), attempt connection with URL only
                return DriverManager.getConnection(url);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to connect to Postgres database", e);
        }
    }
}
