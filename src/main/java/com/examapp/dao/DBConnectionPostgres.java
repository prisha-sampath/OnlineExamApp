package com.examapp.dao;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectionPostgres {

    public static Connection getConnection() {
        try {
            // Read from Render Environment Variable
            String databaseUrl = System.getenv("DATABASE_URL");

            if (databaseUrl == null) {
                throw new RuntimeException("DATABASE_URL not found");
            }

            // Convert string to URI
            URI uri = new URI(databaseUrl);

            String userInfo = uri.getUserInfo(); // user:password
            String[] parts = userInfo.split(":");

            String username = parts[0];
            String password = parts[1];

            String jdbcUrl = "jdbc:postgresql://" +
                    uri.getHost() +
                    ":" + (uri.getPort() == -1 ? 5432 : uri.getPort()) +
                    uri.getPath() +
                    "?sslmode=require";

            return DriverManager.getConnection(jdbcUrl, username, password);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to Neon PostgreSQL");
        }
    }
}
