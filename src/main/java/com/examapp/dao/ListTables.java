package com.examapp.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ListTables {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.err.println("No DB connection. Ensure NEON_DB_URL or MySQL env vars are set.");
                System.exit(2);
            }
            try (Statement s = conn.createStatement()) {
                ResultSet rs = s.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public' ORDER BY table_name;");
                System.out.println("Public tables:");
                while (rs.next()) {
                    System.out.println(" - " + rs.getString(1));
                }
            }
        } catch (Exception e) {
            System.err.println("Error listing tables:");
            e.printStackTrace();
            System.exit(3);
        }
    }
}