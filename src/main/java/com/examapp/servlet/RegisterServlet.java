package com.examapp.servlet;

import com.examapp.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate input
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            response.sendRedirect("register.jsp?error=1");
            return;
        }

        // Check passwords match
        if (!password.equals(confirmPassword)) {
            response.sendRedirect("register.jsp?error=1");
            return;
        }

        // Register user
        UserDao userDao = new UserDao();
        boolean success = userDao.register(username, password);

        if (success) {
            response.sendRedirect("login.jsp?success=1");
        } else {
            response.sendRedirect("register.jsp?error=1");
        }
    }
}
