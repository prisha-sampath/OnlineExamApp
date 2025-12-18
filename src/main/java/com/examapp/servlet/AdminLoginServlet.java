package com.examapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AdminLoginServlet extends HttpServlet {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("admin", true);
            session.setAttribute("adminName", username);
            response.sendRedirect("adminDashboard.jsp");
        } else {
            response.sendRedirect("adminLogin.jsp?error=Invalid credentials");
        }
    }
}
