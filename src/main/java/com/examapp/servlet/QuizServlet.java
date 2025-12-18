package com.examapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class QuizServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int score = 0;
        String q1Answer = request.getParameter("q1");
        String q2Answer = request.getParameter("q2");

        if ("Paris".equals(q1Answer)) {
            score++;
        }
        if ("4".equals(q2Answer)) {
            score++;
        }

        session.setAttribute("score", score);
        response.sendRedirect("result.jsp");
    }
}
