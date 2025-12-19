package com.examapp.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class ApplySqlToNeon {
    public static void main(String[] args) {
        String path = args.length > 0 ? args[0] : "sql_setup_postgres.sql";
        System.out.println("Applying SQL from: " + path);
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to obtain DB connection. Check NEON_DB_URL/credentials.");
                System.exit(2);
            }
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Skip SQL comments that start with --
                    if (line.trim().startsWith("--") || line.trim().isEmpty()) continue;
                    sb.append(line).append("\n");
                }
            }
            String[] statements = sb.toString().split(";\n");
            for (String stmt : statements) {
                stmt = stmt.trim();
                if (stmt.isEmpty()) continue;
                System.out.println("Executing statement: " + (stmt.length() > 80 ? stmt.substring(0, 80) + "..." : stmt));
                try (Statement s = conn.createStatement()) {
                    s.execute(stmt);
                }
            }
            System.out.println("SQL script applied successfully.");
        } catch (Exception e) {
            System.err.println("Error applying SQL:");
            e.printStackTrace();
            System.exit(3);
        }
    }
}