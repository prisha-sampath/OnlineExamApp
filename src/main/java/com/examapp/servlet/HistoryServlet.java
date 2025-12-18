package com.examapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.examapp.dao.QuizResultsDao;
import com.examapp.model.User;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HistoryServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        List<Map<String, Object>> userHistory = QuizResultsDao.getResultsByUsername(user.getUsername());
        
        session.setAttribute("userHistory", userHistory);
        request.getRequestDispatcher("history.jsp").forward(request, response);
    }
}
