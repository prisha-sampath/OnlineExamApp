package com.examapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;

public class UserDao {

    // used by LoginServlet
    public boolean validate(String username, String password) {

        String sql = "SELECT password FROM users WHERE username = ?";

        try (
            // ✅ USE THIS (auto switch)
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashed = rs.getString("password");
                    return BCrypt.checkpw(password, hashed);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // used by RegisterServlet
    public boolean register(String username, String password) {

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String sql = "INSERT INTO users (username, password, created_date) VALUES (?, ?, NOW())";

        try (
            // ✅ USE THIS (auto switch)
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, username);
            ps.setString(2, hashed);

            int rows = ps.executeUpdate();
            return rows == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
