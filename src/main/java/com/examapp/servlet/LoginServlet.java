package com.examapp.servlet;

import com.examapp.dao.UserDao;
import com.examapp.model.User;
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
            // store a User object expected by other servlets
            User user = new User();
            user.setUsername(username);
            session.setAttribute("user", user);
            // keep username for compatibility
            session.setAttribute("username", username);
            response.sendRedirect(request.getContextPath() + "/selectLanguage.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
        }
    }
}
