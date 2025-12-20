package com.examapp.servlet;

import com.examapp.dao.UserDao;
import com.examapp.dao.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirmPassword");

        // password mismatch
        if (password == null || confirm == null || !password.equals(confirm)) {
            response.sendRedirect("register.jsp?error=1");
            return;
        }

        String sql = "INSERT INTO users(username, password) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ps.executeUpdate();
            response.sendRedirect("login.jsp");

        } catch (Exception e) {
            e.printStackTrace(); // IMPORTANT for logs
            response.sendRedirect("register.jsp?error=1");
        }
    }
}
