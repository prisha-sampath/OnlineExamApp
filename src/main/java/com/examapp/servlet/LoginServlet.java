package com.examapp.servlet;

import com.examapp.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDao userDao = new UserDao();
        boolean isValid = userDao.validate(username, password);

        if (isValid) {
            HttpSession session = request.getSession();
            response.sendRedirect("selectLanguage.jsp");
        } else {
            response.sendRedirect("login.jsp?error=1");
        }
    }
}
