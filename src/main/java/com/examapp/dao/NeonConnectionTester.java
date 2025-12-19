package com.examapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NeonConnectionTester {
    public static void main(String[] args) {
        String url = System.getenv("NEON_DB_URL");
        String user = System.getenv("NEON_DB_USER");
        String password = System.getenv("NEON_DB_PASSWORD");

        if (url == null || user == null || password == null) {
            System.err.println("Environment variables NEON_DB_URL, NEON_DB_USER and NEON_DB_PASSWORD must be set.");
            System.exit(1);
        }

        System.out.println("Attempting connection to: " + url);
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection c = DriverManager.getConnection(url, user, password)) {
                System.out.println("Connection successful!");
                System.out.println("Database product: " + c.getMetaData().getDatabaseProductName() + " " + c.getMetaData().getDatabaseProductVersion());
            }
        } catch (SQLException se) {
            System.err.println("SQL error while connecting to Neon:");
            se.printStackTrace();
            System.exit(2);
        } catch (Exception e) {
            System.err.println("Unexpected error:");
            e.printStackTrace();
            System.exit(3);
        }
    }
}