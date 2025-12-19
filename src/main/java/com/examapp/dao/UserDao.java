package com.examapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;

public class UserDao {

    // ================= LOGIN =================
    // used by LoginServlet
    public boolean validate(String username, String password) {

        String sql = "SELECT password FROM users WHERE username = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    return BCrypt.checkpw(password, hashedPassword);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= REGISTER =================
    // used by RegisterServlet
    public boolean register(String username, String password) {

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // ✅ FIXED SQL (PostgreSQL / Neon compatible)
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();   // shows real DB error in logs
            return false;
        }
    }
}
